package io.github.simonschiller.prefiller.integration

import io.github.simonschiller.prefiller.testutil.TestVersions
import io.github.simonschiller.prefiller.testutil.spec.NonAndroidProjectSpec
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource

class NonAndroidProjectTest : BaseIntegrationTest() {

    @ParameterizedTest
    @ArgumentsSource(TestVersions::class)
    fun `Build fails if plugin is applied to non-Android projects`(gradleVersion: String, agpVersion: String) {
        project.setup(gradleVersion, agpVersion, NonAndroidProjectSpec())
        val result = project.run("clean", expectFailure = true)
        assertTrue(result.output.contains("Prefiller is only applicable to Android projects"))
    }
}
