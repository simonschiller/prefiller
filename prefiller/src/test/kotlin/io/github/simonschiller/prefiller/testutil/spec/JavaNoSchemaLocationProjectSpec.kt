package io.github.simonschiller.prefiller.testutil.spec

open class JavaNoSchemaLocationProjectSpec : JavaProjectSpec() {

    override fun getModuleBuildGradleContent() = """
        apply plugin: "com.android.application"
        apply plugin: "io.github.simonschiller.prefiller"
            
        repositories {
            google()
            mavenCentral()
            jcenter()
        }
        android {
            compileSdkVersion(${Versions.COMPILE_SDK})
        	defaultConfig {
            	minSdkVersion(${Versions.MIN_SDK})
            	targetSdkVersion(${Versions.TARGET_SDK})
            }
        }    
        dependencies {
            implementation("${Dependencies.ROOM_RUNTIME}")
            annotationProcessor("${Dependencies.ROOM_COMPILER}")
        }    
        prefiller {
            database("people") {
                classname = "com.test.PeopleDatabase"
                script = file(projectDir.absolutePath + "/setup.sql")
            }
        }
            
    """.trimIndent()

    override fun toString() = "Java project without schema location configured"
}
