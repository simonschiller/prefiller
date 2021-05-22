package io.github.simonschiller.prefiller.testutil.spec

open class KotlinKspProjectSpec : KotlinProjectSpec() {

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
                classpath("${Dependencies.KSP_GRADLE_PLUGIN}")
                classpath("io.github.simonschiller:prefiller:+")
	        }
        }
            
    """.trimIndent()

    override fun getModuleBuildGradleContent() = """
        apply plugin: "com.android.application"
        apply plugin: "kotlin-android"
        apply plugin: "com.google.devtools.ksp"
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
            ksp {
                arg("room.schemaLocation", projectDir.absolutePath + "/schemas")
            }
        }    
        dependencies {
            implementation("${Dependencies.KOTLIN_STDLIB}")
            implementation("${Dependencies.ROOM_RUNTIME}")
            ksp("${Dependencies.ROOM_COMPILER}")
        }    
        prefiller {
            database("people") {
                classname.set("com.test.PeopleDatabase")
                script.set(layout.projectDirectory.file("setup.sql"))
            }
        }
            
    """.trimIndent()

    override fun toString() = "Kotlin project using KSP"
}
