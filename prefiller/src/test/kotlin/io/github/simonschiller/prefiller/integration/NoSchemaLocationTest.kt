package io.github.simonschiller.prefiller.integration

import io.github.simonschiller.prefiller.testutil.NoSchemaLocationTestVersions
import io.github.simonschiller.prefiller.testutil.spec.ProjectSpec
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource

class NoSchemaLocationTest : BaseIntegrationTest() {

    @ParameterizedTest
    @ArgumentsSource(NoSchemaLocationTestVersions::class)
    fun `Build fails if schema location is not configured`(gradleVersion: String, agpVersion: String, projectSpec: ProjectSpec) {
        project.setup(gradleVersion, agpVersion, projectSpec)
        val result = project.run("prefillPeopleDebugDatabase", expectFailure = true)
        assertTrue(result.output.contains("Could not find schema location"))
    }
}