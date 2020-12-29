package io.github.simonschiller.prefiller.internal

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.sql.DriverManager
import java.sql.PreparedStatement

class DatabasePopulatorTest {
    private val populator = DatabasePopulator()

    @TempDir
    lateinit var tempDir: File

    @Test
    fun `New database gets populated correctly`() {
        val database = tempDir.resolve("test.db")

        val statements = listOf(
            "CREATE TABLE numbers(value INTEGER PRIMARY KEY)",
            "INSERT INTO numbers(value) VALUES (1)",
            "INSERT INTO numbers(value) VALUES (2)"
        )
        populator.populateDatabase(database, statements, overwrite = false)

        runInDatabase(database, "SELECT value FROM numbers") { statement ->
            statement.executeQuery().use { result ->
                assertTrue(result.next())
                assertEquals(1, result.getInt("value"))

                assertTrue(result.next())
                assertEquals(2, result.getInt("value"))

                assertFalse(result.next())
            }

        }
    }

    @Test
    fun `Existing database gets populated correctly`() {
        val database = tempDir.resolve("test.db")
        runInDatabase(database, "CREATE TABLE numbers(value INTEGER PRIMARY KEY)") { statement ->
            statement.execute()
        }

        val statements = listOf(
            "INSERT INTO numbers(value) VALUES (1)",
            "INSERT INTO numbers(value) VALUES (2)"
        )
        populator.populateDatabase(database, statements, overwrite = false)

        runInDatabase(database, "SELECT value FROM numbers") { statement ->
            statement.executeQuery().use { result ->
                assertTrue(result.next())
                assertEquals(1, result.getInt("value"))

                assertTrue(result.next())
                assertEquals(2, result.getInt("value"))

                assertFalse(result.next())
            }
        }
    }

    @Test
    fun `Databases are overwritten if they exist`() {
        val database = tempDir.resolve("test.db")
        database.createNewFile()

        val statements = listOf(
            "CREATE TABLE numbers(value INTEGER PRIMARY KEY)",
            "INSERT INTO numbers(value) VALUES (1)",
            "INSERT INTO numbers(value) VALUES (2)"
        )
        populator.populateDatabase(database, statements, overwrite = true)

        runInDatabase(database, "SELECT value FROM numbers") { statement ->
            statement.executeQuery().use { result ->
                assertTrue(result.next())
                assertEquals(1, result.getInt("value"))

                assertTrue(result.next())
                assertEquals(2, result.getInt("value"))

                assertFalse(result.next())
            }
        }
    }

    @Test
    fun `Exception is thrown when non-database file exists and should not be overwritten`() {
        val database = tempDir.resolve("test.txt")
        database.writeText("This is not a database")

        val statements = listOf("CREATE TABLE numbers(value INTEGER PRIMARY KEY)")
        assertThrows<IllegalStateException> {
            populator.populateDatabase(database, statements, overwrite = false)
        }
    }

    private fun runInDatabase(database: File, statement: String, block: (PreparedStatement) -> Unit) {
        DriverManager.getConnection("jdbc:sqlite:$database").use { connection ->
            connection.prepareStatement(statement).use(block)
        }
    }
}
