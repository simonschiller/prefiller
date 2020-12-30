package io.github.simonschiller.prefiller

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.api.AndroidSourceSet
import com.android.build.gradle.api.BaseVariant
import com.android.builder.model.AndroidProject.FD_GENERATED
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import java.io.File
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class DatabaseConfig(val name: String) {
    var classname: String? = null
    var script: File? = null

    internal fun registerTasks(project: Project, variant: BaseVariant) {
        val variantName = variant.name.capitalize(Locale.ROOT)
        val databaseName = name.capitalize(Locale.ROOT)
        val taskName = "prefill$databaseName${variantName}Database"

        // Validate configuration
        val classname = classname ?: error("No classname configured for database $name")
        val script = script ?: error("No script configured for database $name")
        check(script.isFile && script.canRead() && script.extension.equals("sql", ignoreCase = true)) {
            "Cannot locate script at $script, make sure you specify a valid .sql file"
        }

        val databaseFile = File(project.buildDir, "${FD_GENERATED}/prefiller/${variant.name}/$name.db")
        val schemaLocation = getSchemaLocation(project, variant)

        // Register task for database
        val task = project.tasks.register(taskName, PrefillerTask::class.java) {
            description = "Generates and pre-fills ${this@DatabaseConfig.name} database for variant ${variant.name}"
            schemaDirectory =  schemaLocation.resolve(classname)
            scriptFile = script
            generatedDatabaseFile = databaseFile

            // Room schema has to be generated before the Prefiller task runs
            dependsOn(variant.javaCompileProvider)
        }

        // Register the output directory as asset directory and hook task into build process
        val sourceSets = variant.sourceSets.filterIsInstance<AndroidSourceSet>()
        sourceSets.last().assets.srcDir(databaseFile.parent) // Last source set is the most specific one
        variant.mergeAssetsProvider.configure {
            dependsOn(task)
        }
    }

    // Read the Room schema location from the annotation processor options
    private fun getSchemaLocation(project: Project, variant: BaseVariant): File {
        val kaptExtension = project.extensions.findByType(KaptExtension::class.java)
        val arguments = if (kaptExtension != null) {
            val androidExtension = project.extensions.getByType(BaseExtension::class.java)
            kaptExtension.getAdditionalArguments(project, variant, androidExtension)
        } else {
            variant.javaCompileOptions.annotationProcessorOptions.arguments
        }

        // Try to read the schema location from the annotation processor arguments
        val location = arguments?.get("room.schemaLocation") ?: error("Could not find schema location")
        return File(location)
    }
}
