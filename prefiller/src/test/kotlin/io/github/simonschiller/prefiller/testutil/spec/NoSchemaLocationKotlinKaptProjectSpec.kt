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

package io.github.simonschiller.prefiller.testutil.spec

open class NoSchemaLocationKotlinKaptProjectSpec : KotlinKaptProjectSpec() {

    override fun getModuleBuildGradleContent() = """
        apply plugin: "com.android.application"
        apply plugin: "kotlin-android"
        apply plugin: "kotlin-kapt"
        apply plugin: "io.github.simonschiller.prefiller"
            
        repositories {
            google()
            mavenCentral()
        }
        android {
            compileSdkVersion(${Versions.COMPILE_SDK})
        	defaultConfig {
            	minSdkVersion(${Versions.MIN_SDK})
            	targetSdkVersion(${Versions.TARGET_SDK})
            }
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }
        }    
        dependencies {
            implementation("${Dependencies.KOTLIN_STDLIB}")
            implementation("${Dependencies.ROOM_RUNTIME}")
            kapt("${Dependencies.ROOM_COMPILER}")
        }    
        prefiller {
            database("people") {
                classname.set("com.test.PeopleDatabase")
                script.set(layout.projectDirectory.file("setup.sql"))
            }
        }
            
    """.trimIndent()

    override fun toString() = "Kotlin project using KAPT without schema location configured"
}
