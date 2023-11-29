package io.github.simonschiller.prefiller.testutil.compatibility

import io.github.simonschiller.prefiller.internal.util.Version
import io.github.simonschiller.prefiller.testutil.compatibility.GradleVersionCompatibility.GRADLE_6_1_1
import io.github.simonschiller.prefiller.testutil.compatibility.GradleVersionCompatibility.GRADLE_6_5
import io.github.simonschiller.prefiller.testutil.compatibility.GradleVersionCompatibility.GRADLE_6_7_1
import io.github.simonschiller.prefiller.testutil.compatibility.GradleVersionCompatibility.GRADLE_7_0
import io.github.simonschiller.prefiller.testutil.compatibility.GradleVersionCompatibility.GRADLE_7_2
import io.github.simonschiller.prefiller.testutil.compatibility.GradleVersionCompatibility.GRADLE_7_3_3
import io.github.simonschiller.prefiller.testutil.compatibility.GradleVersionCompatibility.GRADLE_7_4
import io.github.simonschiller.prefiller.testutil.compatibility.GradleVersionCompatibility.GRADLE_7_5
import io.github.simonschiller.prefiller.testutil.compatibility.GradleVersionCompatibility.GRADLE_8_0
import io.github.simonschiller.prefiller.testutil.compatibility.GradleVersionCompatibility.GRADLE_8_2
import io.github.simonschiller.prefiller.testutil.compatibility.GradleVersionCompatibility.GRADLE_8_3
import io.github.simonschiller.prefiller.testutil.spec.VersionCatalog
import org.gradle.api.JavaVersion

internal object AgpVersionCompatibility {
    val AGP_4_0_0 = Version(4, 0, 0)
    val AGP_4_1_0 = Version(4, 1, 0)
    val AGP_4_2_0 = Version(4, 2, 0)
    val AGP_4_2_2 = Version(4, 2, 2)
    val AGP_7_0_0 = Version(7, 0, 0)
    val AGP_7_0_2 = Version(7, 0, 2)
    val AGP_7_0_4 = Version(7, 0, 4)
    val AGP_7_1_0 = Version(7, 1, 0)
    val AGP_7_2_0 = Version(7, 2, 0)
    val AGP_7_3_0 = Version(7, 3, 0)
    val AGP_7_4_0 = Version(7, 4, 0)
    val AGP_8_0_0 = Version(8, 0, 0)
    val AGP_8_1_1 = Version(8, 1, 1)
    val AGP_8_2_0 = Version(8, 2, 0)
    val AGP_8_3_0 = Version(8, 3, 0)

    // Checks if a AGP version [agpVersion] can run on the current JVM
    fun agpIsCompatibleWithRuntime(agpVersion: Version): Boolean {
        val jvmVersion = Runtime.version().version()[0]
        return when {
            agpVersion >= AGP_8_3_0 -> jvmVersion >= 17
            agpVersion >= AGP_8_0_0 -> jvmVersion in 17..20
            agpVersion >= AGP_7_0_0 -> jvmVersion >= 11
            else -> jvmVersion in 8..11
        }
    }

    fun getCompatibleJavaVersion(agpVersion: Version): JavaVersion {
        return when {
            agpVersion >= AGP_7_0_0 -> JavaVersion.VERSION_11
            else -> JavaVersion.VERSION_1_8
        }
    }

    // Checks if a AGP version [agpVersion] is compatible with a [gradleVersion] version of Gradle
    // See https://developer.android.com/build/releases/past-releases
    fun agpIsCompatibleWithGradle(
        agpVersion: Version,
        gradleVersion: Version,
    ) = when {
        agpVersion >= AGP_8_3_0 -> gradleVersion >= GRADLE_8_3
        agpVersion >= AGP_8_2_0 -> gradleVersion >= GRADLE_8_2
        agpVersion >= AGP_8_0_0 -> gradleVersion >= GRADLE_8_0
        agpVersion >= AGP_7_4_0 -> gradleVersion >= GRADLE_7_5
        agpVersion >= AGP_7_3_0 -> gradleVersion >= GRADLE_7_4 && gradleVersion < GRADLE_8_0
        agpVersion >= AGP_7_2_0 -> gradleVersion >= GRADLE_7_3_3 && gradleVersion < GRADLE_8_0
        agpVersion >= AGP_7_1_0 -> gradleVersion >= GRADLE_7_2 && gradleVersion < GRADLE_8_0
        agpVersion >= AGP_7_0_0 -> gradleVersion >= GRADLE_7_0 && gradleVersion < GRADLE_8_0
        agpVersion >= AGP_4_2_0 -> gradleVersion >= GRADLE_6_7_1 && gradleVersion < GRADLE_8_0
        agpVersion >= AGP_4_1_0 -> gradleVersion >= GRADLE_6_5 && gradleVersion < GRADLE_8_0
        agpVersion >= AGP_4_0_0 -> gradleVersion >= GRADLE_6_1_1 && gradleVersion < GRADLE_7_0
        else -> false
    }

    // https://developer.android.com/build/releases/gradle-plugin#api-level-support
    fun getCompatibleAndroidApiLevel(
        agpVersion: Version,
    ): Int = when {
        agpVersion >= AGP_8_1_1 -> 34
        agpVersion >= AGP_7_2_0 -> 33
        else -> 32
    }

    // Checks if a AGP version is compatible KSP
    internal fun VersionCatalog.agpIsCompatibleWithKsp(): Boolean = Version.parse(agpVersion) >= AGP_4_1_0

    internal fun VersionCatalog.agpHasNamespaceSupport(): Boolean = Version.parse(agpVersion) >= AGP_7_0_0
}
