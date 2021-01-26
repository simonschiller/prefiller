package io.github.simonschiller.prefiller.integration

import io.github.simonschiller.prefiller.testutil.TestVersions
import io.github.simonschiller.prefiller.testutil.outcomeOf
import io.github.simonschiller.prefiller.testutil.spec.KotlinProjectSpec
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource

class UpToDateChecksTest : BaseIntegrationTest() {

    @ParameterizedTest
    @ArgumentsSource(TestVersions::class)
    fun `Up-to-date checks work correctly`(gradleVersion: String, agpVersion: String) {
        project.setup(gradleVersion, agpVersion, KotlinProjectSpec())

        var result = project.run("prefillPeopleDebugDatabase")
        assertEquals(TaskOutcome.SUCCESS, result.tasks.outcomeOf("prefillPeopleDebugDatabase"))

        result = project.run("prefillPeopleDebugDatabase")
        assertEquals(TaskOutcome.UP_TO_DATE, result.tasks.outcomeOf("prefillPeopleDebugDatabase"))

        // Trigger change
        project.scriptFile.appendText("""
            INSERT INTO people(name, age) VALUES ("Jorja Maddox", 39);
        """.trimIndent())

        result = project.run("prefillPeopleDebugDatabase")
        assertEquals(TaskOutcome.SUCCESS, result.tasks.outcomeOf("prefillPeopleDebugDatabase"))

        result = project.run("prefillPeopleDebugDatabase")
        assertEquals(TaskOutcome.UP_TO_DATE, result.tasks.outcomeOf("prefillPeopleDebugDatabase"))
    }
}