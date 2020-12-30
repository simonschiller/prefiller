import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    antlr
    `java-gradle-plugin`
    `maven-publish`
    id("com.gradle.plugin-publish") version "0.12.0"
}

group = "io.github.simonschiller"
version = "0.1.0" // Also update the version in the README

repositories {
    google()
    jcenter()
    mavenCentral()
}

dependencies {
    antlr(Dependencies.ANTLR)

    implementation(Dependencies.GSON)
    implementation(Dependencies.SQLITE)

    compileOnly(Dependencies.AGP)
    compileOnly(Dependencies.KOTLIN_GRADLE_PLUGIN)

    testRuntimeOnly(Dependencies.JUNIT_5_ENGINE)
    testImplementation(Dependencies.JUNIT_5_API)
    testImplementation(Dependencies.JUNIT_5_PARAMS)
}

sourceSets {
    test.configure {
        java.srcDirs("$rootDir/buildSrc/src/main/kotlin") // Make versions available in tests
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.ExperimentalStdlibApi"
    dependsOn(tasks.named("generateGrammarSource")) // Make sure the ANTLR grammar gets compiled
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()

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
