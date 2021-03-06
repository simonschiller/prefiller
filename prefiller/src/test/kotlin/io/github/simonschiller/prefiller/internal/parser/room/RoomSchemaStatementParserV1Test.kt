package io.github.simonschiller.prefiller.internal.parser.room

import com.google.gson.JsonParser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import java.io.File

class RoomSchemaStatementParserV1Test {

    @TempDir
    lateinit var tempDir: File

    @Test
    fun `Statements are parsed correctly`() {
        val resource = ClassLoader.getSystemResource("schemas/v1.json")
        val schemaFile = tempDir.resolve("schema.json")
        schemaFile.writeText(resource.readText())

        val json = JsonParser.parseString(schemaFile.readText()).asJsonObject
        val parser = RoomSchemaStatementParserV1(json)

        val statements = listOf(
            "CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)",
            "INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'a2e9a19561a60299601930b4d1453ede')",
            "CREATE TABLE IF NOT EXISTS `creditcards` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `customerId` INTEGER NOT NULL, `cardNumber` TEXT NOT NULL, FOREIGN KEY(`customerId`) REFERENCES `customers`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )",
            "CREATE INDEX IF NOT EXISTS `index_creditcards_customerId` ON `creditcards` (`customerId`)",
            "CREATE TABLE IF NOT EXISTS `customers` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `age` INTEGER NOT NULL)",
            "CREATE UNIQUE INDEX IF NOT EXISTS `index_customers_name` ON `customers` (`name`)",
            "CREATE VIRTUAL TABLE IF NOT EXISTS `products` USING FTS4(`name` TEXT NOT NULL, `description` TEXT NOT NULL)"
        )
        assertEquals(statements, parser.parse())
    }

    @Test
    fun `Exception is thrown for unknown JSON object`() {
        val json = JsonParser.parseString("""{ "foo": "bar" }""").asJsonObject
        val parser = RoomSchemaStatementParserV1(json)
        assertThrows<IllegalArgumentException> { parser.parse() }
    }
}
