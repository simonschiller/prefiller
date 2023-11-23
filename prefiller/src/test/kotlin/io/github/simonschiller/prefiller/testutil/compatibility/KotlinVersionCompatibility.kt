package io.github.simonschiller.prefiller.testutil.compatibility

import io.github.simonschiller.prefiller.internal.util.Version
import io.github.simonschiller.prefiller.testutil.compatibility.AgpVersionCompatibility.AGP_4_2_2
import io.github.simonschiller.prefiller.testutil.compatibility.AgpVersionCompatibility.AGP_7_0_2
import io.github.simonschiller.prefiller.testutil.compatibility.AgpVersionCompatibility.AGP_7_0_4
import io.github.simonschiller.prefiller.testutil.compatibility.GradleVersionCompatibility.GRADLE_6_1_1
import io.github.simonschiller.prefiller.testutil.compatibility.GradleVersionCompatibility.GRADLE_6_7_1
import io.github.simonschiller.prefiller.testutil.compatibility.GradleVersionCompatibility.GRADLE_6_8_3

internal object KotlinVersionCompatibility {
    val KOTLIN_1_6_21 = Version(1, 6, 21)
    val KOTLIN_1_7_0 = Version(1, 7, 0)
    val KOTLIN_1_7_10 = Version(1, 7, 10)
    val KOTLIN_1_7_20 = Version(1, 7, 20)
    val KOTLIN_1_7_21 = Version(1, 7, 21)
    val KOTLIN_1_7_22 = Version(1, 7, 22)
    val KOTLIN_1_8_0 = Version(1, 8, 0)
    val KOTLIN_1_8_10 = Version(1, 8, 10)
    val KOTLIN_1_8_20 = Version(1, 8, 20)
    val KOTLIN_1_8_21 = Version(1, 8, 21)
    val KOTLIN_1_8_22 = Version(1, 8, 22)
    val KOTLIN_1_9_0 = Version(1, 9, 0)
    val KOTLIN_1_9_10 = Version(1, 9, 10)
    val KOTLIN_1_9_20 = Version(1, 9, 20)

    fun hasCompatibleKotlinVersion(
        agpVersion: Version,
        gradleVersion: Version,
    ): Boolean = getKotlinCompatibleVersion(agpVersion, gradleVersion) != null

    // https://kotlinlang.org/docs/gradle-configure-project.html#apply-the-plugin
    fun getKotlinCompatibleVersion(
        agpVersion: Version,
        gradleVersion: Version,
    ): Version? = when {
        agpVersion < AGP_4_2_2 -> {
            KOTLIN_1_6_21
        }

        gradleVersion >= GRADLE_6_8_3 -> {
            KOTLIN_1_9_20
        }

        gradleVersion >= GRADLE_6_7_1 -> {
            when {
                agpVersion <= AGP_7_0_4 -> KOTLIN_1_7_22
                else -> null
            }
        }

        gradleVersion >= GRADLE_6_1_1 -> {
            when {
                agpVersion <= AGP_7_0_2 -> KOTLIN_1_7_10
                else -> null
            }
        }

        else -> null
    }
}
