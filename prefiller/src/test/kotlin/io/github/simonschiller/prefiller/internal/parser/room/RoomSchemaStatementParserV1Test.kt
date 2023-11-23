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

package io.github.simonschiller.prefiller.internal.parser.room

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import java.io.File
import jakarta.json.Json

class RoomSchemaStatementParserV1Test {

    @TempDir
    lateinit var tempDir: File

    @Test
    fun `Statements are parsed correctly`() {
        val resource = ClassLoader.getSystemResource("schemas/v1.json")
        val schemaFile = tempDir.resolve("schema.json")
        schemaFile.writeText(resource.readText())

        val json = Json.createReader(schemaFile.reader()).readObject()
        val parser = RoomSchemaStatementParserV1(json)

        assertThat(parser.parse()).containsExactly(
            "CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)",
            "INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'a2e9a19561a60299601930b4d1453ede')",
            "CREATE TABLE IF NOT EXISTS `creditcards` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `customerId` INTEGER NOT NULL, `cardNumber` TEXT NOT NULL, FOREIGN KEY(`customerId`) REFERENCES `customers`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )",
            "CREATE INDEX IF NOT EXISTS `index_creditcards_customerId` ON `creditcards` (`customerId`)",
            "CREATE TABLE IF NOT EXISTS `customers` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `age` INTEGER NOT NULL)",
            "CREATE UNIQUE INDEX IF NOT EXISTS `index_customers_name` ON `customers` (`name`)",
            "CREATE VIRTUAL TABLE IF NOT EXISTS `products` USING FTS4(`name` TEXT NOT NULL, `description` TEXT NOT NULL)",
        )
    }

    @Test
    fun `Exception is thrown for unknown JSON object`() {
        val json = Json.createObjectBuilder().add("foo", "bar").build()
        val parser = RoomSchemaStatementParserV1(json)
        assertThrows<IllegalArgumentException> { parser.parse() }
    }
}
