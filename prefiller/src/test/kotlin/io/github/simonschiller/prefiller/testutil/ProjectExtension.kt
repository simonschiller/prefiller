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

package io.github.simonschiller.prefiller.testutil

import io.github.simonschiller.prefiller.testutil.spec.ProjectSpec
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.BuildTask
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import java.io.File
import java.nio.file.Files

class ProjectExtension : BeforeEachCallback, AfterEachCallback {
    private lateinit var rootDir: File
    private val gradleVersionFile: File get() = rootDir.resolve("gradle.version")

    val rootBuildGradle: File get() = rootDir.resolve("build.gradle")
    val moduleBuildGradle: File get() = moduleDir.resolve("build.gradle")

    val moduleDir: File get() = rootDir.resolve("module")
    val scriptFile: File get() = moduleDir.resolve("setup.sql")

    override fun beforeEach(context: ExtensionContext) {
        rootDir = Files.createTempDirectory("prefiller-test").toFile()
    }

    override fun afterEach(context: ExtensionContext) {
        rootDir.deleteRecursively()
    }

    fun setup(gradleVersion: String, agpVersion: String, projectSpec: ProjectSpec) {
        val mainDir = moduleDir.resolve("src/main").also { it.mkdirs() }
        val codeDir = mainDir.resolve("java/com/test").also { it.mkdirs() }

        val settingsGradle = rootDir.resolve("settings.gradle")
        settingsGradle.writeText(projectSpec.getSettingsGradleContent())

        val gradleProperties = rootDir.resolve("gradle.properties")
        gradleProperties.writeText(projectSpec.getGradlePropertiesContent())

        val localProperties = rootDir.resolve("local.properties")
        localProperties.writeText(projectSpec.getLocalPropertiesContent())

        rootBuildGradle.writeText(projectSpec.getRootBuildGradleContent(agpVersion))
        moduleBuildGradle.writeText(projectSpec.getModuleBuildGradleContent())

        val androidManifest = mainDir.resolve("AndroidManifest.xml")
        androidManifest.writeText(projectSpec.getAndroidManifestContent())

        val entityClass = codeDir.resolve(projectSpec.getEntityClassName())
        entityClass.writeText(projectSpec.getEntityClassContent())

        val databaseClass = codeDir.resolve(projectSpec.getDatabaseClassName())
        databaseClass.writeText(projectSpec.getDatabaseClassContent())

        scriptFile.writeText(projectSpec.getScriptFileContent())
        gradleVersionFile.writeText(gradleVersion)

        projectSpec.generateAdditionalFiles(rootDir)
    }

    fun run(vararg arguments: String, expectFailure: Boolean = false): BuildResult {
        val gradleVersion = gradleVersionFile.readText()

        val runner = GradleRunner.create()
            .withProjectDir(rootDir)
            .withGradleVersion(gradleVersion)
            .withArguments(*arguments, "--stacktrace")
            .forwardOutput()

        return if (expectFailure) {
            runner.buildAndFail()
        } else {
            runner.build()
        }
    }
}

fun List<BuildTask>.outcomeOf(taskName: String): TaskOutcome {
    val task = singleOrNull { it.path == ":module:$taskName" } ?: error("Could not find task with name $taskName")
    return task.outcome
}
