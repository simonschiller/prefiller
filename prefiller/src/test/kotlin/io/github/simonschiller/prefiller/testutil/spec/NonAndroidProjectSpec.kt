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

open class NonAndroidProjectSpec : BaseProjectSpec() {

    override fun getRootBuildGradleContent(agpVersion: String) = """
        buildscript {
            repositories {
                mavenLocal()
                google()
		        mavenCentral()
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
