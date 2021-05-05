package io.github.simonschiller.prefiller.testutil.spec

open class KotlinProjectSpec : BaseProjectSpec() {

    override fun getRootBuildGradleContent(agpVersion: String) = """
        buildscript {
            repositories {
                mavenLocal()
                google()
		        mavenCentral()
	        }
	        dependencies {
		        classpath("com.android.tools.build:gradle:$agpVersion")
                classpath("${Dependencies.KOTLIN_GRADLE_PLUGIN}")
                classpath("io.github.simonschiller:prefiller:+")
	        }
        }
            
    """.trimIndent()

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
            kapt {
                arguments {
                    arg("room.schemaLocation", projectDir.absolutePath + "/schemas")
                }
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

    override fun getEntityClassName() = "Person.kt"

    override fun getEntityClassContent() = """
        package com.test

        import androidx.room.*

        @Entity(tableName = "people")
        data class Person(
            @PrimaryKey val name: String,
            val age: Int
        )
            
    """.trimIndent()

    override fun getDatabaseClassName() = "PeopleDatabase.kt"

    override fun getDatabaseClassContent() = """
        package com.test

        import androidx.room.*

        @Database(entities = [Person::class], version = 1)
        abstract class PeopleDatabase : RoomDatabase()
            
    """.trimIndent()

    override fun toString() = "Normal Kotlin project"
}
