package io.github.simonschiller.prefiller.integration

import io.github.simonschiller.prefiller.testutil.TestVersions
import io.github.simonschiller.prefiller.testutil.spec.KotlinProjectSpec
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource

class NoSchemaDirectoryTest : BaseIntegrationTest() {

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
}