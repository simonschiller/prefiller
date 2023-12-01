package io.github.simonschiller.prefiller.testutil.spec

import Versions
import io.github.simonschiller.prefiller.testutil.compatibility.GradleVersionCompatibility.GRADLE_8_3
import org.gradle.api.JavaVersion

data class VersionCatalog(
    val gradleVersion: String = GRADLE_8_3.toString(),
    val kotlinVersion: String = Versions.KOTLIN,
    val compatibilityJavaVersion: JavaVersion = JavaVersion.VERSION_11,
    val agpVersion: String = Versions.AGP,
    val compileSdk: String = Versions.COMPILE_SDK.toString(),
    val minSdk: String = Versions.MIN_SDK.toString(),
    val targetSdk: String = Versions.TARGET_SDK.toString(),
    val kspVersion: String = Versions.KSP,
    val roomCompilerVersion: String = Versions.ROOM,
    val roomRuntimeVersion: String = Versions.ROOM,
    val androidxCoreRuntimeVersion: String = Versions.CORE_RUNTIME,
) {
    val androidxCoreRuntime get() = "androidx.arch.core:core-runtime:$androidxCoreRuntimeVersion"
    val kotlinGradlePlugin get() = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    val kotlinStdlib get() = "org.jetbrains.kotlin:kotlin-stdlib:${kotlinVersion}"
    val kspGradlePlugin get() = "com.google.devtools.ksp:symbol-processing-gradle-plugin:$kspVersion"
    val roomCompiler get() = "androidx.room:room-compiler:$roomCompilerVersion"
    val roomRuntime get() = "androidx.room:room-runtime:$roomRuntimeVersion"

    override fun toString(): String {
        return "{ Gradle $gradleVersion, " +
                "AGP $agpVersion, " +
                "Room $roomCompilerVersion, " +
                "Kotlin $kotlinVersion, " +
                "KSP $kspVersion, " +
                "SDK $minSdk/$compileSdk/$targetSdk " +
                "}"
    }
}
