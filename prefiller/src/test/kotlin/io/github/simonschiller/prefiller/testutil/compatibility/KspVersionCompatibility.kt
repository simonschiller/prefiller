package io.github.simonschiller.prefiller.testutil.compatibility

import io.github.simonschiller.prefiller.internal.util.Version
import io.github.simonschiller.prefiller.testutil.compatibility.KotlinVersionCompatibility.KOTLIN_1_6_21
import io.github.simonschiller.prefiller.testutil.compatibility.KotlinVersionCompatibility.KOTLIN_1_7_0
import io.github.simonschiller.prefiller.testutil.compatibility.KotlinVersionCompatibility.KOTLIN_1_7_10
import io.github.simonschiller.prefiller.testutil.compatibility.KotlinVersionCompatibility.KOTLIN_1_7_20
import io.github.simonschiller.prefiller.testutil.compatibility.KotlinVersionCompatibility.KOTLIN_1_7_21
import io.github.simonschiller.prefiller.testutil.compatibility.KotlinVersionCompatibility.KOTLIN_1_7_22
import io.github.simonschiller.prefiller.testutil.compatibility.KotlinVersionCompatibility.KOTLIN_1_8_0
import io.github.simonschiller.prefiller.testutil.compatibility.KotlinVersionCompatibility.KOTLIN_1_8_10
import io.github.simonschiller.prefiller.testutil.compatibility.KotlinVersionCompatibility.KOTLIN_1_8_20
import io.github.simonschiller.prefiller.testutil.compatibility.KotlinVersionCompatibility.KOTLIN_1_8_21
import io.github.simonschiller.prefiller.testutil.compatibility.KotlinVersionCompatibility.KOTLIN_1_8_22
import io.github.simonschiller.prefiller.testutil.compatibility.KotlinVersionCompatibility.KOTLIN_1_9_0
import io.github.simonschiller.prefiller.testutil.compatibility.KotlinVersionCompatibility.KOTLIN_1_9_10
import io.github.simonschiller.prefiller.testutil.compatibility.KotlinVersionCompatibility.KOTLIN_1_9_20

internal object KspVersionCompatibility {
    fun getKotlinKspVersion(
        kotlinVersion: Version,
    ): Version? = when {
        kotlinVersion >= KOTLIN_1_9_20 -> Version(1, 9, 20, "1.0.14")
        kotlinVersion >= KOTLIN_1_9_10 -> Version(1, 9, 10, "1.0.13")
        kotlinVersion >= KOTLIN_1_9_0 -> Version(1, 9, 0, "1.0.13")
        kotlinVersion >= KOTLIN_1_8_22 -> Version(1, 8, 22, "1.0.11")
        kotlinVersion >= KOTLIN_1_8_21 -> Version(1, 8, 21, "1.0.11")
        kotlinVersion >= KOTLIN_1_8_20 -> Version(1, 8, 20, "1.0.11")
        kotlinVersion >= KOTLIN_1_8_10 -> Version(1, 8, 10, "1.0.9")
        kotlinVersion >= KOTLIN_1_8_0 -> Version(1, 8, 0, "1.0.9")
        kotlinVersion >= KOTLIN_1_7_22 -> Version(1, 7, 22, "1.0.8")
        kotlinVersion >= KOTLIN_1_7_21 -> Version(1, 7, 21, "1.0.8")
        kotlinVersion >= KOTLIN_1_7_20 -> Version(1, 7, 20, "1.0.8")
        kotlinVersion >= KOTLIN_1_7_10 -> Version(1, 7, 10, "1.0.6")
        kotlinVersion >= KOTLIN_1_7_0 -> Version(1, 7, 0, "1.0.6")
        kotlinVersion >= KOTLIN_1_6_21 -> Version(1, 6, 21, "1.0.6")
        else -> null
    }
}
