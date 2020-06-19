package io.github.simonschiller.prefiller.internal.parser.sqlite

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import java.io.File

class SqliteStatementParserTest {

    @TempDir
    lateinit var tempDir: File

    @Test
    fun `Statements are parsed correctly`() {
        val scriptFile = tempDir.resolve("script.sql")
        scriptFile.writeText("""
            INSERT INTO people(firstname, lastname, age) VALUES ("Mikael", "Burke", 38);
            INSERT INTO people(firstname, lastname, age) VALUES ("Ayana", "Clarke", 12);
            INSERT INTO people(firstname, lastname, age) VALUES ("Malachy", "Wall", 24);
        """.trimIndent())

        val parser = SqliteStatementParser(scriptFile)
        val statements = listOf(
            "INSERT INTO people(firstname, lastname, age) VALUES (\"Mikael\", \"Burke\", 38)",
            "INSERT INTO people(firstname, lastname, age) VALUES (\"Ayana\", \"Clarke\", 12)",
            "INSERT INTO people(firstname, lastname, age) VALUES (\"Malachy\", \"Wall\", 24)"
        )
        assertEquals(statements, parser.parse())
    }

    @Test
    fun `Exception is thrown if script contains invalid SQL`() {
        val scriptFile = tempDir.resolve("script.sql")
        scriptFile.writeText("INSERT INTO people(firstname, lastname) VAUES (1; 2; 3)")

        val parser = SqliteStatementParser(scriptFile)
        assertThrows<IllegalArgumentException> { parser.parse() }
    }
}
