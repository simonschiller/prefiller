/*
 * Copyright 2020 Simon Schiller
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("DEPRECATION") // Using deprecated APIs to maintain backwards compatibility

package io.github.simonschiller.prefiller

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.api.AndroidSourceSet
import com.android.build.gradle.api.BaseVariant
import com.android.builder.model.AndroidProject.FD_GENERATED
import com.google.devtools.ksp.gradle.KspExtension
import io.github.simonschiller.prefiller.internal.util.Version
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.property
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import java.util.*

class DatabaseConfig internal constructor(val name: String, objects: ObjectFactory) {
    val classname: Property<String> = objects.property()
    val script: RegularFileProperty = objects.fileProperty()

    internal fun registerTasks(project: Project, variant: BaseVariant) {
        val variantName = variant.name.capitalize(Locale.ROOT)
        val databaseName = name.capitalize(Locale.ROOT)
        val taskName = "prefill$databaseName${variantName}Database"

        // Validate configuration
        val classname = classname.orNull ?: error("No classname configured for database $name")
        val script = script.asFile.orNull ?: error("No script configured for database $name")
        check(script.isFile && script.canRead() && script.extension.equals("sql", ignoreCase = true)) {
            "Cannot locate script at $script, make sure you specify a valid .sql file"
        }

        val databaseDir = project.layout.buildDirectory.dir("$FD_GENERATED/prefiller/${variant.name}")
        val databaseFile = databaseDir.map { dir -> dir.file("$name.db") }
        val schemaLocation = getSchemaLocation(project, variant)

        // Register task for database
        val task = project.tasks.register(taskName, PrefillerTask::class.java) {
            description = "Generates and pre-fills ${this@DatabaseConfig.name} database for variant ${variant.name}"
            scriptFile.set(script)
            generatedDatabaseFile.set(databaseFile)

            // On Gradle versions earlier than 6.3, we have to resolve the path manually due to a bug
            val schemaDir = schemaLocation.map { parentDir ->
                if (Version.parse(project.gradle.gradleVersion) < Version.parse("6.3")) {
                    val fileProvider = project.provider { parentDir.asFile.resolve(classname) }
                    project.layout.dir(fileProvider).get()
                } else {
                    parentDir.dir(classname)
                }
            }
            schemaDirectory.set(schemaDir)

            // Room schema has to be generated before the Prefiller task runs
            dependsOn(variant.javaCompileProvider)
        }

        // Register the output directory as asset directory and hook task into build process
        val sourceSets = variant.sourceSets.filterIsInstance<AndroidSourceSet>()
        sourceSets.last().assets.srcDir(databaseDir) // Last source set is the most specific one
        project.tasks.named("generate${variantName}Assets").configure {
            dependsOn(task)
        }
    }

    // Read the Room schema location from the annotation processor options
    private fun getSchemaLocation(project: Project, variant: BaseVariant): Provider<Directory> {
        val fileProvider = project.provider {
            val kaptSchemaLocation = getKaptSchemaLocation(project, variant)
            val kspSchemaLocation = getKspSchemaLocation(project)
            val javaAptSchemaLocation = getJavaAptSchemaLocation(variant)

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

    private fun getKaptSchemaLocation(project: Project, variant: BaseVariant): String? = try {
        val kaptExtension = project.extensions.findByType(KaptExtension::class.java)
        val androidExtension = project.extensions.findByType(BaseExtension::class.java)
        val arguments = kaptExtension?.getAdditionalArguments(project, variant, androidExtension)
        arguments?.get(SCHEMA_LOCATION_KEY)
    } catch (exception: NoClassDefFoundError) {
        null // KAPT plugin not applied
    }

    private fun getKspSchemaLocation(project: Project): String? = try {
        val kspExtension = project.extensions.findByType(KspExtension::class.java)
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

    private fun getJavaAptSchemaLocation(variant: BaseVariant): String? {
        val arguments = variant.javaCompileOptions.annotationProcessorOptions.arguments
        return arguments[SCHEMA_LOCATION_KEY]
    }

    private companion object {
        private const val SCHEMA_LOCATION_KEY = "room.schemaLocation"
    }
}
