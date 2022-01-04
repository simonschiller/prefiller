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

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("io.github.simonschiller.prefiller")
}

android {
    compileSdkVersion(Versions.COMPILE_SDK)

    defaultConfig {
        minSdkVersion(Versions.MIN_SDK)
        targetSdkVersion(Versions.TARGET_SDK)

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }
    testOptions {
        unitTests.apply {
            isIncludeAndroidResources = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(Dependencies.KOTLIN_STDLIB)
    implementation(Dependencies.APPCOMPAT)
    implementation(Dependencies.ROOM_RUNTIME)
    kapt(Dependencies.ROOM_COMPILER)

    testImplementation(Dependencies.JUNIT_4)
    testImplementation(Dependencies.ANDROIDX_TEST)
    testImplementation(Dependencies.ROBOLECTRIC)
}

prefiller {
    database("customers") {
        classname.set("io.github.simonschiller.prefiller.sample.customer.CustomerDatabase")
        script.set(layout.projectDirectory.file("../sql/customers.sql"))
    }
    database("products") {
        classname.set("io.github.simonschiller.prefiller.sample.product.ProductDatabase")
        script.set(layout.projectDirectory.file("../sql/products.sql"))
    }
}
