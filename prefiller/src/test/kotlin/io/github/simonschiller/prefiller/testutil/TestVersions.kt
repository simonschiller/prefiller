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
        "6.8.1",
        "6.7.1",
        "6.6.1",
        "6.5.1",
        "6.4.1",
        "6.3",
        "6.2.2",
        "6.1.1"
    )

    // See https://developer.android.com/studio/releases/gradle-plugin, with minimum Gradle version
    private val agpVersions = listOf(
        "4.1.2" to "6.5.1",
        "4.0.1" to "6.1.1"
    )

    protected val versions = agpVersions.flatMap { (agpVersion, minGradleVersion) ->
        val minVersion = VersionNumber.parse(minGradleVersion)
        gradleVersions
            .filter { gradleVersion -> VersionNumber.parse(gradleVersion) >= minVersion }
            .map { gradleVersion -> gradleVersion to agpVersion }
    }

    override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
        val arguments = Stream.builder<Arguments>()
        versions.forEach { (gradleVersion, agpVersion) ->
            arguments.add(Arguments.of(gradleVersion, agpVersion))
        }
        return arguments.build()
    }
}

class LanguageTestVersions : ArgumentsProvider, TestVersions() {

    override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
        val arguments = Stream.builder<Arguments>()
        versions.forEach { (gradleVersion, agpVersion) ->
            arguments.add(Arguments.of(gradleVersion, agpVersion, KotlinProjectSpec()))
            arguments.add(Arguments.of(gradleVersion, agpVersion, JavaProjectSpec()))
        }
        return arguments.build()
    }
}

class NoSchemaLocationTestVersions : ArgumentsProvider, TestVersions() {

    override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
        val arguments = Stream.builder<Arguments>()
        versions.forEach { (gradleVersion, agpVersion) ->
            arguments.add(Arguments.of(gradleVersion, agpVersion, KotlinNoSchemaLocationProjectSpec()))
            arguments.add(Arguments.of(gradleVersion, agpVersion, JavaNoSchemaLocationProjectSpec()))
        }
        return arguments.build()
    }
}
