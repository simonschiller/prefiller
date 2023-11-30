/*
 * Copyright 2020 Simon Schiller
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.simonschiller.prefiller.testutil

import io.github.simonschiller.prefiller.internal.util.Version
import io.github.simonschiller.prefiller.testutil.compatibility.AgpVersionCompatibility.agpIsCompatibleWithGradle
import io.github.simonschiller.prefiller.testutil.compatibility.AgpVersionCompatibility.agpIsCompatibleWithKsp
import io.github.simonschiller.prefiller.testutil.compatibility.AgpVersionCompatibility.agpIsCompatibleWithRuntime
import io.github.simonschiller.prefiller.testutil.compatibility.AgpVersionCompatibility.getCompatibleAndroidApiLevel
import io.github.simonschiller.prefiller.testutil.compatibility.AgpVersionCompatibility.getCompatibleJavaVersion
import io.github.simonschiller.prefiller.testutil.compatibility.GradleVersionCompatibility.gradleIsCompatibleWithRuntime
import io.github.simonschiller.prefiller.testutil.compatibility.KotlinVersionCompatibility.getKotlinCompatibleVersion
import io.github.simonschiller.prefiller.testutil.compatibility.KotlinVersionCompatibility.hasCompatibleKotlinVersion
import io.github.simonschiller.prefiller.testutil.compatibility.KspVersionCompatibility.getKotlinKspVersion
import io.github.simonschiller.prefiller.testutil.compatibility.RoomVersionCompatibility.getCompatibleAndroidxCoreRuntimeVersion
import io.github.simonschiller.prefiller.testutil.compatibility.RoomVersionCompatibility.getCompatibleRoomVersion
import io.github.simonschiller.prefiller.testutil.spec.JavaProjectSpec
import io.github.simonschiller.prefiller.testutil.spec.KotlinKaptProjectSpec
import io.github.simonschiller.prefiller.testutil.spec.KotlinKspProjectSpec
import io.github.simonschiller.prefiller.testutil.spec.NoSchemaLocationJavaProjectSpec
import io.github.simonschiller.prefiller.testutil.spec.NoSchemaLocationKotlinKaptProjectSpec
import io.github.simonschiller.prefiller.testutil.spec.NoSchemaLocationKotlinKspProjectSpec
import io.github.simonschiller.prefiller.testutil.spec.VersionCatalog
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.stream.Stream

object TestVersions {
    private val logger: Logger = LoggerFactory.getLogger(TestVersions::class.java)

    // See https://gradle.org/releases
    private val gradleVersions = listOf(
        "8.5",
        "8.4",
        "8.3",
        "8.2.1",
        "8.1.1",
        "8.0.2",
        "7.6.3",
        "7.5.1",
        "7.4.2",
        "7.3.3",
        "7.2",
        "7.1.1",
        "7.0.2",
        "6.9.4",
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
        "8.2.0",
        "8.1.4",
        "8.0.2",
        "7.4.2",
        "7.3.1",
        "7.2.2",
        "7.1.3",
        "7.0.4",
        "4.2.2",
        "4.1.2",
        "4.0.2"
    )

    fun getTestVariants(): List<VersionCatalog> = getCompatibleGradleAgpVariants()
        .map { (gradleVersion, agpVersion) ->
            val kotlinVersion = getKotlinCompatibleVersion(agpVersion, gradleVersion)
                ?: error("No Kotlin for `$gradleVersion` - `$agpVersion`")
            val kspVersion = getKotlinKspVersion(kotlinVersion)
                ?: error("No Kotlin KSP for Kotlin `$kotlinVersion`")
            val compileTargetSdk = getCompatibleAndroidApiLevel(agpVersion)
            val room = getCompatibleRoomVersion(compileTargetSdk).toString()
            VersionCatalog(
                gradleVersion = gradleVersion.toString(),
                agpVersion = agpVersion.toString(),
                compileSdk = compileTargetSdk.toString(),
                targetSdk = compileTargetSdk.toString(),
                kotlinVersion = kotlinVersion.toString(),
                kspVersion = kspVersion.toString(),
                compatibilityJavaVersion = getCompatibleJavaVersion(agpVersion),
                roomCompilerVersion = room,
                roomRuntimeVersion = room,
                androidxCoreRuntimeVersion = getCompatibleAndroidxCoreRuntimeVersion(compileTargetSdk).toString()
            )
        }
        .toList()
        .also {
            require(it.isNotEmpty()) {
                "Found no compatible AGP and Gradle version combination, check your supplied arguments."
            }
        }

    private fun getCompatibleGradleAgpVariants(): Sequence<Pair<Version, Version>> {
        val (gradleCompatibleVersions, gradleIncompatibleVersions) = gradleVersions().partition {
            gradleIsCompatibleWithRuntime(it.baseVersion())
        }

        if (gradleIncompatibleVersions.isNotEmpty()) {
            logger.warn(
                "Gradle versions {} cannot be run on the current JVM `{}`",
                gradleIncompatibleVersions.joinToString(),
                Runtime.version(),
            )
        }

        val (agpCompatibleVersions, agpIncompatibleVersions) = agpVersions().partition {
            agpIsCompatibleWithRuntime(it)
        }

        if (agpIncompatibleVersions.isNotEmpty()) {
            logger.warn(
                "Android Gradle Plugin versions {} cannot be run on the current JVM `{}`",
                agpIncompatibleVersions.joinToString(),
                Runtime.version(),
            )
        }

        return sequence {
            gradleCompatibleVersions.forEach { gradleVersion ->
                agpCompatibleVersions.forEach { agpVersion ->
                    yield(gradleVersion to agpVersion)
                }
            }
        }.filter { (gradleVersion, agpVersion) ->
            agpIsCompatibleWithGradle(agpVersion, gradleVersion) &&
                    hasCompatibleKotlinVersion(agpVersion, gradleVersion)
        }
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
}

class TestVariants : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<Arguments> = TestVersions.getTestVariants()
        .map { Arguments.of(it) }
        .stream()
}

class LanguageTestVersions : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
        return TestVersions.getTestVariants()
            .flatMap { versions ->
                getTestProjectSpecs(versions).map { Arguments.of(it) }
            }
            .stream()
    }

    private fun getTestProjectSpecs(versionCatalog: VersionCatalog) = buildList {
        add(JavaProjectSpec(versionCatalog))
        add(KotlinKaptProjectSpec(versionCatalog))
        if (versionCatalog.agpIsCompatibleWithKsp()) {
            add(KotlinKspProjectSpec(versionCatalog))
        }
    }
}

class NoSchemaLocationTestVersions : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
        return TestVersions.getTestVariants()
            .flatMap { versions ->
                getTestProjectSpecs(versions).map { Arguments.of(it) }
            }
            .stream()
    }

    private fun getTestProjectSpecs(versionCatalog: VersionCatalog) = buildList {
        add(NoSchemaLocationJavaProjectSpec(versionCatalog))
        add(NoSchemaLocationKotlinKaptProjectSpec(versionCatalog))
        if (versionCatalog.agpIsCompatibleWithKsp()) {
            add(NoSchemaLocationKotlinKspProjectSpec(versionCatalog))
        }
    }
}
