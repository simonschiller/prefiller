package io.github.simonschiller.prefiller.integration

import io.github.simonschiller.prefiller.testutil.LanguageTestVersions
import io.github.simonschiller.prefiller.testutil.spec.ProjectSpec
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource

class BuildLifecycleTest : BaseIntegrationTest() {

    @ParameterizedTest
    @ArgumentsSource(LanguageTestVersions::class)
    fun `Database is generated during normal build process`(gradleVersion: String, agpVersion: String, projectSpec: ProjectSpec) {
        project.setup(gradleVersion, agpVersion, projectSpec)
        project.run("assembleDebug")

        val databaseFile = project.moduleDir.resolve("build/generated/prefiller/debug/people.db")
        assertTrue(databaseFile.exists())
    }
}