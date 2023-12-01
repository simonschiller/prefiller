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

import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.google.devtools.ksp")
    id("io.github.simonschiller.prefiller")
}

android {
    compileSdk = Versions.COMPILE_SDK
    namespace = "io.github.simonschiller.prefiller.sample"

    defaultConfig {
        minSdk = Versions.MIN_SDK

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }
    testOptions {
        unitTests.apply {
            isIncludeAndroidResources = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(Dependencies.KOTLIN_STDLIB)
    implementation(Dependencies.APPCOMPAT)
    implementation(Dependencies.ROOM_RUNTIME)
    ksp(Dependencies.ROOM_COMPILER)

    testImplementation(Dependencies.JUNIT_4)
    testImplementation(Dependencies.TRUTH)
    testImplementation(Dependencies.ANDROIDX_TEST)
    testImplementation(Dependencies.ROBOLECTRIC)
}

tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_11
    }
}

prefiller {
    database("customers") {
        classname.set("io.github.simonschiller.prefiller.sample.customer.CustomerDatabase")
        scripts.from(file("../sql/customers.sql"))
    }
    database("products") {
        classname.set("io.github.simonschiller.prefiller.sample.product.ProductDatabase")
        scripts.from(file("../sql/orders.sql"), file("../sql/products.sql"))
    }
}
