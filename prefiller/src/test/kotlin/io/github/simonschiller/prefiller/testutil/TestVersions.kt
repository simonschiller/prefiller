package io.github.simonschiller.prefiller.testutil

import io.github.simonschiller.prefiller.testutil.spec.*
import org.gradle.util.VersionNumber
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.util.stream.Stream

open class TestVersions : ArgumentsProvider {

    // See https://gradle.org/releases
    private val gradleVersions = listOf(
        "7.0.2",
        "6.8.3",
        "6.7.1",
        "6.6.1",
        "6.5.1",
        "6.4.1",
        "6.3",
        "6.2.2",
        "6.1.1"
    )

    // See https://developer.android.com/studio/releases/gradle-plugin
    private val agpVersions = listOf(
        "4.2.0",
        "4.1.2",
        "4.0.2"
    )

    override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
        val arguments = mutableListOf<Arguments>()
        gradleVersions().forEach { gradleVersion ->
            agpVersions().forEach { agpVersion ->
                if (agpVersion.baseVersion isCompatibleWith gradleVersion.baseVersion) {
                    arguments += Arguments.of(gradleVersion.toGradleString(), agpVersion.toString())
                }
            }
        }

        require(arguments.isNotEmpty()) {
            "Found no compatible AGP and Gradle version combination, check your supplied arguments."
        }
        return arguments.stream()
    }

    // Allow setting a single, fixed Gradle version via environment variables
    private fun gradleVersions(): List<VersionNumber> {
        val gradleVersion = System.getenv("GRADLE_VERSION")
        return if (gradleVersion == null) {
            gradleVersions.map(VersionNumber::parse)
        } else {
            listOf(VersionNumber.parse(gradleVersion))
        }
    }

    // Allow setting a single, fixed AGP version via environment variables
    private fun agpVersions(): List<VersionNumber> {
        val agpVersion = System.getenv("AGP_VERSION")
        return if (agpVersion == null) {
            agpVersions.map(VersionNumber::parse)
        } else {
            listOf(VersionNumber.parse(agpVersion))
        }
    }

    // Checks if a AGP version (receiver) is compatible with a certain version of Gradle
    private infix fun VersionNumber.isCompatibleWith(gradleVersion: VersionNumber) = when {
        this >= VersionNumber.parse("4.2.0") -> gradleVersion >= VersionNumber.parse("6.7.1")
        this >= VersionNumber.parse("4.1.0") -> gradleVersion >= VersionNumber.parse("6.5")
        this >= VersionNumber.parse("4.0.0") -> gradleVersion >= VersionNumber.parse("6.1.1") && gradleVersion < VersionNumber.parse("7.0")
        else -> false
    }

    // Converts a VersionNumber into a String without trailing 0 versions (keeping at least one .)
    private fun VersionNumber.toGradleString(): String {
        var version = toString()
        var dots = version.count { it == '.' }
        while (version.endsWith(".0") && dots > 1) {
            version = version.substring(0, version.length - 2)
            dots--
        }
        return version
    }
}

class LanguageTestVersions : ArgumentsProvider, TestVersions() {

    override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
        val arguments = mutableListOf<Arguments>()
        super.provideArguments(context).forEach { argument ->
            val (gradleVersion, agpVersion) = argument.get()
            arguments += Arguments.of(gradleVersion, agpVersion, KotlinProjectSpec())
            arguments += Arguments.of(gradleVersion, agpVersion, JavaProjectSpec())
        }
        return arguments.stream()
    }
}

class NoSchemaLocationTestVersions : ArgumentsProvider, TestVersions() {

    override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
        val arguments = mutableListOf<Arguments>()
        super.provideArguments(context).forEach { argument ->
            val (gradleVersion, agpVersion) = argument.get()
            arguments += Arguments.of(gradleVersion, agpVersion, KotlinNoSchemaLocationProjectSpec())
            arguments += Arguments.of(gradleVersion, agpVersion, JavaNoSchemaLocationProjectSpec())
        }
        return arguments.stream()
    }
}
