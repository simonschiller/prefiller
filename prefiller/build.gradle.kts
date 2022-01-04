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
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("java-gradle-plugin")
    id("antlr")
    id("maven-publish")
    id("com.gradle.plugin-publish") version "0.19.0"
}

group = "io.github.simonschiller"
version = "1.2.1" // Also update the version in the README

repositories {
    google()
    mavenCentral()
}

dependencies {
    antlr(Dependencies.ANTLR)

    implementation(Dependencies.SQLITE)

    compileOnly(Dependencies.AGP)
    compileOnly(Dependencies.KOTLIN_GRADLE_PLUGIN)
    compileOnly(Dependencies.KSP_GRADLE_PLUGIN)

    testRuntimeOnly(Dependencies.JUNIT_5_ENGINE)
    testImplementation(Dependencies.JUNIT_5_API)
    testImplementation(Dependencies.JUNIT_5_PARAMS)
    testImplementation(Dependencies.TRUTH)
}

sourceSets {
    test.configure {
        java.srcDirs("$rootDir/buildSrc/src/main/kotlin") // Make versions available in tests
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
    dependsOn(tasks.named("generateGrammarSource")) // Make sure the ANTLR grammar gets compiled
}

tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = JavaVersion.VERSION_1_8.toString()
    targetCompatibility = JavaVersion.VERSION_1_8.toString()
}

tasks.withType<Jar>().configureEach {
    dependsOn(tasks.named("generateGrammarSource")) // Make sure the ANTLR grammar gets compiled
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    dependsOn("publishToMavenLocal")

    jvmArgs("-XX:MaxMetaspaceSize=2g")

    testLogging {
        events("passed", "skipped", "failed")
        exceptionFormat = TestExceptionFormat.FULL
    }
}

tasks.withType<AntlrTask>().configureEach {
    arguments = arguments + listOf("-package", "io.github.simonschiller.prefiller.antlr", "-no-listener")
}

gradlePlugin {
    plugins {
        create("prefiller") {
            id = "io.github.simonschiller.prefiller"
            implementationClass = "io.github.simonschiller.prefiller.PrefillerPlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/simonschiller/prefiller"
    vcsUrl = "https://github.com/simonschiller/prefiller"
    description = "Prefiller is a Gradle plugin that generates pre-filled Room databases at compile time."
    tags = listOf("android", "room")

    mavenCoordinates {
        groupId = project.group.toString()
        artifactId = "prefiller"
    }

    (plugins) {
        "prefiller" {
            displayName = "Prefiller"
        }
    }
}
