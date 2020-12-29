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
    antlr("org.antlr:antlr4:${project.properties["versions.antlr"]}")

    implementation("com.google.code.gson:gson:${project.properties["versions.gson"]}")
    implementation("org.xerial:sqlite-jdbc:${project.properties["versions.sqlite"]}")

    implementation("com.android.tools.build:gradle:${project.properties["versions.agp"]}")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${project.properties["versions.kotlin"]}")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${project.properties["versions.junit5"]}")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${project.properties["versions.junit5"]}")
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.ExperimentalStdlibApi"
    dependsOn(tasks.named("generateGrammarSource")) // Make sure the ANTLR grammar gets compiled
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
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
