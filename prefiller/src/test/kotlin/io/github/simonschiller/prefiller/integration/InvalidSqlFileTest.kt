package io.github.simonschiller.prefiller.integration

import io.github.simonschiller.prefiller.testutil.TestVersions
import io.github.simonschiller.prefiller.testutil.spec.KotlinProjectSpec
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource

class InvalidSqlFileTest : BaseIntegrationTest() {

    @ParameterizedTest
    @ArgumentsSource(TestVersions::class)
    fun `Build fails if SQL file is invalid`(gradleVersion: String, agpVersion: String) {
        project.setup(gradleVersion, agpVersion, KotlinProjectSpec())
        project.scriptFile.appendText("Normal text is not a valid SQL statement")

        val result = project.run("prefillPeopleDebugDatabase", expectFailure = true)
        assertTrue(result.output.contains("Could not parse script"))
    }
}