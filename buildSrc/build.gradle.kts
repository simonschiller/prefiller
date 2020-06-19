import java.util.Properties
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Read gradle.properties of the root project to make versions available
val properties = Properties()
rootDir.parentFile.resolve("gradle.properties").reader().use { reader ->
    properties.load(reader)
}

plugins {
    `kotlin-dsl`
    antlr
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "0.12.0"
}

group = "io.github.simonschiller"
version = "0.1.0"

repositories {
    google()
    jcenter()
    mavenCentral()
}

dependencies {
    antlr("org.antlr:antlr4:${properties["versions.antlr"]}")

    implementation("com.google.code.gson:gson:${properties["versions.gson"]}")
    implementation("org.xerial:sqlite-jdbc:${properties["versions.sqlite"]}")

    implementation("com.android.tools.build:gradle:${properties["versions.agp"]}")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${properties["versions.kotlin"]}")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${properties["versions.junit5"]}")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${properties["versions.junit5"]}")
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
