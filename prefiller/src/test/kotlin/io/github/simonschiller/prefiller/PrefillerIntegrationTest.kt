package io.github.simonschiller.prefiller

import io.github.simonschiller.prefiller.testutil.*
import io.github.simonschiller.prefiller.testutil.spec.KotlinProjectSpec
import io.github.simonschiller.prefiller.testutil.spec.NonAndroidProjectSpec
import io.github.simonschiller.prefiller.testutil.spec.ProjectSpec
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.RegisterExtension
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource

class PrefillerIntegrationTest {

    @JvmField
    @RegisterExtension
    val project = ProjectExtension()

    @ParameterizedTest
    @ArgumentsSource(TestVersions::class)
    fun `Build fails if plugin is applied to non-Android projects`(gradleVersion: String, agpVersion: String) {
        project.setup(gradleVersion, agpVersion, NonAndroidProjectSpec())
        val result = project.run("clean", expectFailure = true)
        assertTrue(result.output.contains("Prefiller is only applicable to Android projects"))
    }

    @ParameterizedTest
    @ArgumentsSource(TestVersions::class)
    fun `Build fails if schema directory is not found`(gradleVersion: String, agpVersion: String) {
        project.setup(gradleVersion, agpVersion, KotlinProjectSpec())

        var content = project.moduleBuildGradle.readText()
        content = content.replace("com.test.PeopleDatabase", "com.test.InvalidDatabase")
        project.moduleBuildGradle.writeText(content)

        val result = project.run("prefillPeopleDebugDatabase", expectFailure = true)
        assertTrue(result.output.contains("'schemaDirectory' does not exist"))

    }

    @ParameterizedTest
    @ArgumentsSource(TestVersions::class)
    fun `Build fails if SQL file is invalid`(gradleVersion: String, agpVersion: String) {
        project.setup(gradleVersion, agpVersion, KotlinProjectSpec())
        project.scriptFile.appendText("Normal text is not a valid SQL statement")

        val result = project.run("prefillPeopleDebugDatabase", expectFailure = true)
        assertTrue(result.output.contains("Could not parse script"))
    }

    @ParameterizedTest
    @ArgumentsSource(NoSchemaLocationTestVersions::class)
    fun `Build fails if schema location is not configured`(gradleVersion: String, agpVersion: String, projectSpec: ProjectSpec) {
        project.setup(gradleVersion, agpVersion, projectSpec)
        val result = project.run("prefillPeopleDebugDatabase", expectFailure = true)
        assertTrue(result.output.contains("Could not find schema location"))
    }

    @ParameterizedTest
    @ArgumentsSource(LanguageTestVersions::class)
    fun `Pre-filled database file is generated`(gradleVersion: String, agpVersion: String, projectSpec: ProjectSpec) {
        project.setup(gradleVersion, agpVersion, projectSpec)
        project.run("prefillPeopleDebugDatabase")

        val databaseFile = project.moduleDir.resolve("build/generated/prefiller/debug/people.db")
        assertTrue(databaseFile.exists())
    }

    @ParameterizedTest
    @ArgumentsSource(LanguageTestVersions::class)
    fun `Database is generated during normal build process`(gradleVersion: String, agpVersion: String, projectSpec: ProjectSpec) {
        project.setup(gradleVersion, agpVersion, projectSpec)
        project.run("assembleDebug")

        val databaseFile = project.moduleDir.resolve("build/generated/prefiller/debug/people.db")
        assertTrue(databaseFile.exists())
    }

    @ParameterizedTest
    @ArgumentsSource(LanguageTestVersions::class)
    fun `Generated database is registered correctly as Android asset`(gradleVersion: String, agpVersion: String, projectSpec: ProjectSpec) {
        project.setup(gradleVersion, agpVersion, projectSpec)
        project.run("mergeDebugAssets")

        val mergedAssetsDir = project.moduleDir.resolve("build/intermediates/merged_assets")
        val databaseFile = mergedAssetsDir.resolve("debug/out/people.db")
        assertTrue(databaseFile.exists())
    }
}
