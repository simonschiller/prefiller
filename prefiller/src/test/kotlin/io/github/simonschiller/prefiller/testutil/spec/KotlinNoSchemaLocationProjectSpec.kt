package io.github.simonschiller.prefiller.testutil.spec

open class KotlinNoSchemaLocationProjectSpec : KotlinProjectSpec() {

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
        }    
        dependencies {
            implementation("${Dependencies.KOTLIN_STDLIB}")
            implementation("${Dependencies.ROOM_RUNTIME}")
            kapt("${Dependencies.ROOM_RUNTIME}")
        }    
        prefiller {
            database("people") {
                classname.set("com.test.PeopleDatabase")
                script.set(layout.projectDirectory.file("setup.sql"))
            }
        }
            
    """.trimIndent()

    override fun toString() = "Kotlin project without schema location configured"
}
