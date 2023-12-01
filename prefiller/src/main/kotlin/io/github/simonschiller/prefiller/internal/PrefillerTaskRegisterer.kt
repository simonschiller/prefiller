package io.github.simonschiller.prefiller.internal

import com.android.SdkConstants
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.api.AndroidSourceSet
import com.android.build.gradle.api.BaseVariant
import com.google.devtools.ksp.gradle.KspExtension
import io.github.simonschiller.prefiller.DatabaseConfig
import io.github.simonschiller.prefiller.PrefillerTask
import io.github.simonschiller.prefiller.internal.util.Version
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import java.util.*

internal class PrefillerTaskRegisterer(
    private val project: Project,
    private val variant: BaseVariant
) {
    private val extensions: ExtensionContainer = project.extensions
    private val providerFactory: ProviderFactory = project.providers
    private val databaseDir = project.layout.buildDirectory.dir(
        "${SdkConstants.FD_GENERATED}/prefiller/${variant.name}"
    )

    fun registerTasks(config: DatabaseConfig) {
        val variantName = variant.name.capitalizeAscii()
        val databaseName = config.name.capitalizeAscii()
        val taskName = "prefill$databaseName${variantName}Database"

        // Validate configuration
        val classname = config.classname.orNull ?: error("No classname configured for database ${config.name}")

        val databaseFile = databaseDir.map { dir -> dir.file("${config.name}.db") }
        val schemaLocation = getSchemaLocation()

        // Register task for database
        val task = project.tasks.register(taskName, PrefillerTask::class.java) { prefillerTask ->
            prefillerTask.description = "Generates and pre-fills ${config.name} database for variant ${variant.name}"
            prefillerTask.generatedDatabaseFile.set(databaseFile)

            // Compatibility until the script property has been fully removed
            if (config.script.isPresent) {
                project.logger.warn("Deprecated 'script' property was used, please use 'scripts' instead.")
                prefillerTask.scriptFiles.setFrom(config.script)
            } else {
                prefillerTask.scriptFiles.setFrom(config.scripts)
            }

            // On Gradle versions earlier than 6.3, we have to resolve the path manually due to a bug
            val schemaDir = schemaLocation.map { parentDir ->
                if (Version.parse(project.gradle.gradleVersion) < Version.parse("6.3")) {
                    val fileProvider = project.provider { parentDir.asFile.resolve(classname) }
                    project.layout.dir(fileProvider).get()
                } else {
                    parentDir.dir(classname)
                }
            }
            prefillerTask.schemaDirectory.set(schemaDir)

            // Room schema has to be generated before the Prefiller task runs
            prefillerTask.dependsOn(variant.javaCompileProvider)
        }

        project.tasks.named("generate${variantName}Assets").configure { generateAssetsTask ->
            generateAssetsTask.dependsOn(task)
        }
    }

    fun setupSourceSets() {
        // Register the output directory as asset directory and hook task into build process
        val sourceSets = variant.sourceSets.filterIsInstance<AndroidSourceSet>()
        sourceSets.last().assets.srcDir(databaseDir) // Last source set is the most specific one
    }

    // Read the Room schema location from the annotation processor options
    private fun getSchemaLocation(): Provider<Directory> {
        val fileProvider = providerFactory.provider {
            val kaptSchemaLocation = getKaptSchemaLocation()
            val kspSchemaLocation = getKspSchemaLocation()
            val javaAptSchemaLocation = getJavaAptSchemaLocation()

            val schemaLocation = when {
                kaptSchemaLocation != null -> kaptSchemaLocation
                kspSchemaLocation != null -> kspSchemaLocation
                javaAptSchemaLocation != null -> javaAptSchemaLocation
                else -> error("Could not find schema location")
            }
            project.file(schemaLocation)
        }
        return project.layout.dir(fileProvider)
    }

    private fun getKaptSchemaLocation(): String? = try {
        val kaptExtension = extensions.findByType(KaptExtension::class.java)
        val androidExtension = extensions.findByType(BaseExtension::class.java)
        val arguments = kaptExtension?.getAdditionalArguments(project, variant, androidExtension)
        arguments?.get(SCHEMA_LOCATION_KEY)
    } catch (exception: NoClassDefFoundError) {
        null // KAPT plugin not applied
    }

    private fun getKspSchemaLocation(): String? = try {
        val kspExtension = extensions.findByType(KspExtension::class.java)
        // Arguments were only made public recently (https://github.com/google/ksp/pull/445), so
        // we're using reflection for now to stay stay compatible with some older versions.
        // TODO: Switch to public API once it has been released for a while
        val method = kspExtension?.javaClass?.getMethod("getApOptions\$gradle_plugin")
        @Suppress("UNCHECKED_CAST")
        val arguments = method?.invoke(kspExtension) as? Map<String, String>
        arguments?.get(SCHEMA_LOCATION_KEY)
    } catch (exception: NoClassDefFoundError) {
        null // KSP plugin not applied
    }

    private fun getJavaAptSchemaLocation(): String? {
        val arguments = variant.javaCompileOptions.annotationProcessorOptions.arguments
        return arguments[SCHEMA_LOCATION_KEY]
    }

    private companion object {
        private const val SCHEMA_LOCATION_KEY = "room.schemaLocation"

        @Suppress("DEPRECATION")
        private fun String.capitalizeAscii(): String = capitalize(Locale.ROOT)
    }
}
