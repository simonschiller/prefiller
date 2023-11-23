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

import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("java-gradle-plugin")
    id("antlr")
    id("maven-publish")
    id("com.gradle.plugin-publish")
}

group = "io.github.simonschiller"
version = "1.4.0" // Also update the version in the README

repositories {
    google()
    mavenCentral()
}

dependencies {
    antlr(Dependencies.ANTLR)

    implementation(Dependencies.SQLITE)
    implementation(Dependencies.JSONP)

    compileOnly(Dependencies.AGP)
    compileOnly(Dependencies.SDK_COMMON)
    compileOnly(Dependencies.KOTLIN_GRADLE_PLUGIN)
    compileOnly(Dependencies.KSP_GRADLE_PLUGIN)

    testRuntimeOnly(Dependencies.JUNIT_5_ENGINE)
    testImplementation(Dependencies.JUNIT_5_API)
    testImplementation(Dependencies.JUNIT_5_PARAMS)
    testImplementation(Dependencies.TRUTH)
}

sourceSets {
    main {
        java.srcDir(tasks.named("generateGrammarSource").map { files() })
    }

    test {
        java.srcDirs(
            tasks.named("generateTestGrammarSource").map { files() },
            "$rootDir/buildSrc/src/main/kotlin", // Make versions available in tests
        )
    }
}

tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_11
        // https://docs.gradle.org/current/userguide/compatibility.html#kotlin
        apiVersion = KotlinVersion.KOTLIN_1_6
        languageVersion = KotlinVersion.KOTLIN_1_6
    }
}

tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = JavaVersion.VERSION_11.toString()
    targetCompatibility = JavaVersion.VERSION_11.toString()
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    dependsOn("publishToMavenLocal")

    jvmArgs("-XX:MaxMetaspaceSize=2g")

    javaLauncher = javaToolchains.launcherFor {
        languageVersion = providers.environmentVariable("TEST_JDK_VERSION")
            .map { JavaLanguageVersion.of(it.toInt())  }
            .orElse(JavaLanguageVersion.of(17))
    }

    testLogging {
        events("passed", "skipped", "failed")
        exceptionFormat = TestExceptionFormat.FULL
    }
}

tasks.withType<AntlrTask>().configureEach {
    arguments = arguments + listOf("-package", "io.github.simonschiller.prefiller.antlr", "-no-listener")
}

gradlePlugin {
    website.set("https://github.com/simonschiller/prefiller")
    vcsUrl.set("https://github.com/simonschiller/prefiller")
    plugins {
        create("prefiller") {
            id = "io.github.simonschiller.prefiller"
            implementationClass = "io.github.simonschiller.prefiller.PrefillerPlugin"
            displayName = "Prefiller"
            description = "Prefiller is a Gradle plugin that generates pre-filled Room databases at compile time."
            tags = listOf("android", "room")
        }
    }
}
