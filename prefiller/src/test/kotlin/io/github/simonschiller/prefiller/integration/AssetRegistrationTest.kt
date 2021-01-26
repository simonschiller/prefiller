package io.github.simonschiller.prefiller.integration

import io.github.simonschiller.prefiller.testutil.LanguageTestVersions
import io.github.simonschiller.prefiller.testutil.spec.ProjectSpec
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource

class AssetRegistrationTest : BaseIntegrationTest() {

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