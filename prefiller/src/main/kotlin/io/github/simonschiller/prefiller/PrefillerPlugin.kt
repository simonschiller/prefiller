package io.github.simonschiller.prefiller

import com.android.build.gradle.*
import org.gradle.api.Plugin
import org.gradle.api.Project

class PrefillerPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.create("prefiller", PrefillerExtension::class.java)

        // Eagerly fail the build if the AGP is not available
        if (!project.plugins.hasPlugin("com.android.internal.version-check")) {
            error("Prefiller is only applicable to Android projects")
        }

        // Register tasks when Android plugins are available
        project.plugins.configureEach {
            val variants = when (this) {
                is AppPlugin, is DynamicFeaturePlugin -> project.extensions.getByType(AppExtension::class.java).applicationVariants
                is LibraryPlugin -> project.extensions.getByType(LibraryExtension::class.java).libraryVariants
                else -> null
            }

            // Register prefiller tasks
            variants?.configureEach {
                extension.databaseConfigs.forEach { config -> config.registerTasks(project, this) }
            }
        }
    }
}
