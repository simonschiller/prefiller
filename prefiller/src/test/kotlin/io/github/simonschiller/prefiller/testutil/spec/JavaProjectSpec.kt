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

open class JavaProjectSpec(
    versionCatalog: VersionCatalog,
) : BaseProjectSpec(versionCatalog) {

    override fun getRootBuildGradleContent() = """
        buildscript {
            repositories {
                mavenLocal()
                google()
		        mavenCentral()
	        }
	        dependencies {
		        classpath("com.android.tools.build:gradle:${versionCatalog.agpVersion}")
                classpath("io.github.simonschiller:prefiller:+")
	        }
        }
            
    """.trimIndent()

    override fun getModuleBuildGradleContent() = """
        apply plugin: "com.android.application"
        apply plugin: "io.github.simonschiller.prefiller"
            
        repositories {
            google()
            mavenCentral()
        }
        android {
            compileSdkVersion(${versionCatalog.compileSdk})
            ${getNamespaceContent()}
        	defaultConfig {
            	minSdkVersion(${versionCatalog.minSdk})
            	targetSdkVersion(${versionCatalog.targetSdk})

                javaCompileOptions {
                    annotationProcessorOptions {
                        arguments["room.schemaLocation"] = projectDir.absolutePath + "/schemas"
                    }
                }
            }
            compileOptions {
                sourceCompatibility = JavaVersion.${versionCatalog.compatibilityJavaVersion.name}
                targetCompatibility = JavaVersion.${versionCatalog.compatibilityJavaVersion.name}
            }
        }    
        dependencies {
            implementation("${versionCatalog.androidxCoreRuntime}")
            implementation("${versionCatalog.roomRuntime}")
            annotationProcessor("${versionCatalog.roomCompiler}")
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

    override fun toString() = "Normal Java project ($versionCatalog)"
}
