package io.github.simonschiller.prefiller.testutil

import io.github.simonschiller.prefiller.internal.util.Version
import io.github.simonschiller.prefiller.testutil.spec.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.util.stream.Stream

open class TestVersions : ArgumentsProvider {

    // See https://gradle.org/releases
    private val gradleVersions = listOf(
        "7.3.3",
        "7.2",
        "7.1.1",
        "7.0.2",
        "6.9.2",
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
        "7.0.1",
        "4.2.2",
        "4.1.2",
        "4.0.2"
    )

    override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
        val arguments = mutableListOf<Arguments>()
        gradleVersions().forEach { gradleVersion ->
            agpVersions().forEach { agpVersion ->
                if (agpVersion.baseVersion() isCompatibleWith gradleVersion.baseVersion()) {
                    arguments += Arguments.of(gradleVersion.toString(), agpVersion.toString())
                }
            }
        }

        require(arguments.isNotEmpty()) {
            "Found no compatible AGP and Gradle version combination, check your supplied arguments."
        }
        return arguments.stream()
    }

    // Allow setting a single, fixed Gradle version via environment variables
    private fun gradleVersions(): List<Version> {
        val gradleVersion = System.getenv("GRADLE_VERSION")
        return if (gradleVersion == null) {
            gradleVersions.map(Version::parse)
        } else {
            listOf(Version.parse(gradleVersion))
        }
    }

    // Allow setting a single, fixed AGP version via environment variables
    private fun agpVersions(): List<Version> {
        val agpVersion = System.getenv("AGP_VERSION")
        return if (agpVersion == null) {
            agpVersions.map(Version::parse)
        } else {
            listOf(Version.parse(agpVersion))
        }
    }

    // Checks if a AGP version (receiver) is compatible KSP
    protected fun Version.isCompatibleWithKsp(): Boolean {
        return baseVersion() >= Version.parse("4.1.0")
    }

    // Checks if a AGP version (receiver) is compatible with a certain version of Gradle
    private infix fun Version.isCompatibleWith(gradleVersion: Version) = when {
        this >= Version.parse("7.0.0") -> gradleVersion >= Version.parse("7.0")
        this >= Version.parse("4.2.0") -> gradleVersion >= Version.parse("6.7.1")
        this >= Version.parse("4.1.0") -> gradleVersion >= Version.parse("6.5")
        this >= Version.parse("4.0.0") -> gradleVersion >= Version.parse("6.1.1") && gradleVersion < Version.parse("7.0")
        else -> false
    }
}

class LanguageTestVersions : ArgumentsProvider, TestVersions() {

    override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
        val arguments = mutableListOf<Arguments>()
        super.provideArguments(context).forEach { argument ->
            val (gradleVersion, agpVersion) = argument.get()
            arguments += Arguments.of(gradleVersion, agpVersion, JavaProjectSpec())
            arguments += Arguments.of(gradleVersion, agpVersion, KotlinKaptProjectSpec())
            if (Version.parse(agpVersion as String).isCompatibleWithKsp()) {
                arguments += Arguments.of(gradleVersion, agpVersion, KotlinKspProjectSpec())
            }
        }
        return arguments.stream()
    }
}

class NoSchemaLocationTestVersions : ArgumentsProvider, TestVersions() {

    override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
        val arguments = mutableListOf<Arguments>()
        super.provideArguments(context).forEach { argument ->
            val (gradleVersion, agpVersion) = argument.get()
            arguments += Arguments.of(gradleVersion, agpVersion, NoSchemaLocationJavaProjectSpec())
            arguments += Arguments.of(gradleVersion, agpVersion, NoSchemaLocationKotlinKaptProjectSpec())
            if (Version.parse(agpVersion as String).isCompatibleWithKsp()) {
                arguments += Arguments.of(gradleVersion, agpVersion, NoSchemaLocationKotlinKspProjectSpec())
            }
        }
        return arguments.stream()
    }
}
