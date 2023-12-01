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

import io.github.simonschiller.prefiller.internal.util.Version
import io.github.simonschiller.prefiller.testutil.compatibility.AgpVersionCompatibility.AGP_7_0_0

abstract class KotlinProjectSpec(
    versionCatalog: VersionCatalog,
) : BaseProjectSpec(versionCatalog) {

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

    protected fun getKotlinTaskSetupContent(): String =
        if (Version.parse(versionCatalog.agpVersion) >= AGP_7_0_0) {
            """
        tasks
            .withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask.class)
            .configureEach {
                kotlinOptions {
                    jvmTarget = JavaVersion.${versionCatalog.compatibilityJavaVersion.name}.toString()
                }
            }

        """.trimIndent()
        } else {
            ""
        }
}
