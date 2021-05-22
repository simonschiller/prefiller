package io.github.simonschiller.prefiller.testutil.spec

abstract class KotlinProjectSpec : BaseProjectSpec() {

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
}
