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

import java.io.File

open class DynamicFeatureProjectSpec : BaseProjectSpec() {

    override fun getRootBuildGradleContent(agpVersion: String) = """
        buildscript {
            repositories {
                mavenLocal()
                google()
		        mavenCentral()
	        }
	        dependencies {
		        classpath("com.android.tools.build:gradle:$agpVersion")
                classpath("io.github.simonschiller:prefiller:+")
	        }
        }
            
    """.trimIndent()

    override fun getModuleBuildGradleContent() = """
        apply plugin: "com.android.dynamic-feature"
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
            
                javaCompileOptions {
                    annotationProcessorOptions {
                        arguments["room.schemaLocation"] = projectDir.absolutePath + "/schemas"
                    }
                }
            }
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }
        }    
        dependencies {
            implementation(project(":app"))
            implementation("${Dependencies.ROOM_RUNTIME}")
            annotationProcessor("${Dependencies.ROOM_COMPILER}")
        }    
        prefiller {
            database("people") {
                classname.set("com.test.PeopleDatabase")
                scripts.from(file("setup.sql"))
            }
        }
            
    """.trimIndent()

    override fun getEntityClassName() = "Person.java"

    override fun getEntityClassContent() = """
        package com.test;

        import androidx.annotation.NonNull;
        import androidx.room.*;

        @Entity(tableName = "people")
        public class Person {
            @PrimaryKey
            @NonNull
            public String name;
            public int age;
        }
            
    """.trimIndent()

    override fun getDatabaseClassName() = "PeopleDatabase.java"

    override fun getDatabaseClassContent() = """
        package com.test;

        import androidx.room.*;

        @Database(entities = {Person.class}, version = 1)
        public abstract class PeopleDatabase extends RoomDatabase {}
            
    """.trimIndent()

    override fun getSettingsGradleContent() = """
        include(':module')
        include(':app')
    """.trimIndent()

    override fun generateAdditionalFiles(rootDir: File) {
        val appModuleDir = rootDir.resolve("app").also { it.mkdirs() }
        val appMainDir = appModuleDir.resolve("src/main").also { it.mkdirs() }
        
        val appModuleBuildGradle = appModuleDir.resolve("build.gradle")
        appModuleBuildGradle.writeText(getAppModuleBuildGradleContent())

        val appAndroidManifest = appMainDir.resolve("AndroidManifest.xml")
        appAndroidManifest.writeText(getAppAndroidManifestContent())
    }

    private fun getAppModuleBuildGradleContent() = """
        apply plugin: "com.android.application"

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
            dynamicFeatures = [":module"]
        }
            
    """.trimIndent()

    private fun getAppAndroidManifestContent() = """
        <manifest package="com.test" />
    """.trimIndent()

    override fun toString() = "Dynamic feature module project"
}
