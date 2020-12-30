package io.github.simonschiller.prefiller.testutil.spec

open class NonAndroidProjectSpec : BaseProjectSpec() {

    override fun getRootBuildGradleContent(agpVersion: String) = """
        buildscript {
            repositories {
                mavenLocal()
                google()
		        mavenCentral()
		        jcenter()
	        }
	        dependencies {
                classpath("io.github.simonschiller:prefiller:+")
	        }
        }
            
    """.trimIndent()

    override fun getModuleBuildGradleContent() = """
        apply plugin: "io.github.simonschiller.prefiller"
            
        repositories {
            google()
            mavenCentral()
            jcenter()
        }
            
    """.trimIndent()

    override fun getEntityClassName() = "Person.java"

    override fun getEntityClassContent() = """
        package com.test;

        public class Person {
            public String name;
            public int age;
        }
            
    """.trimIndent()

    override fun getDatabaseClassName() = "PeopleDatabase.java"

    override fun getDatabaseClassContent() = """
        package com.test;

        public abstract class PeopleDatabase {}
            
    """.trimIndent()

    override fun toString() = "Non-Android project"
}
