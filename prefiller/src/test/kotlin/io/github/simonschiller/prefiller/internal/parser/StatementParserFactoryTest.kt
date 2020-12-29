package io.github.simonschiller.prefiller.internal.parser

import com.google.gson.JsonParser
import io.github.simonschiller.prefiller.internal.parser.room.RoomSchemaStatementParserV1
import io.github.simonschiller.prefiller.internal.parser.sqlite.SqliteStatementParser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import java.io.File

class StatementParserFactoryTest {
    private val parserFactory = StatementParserFactory()

    @TempDir
    lateinit var tempDir: File

    @Test
    fun `Correct parser is returned for SQL files`() {
        val file = tempDir.resolve("script.sql")
        file.createNewFile()

        val parser = parserFactory.createParser(file)
        assertEquals(SqliteStatementParser::class, parser::class)
    }

    @Test
    fun `Correct parser is returned for Room schema files`() {
        val parsers = mapOf(
            "schemas/v1.json" to RoomSchemaStatementParserV1::class
        )

        parsers.forEach { (resourcePath, parserClass) ->
            val file = File(ClassLoader.getSystemResource(resourcePath).file)

            val parser = parserFactory.createParser(file)
            assertEquals(parserClass, parser::class)
        }
    }

    @Test
    fun `Exception is thrown for unknown Room schema version`() {
        val resource = ClassLoader.getSystemResource("schemas/v1.json")
        val file = tempDir.resolve("schema.json")
        file.writeText(resource.readText())

        val json = JsonParser.parseString(file.readText()).asJsonObject
        json.addProperty("formatVersion", 0)
        file.writeText(json.toString())

        assertThrows<IllegalArgumentException> { parserFactory.createParser(file) }
    }

    @Test
    fun `Exception is thrown for unknown JSON object`() {
        val file = tempDir.resolve("unknown.json")
        file.writeText("""{ "foo": "bar" }""")
        assertThrows<IllegalArgumentException> { parserFactory.createParser(file) }
    }

    @Test
    fun `Exception is thrown for illegal JSON`() {
        val file = tempDir.resolve("illegal.json")
        file.writeText("Not a JSON document")
        assertThrows<IllegalArgumentException> { parserFactory.createParser(file) }
    }

    @Test
    fun `Exception is thrown for unknown file type`() {
        val file = tempDir.resolve("unknown.txt")
        file.writeText("This file should not be parsable")
        assertThrows<IllegalArgumentException> { parserFactory.createParser(file) }
    }
}
