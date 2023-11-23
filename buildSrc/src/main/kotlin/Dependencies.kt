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

object Versions {
    const val COMPILE_SDK = 32
    const val MIN_SDK = 21
    const val TARGET_SDK = 32

    const val AGP = "7.2.2" // https://mvnrepository.com/artifact/com.android.tools.build/gradle?repo=google
    const val KOTLIN = "1.6.21" // https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-stdlib
    const val KSP = "1.6.21-1.0.6" // https://mvnrepository.com/artifact/com.google.devtools.ksp/com.google.devtools.ksp.gradle.plugin
    const val PUBLISH_GRADLE_PLUGIN = "0.21.0" // https://mvnrepository.com/artifact/com.gradle.publish/plugin-publish-plugin?repo=gradle-plugins

    const val SDK_COMMON = "30.2.2" // https://mvnrepository.com/artifact/com.android.tools/sdk-common?repo=google
    const val ANTLR = "4.10.1" // https://mvnrepository.com/artifact/org.antlr/antlr4
    const val SQLITE = "3.39.2.0" // https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc
    const val JUNIT_5 = "5.9.0" // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-engine
    const val TRUTH = "1.1.3" // https://mvnrepository.com/artifact/com.google.truth/truth

    const val APPCOMPAT = "1.4.2" // https://mvnrepository.com/artifact/androidx.appcompat/appcompat
    const val CORE_RUNTIME = "2.2.0" // https://maven.google.com/web/index.html#androidx.arch.core:core-runtime
    const val ROOM = "2.4.3" // https://mvnrepository.com/artifact/androidx.room/room-runtime
    const val JUNIT_4 = "4.13.2" // https://mvnrepository.com/artifact/junit/junit
    const val ROBOLECTRIC = "4.8.1" // https://mvnrepository.com/artifact/org.robolectric/robolectric

    // Alpha version required to be compatible with Android 12
    const val ANDROIDX_TEST = "1.1.4-alpha07" // https://mvnrepository.com/artifact/androidx.test.ext/junit
}

object Dependencies {
    const val AGP = "com.android.tools.build:gradle:${Versions.AGP}"
    const val KOTLIN_GRADLE_PLUGIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"
    const val KSP_GRADLE_PLUGIN = "com.google.devtools.ksp:symbol-processing-gradle-plugin:${Versions.KSP}"
    const val PUBLISH_GRADLE_PLUGIN = "com.gradle.publish:plugin-publish-plugin:${Versions.PUBLISH_GRADLE_PLUGIN}"

    const val KOTLIN_STDLIB = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN}"
    const val SDK_COMMON = "com.android.tools:common:${Versions.SDK_COMMON}"
    const val ANTLR = "org.antlr:antlr4:${Versions.ANTLR}"
    const val SQLITE = "org.xerial:sqlite-jdbc:${Versions.SQLITE}"
    const val JUNIT_5_ENGINE = "org.junit.jupiter:junit-jupiter-engine:${Versions.JUNIT_5}"
    const val JUNIT_5_API = "org.junit.jupiter:junit-jupiter-api:${Versions.JUNIT_5}"
    const val JUNIT_5_PARAMS = "org.junit.jupiter:junit-jupiter-params:${Versions.JUNIT_5}"
    const val TRUTH = "com.google.truth:truth:${Versions.TRUTH}"

    const val APPCOMPAT = "androidx.appcompat:appcompat:${Versions.APPCOMPAT}"
    const val ROOM_RUNTIME = "androidx.room:room-runtime:${Versions.ROOM}"
    const val ROOM_COMPILER = "androidx.room:room-compiler:${Versions.ROOM}"
    const val JUNIT_4 = "junit:junit:${Versions.JUNIT_4}"
    const val ANDROIDX_TEST = "androidx.test.ext:junit:${Versions.ANDROIDX_TEST}"
    const val ROBOLECTRIC = "org.robolectric:robolectric:${Versions.ROBOLECTRIC}"
}
