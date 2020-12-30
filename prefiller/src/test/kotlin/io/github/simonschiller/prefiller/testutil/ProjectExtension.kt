package io.github.simonschiller.prefiller.testutil

import io.github.simonschiller.prefiller.testutil.spec.ProjectSpec
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
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
