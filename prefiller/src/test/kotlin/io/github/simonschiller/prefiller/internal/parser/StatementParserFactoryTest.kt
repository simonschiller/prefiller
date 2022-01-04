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

package io.github.simonschiller.prefiller.internal.parser

import com.google.common.truth.Truth.assertThat
import com.google.gson.JsonParser
import io.github.simonschiller.prefiller.internal.parser.room.RoomSchemaStatementParserV1
import io.github.simonschiller.prefiller.internal.parser.sqlite.SqliteStatementParser
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
        assertThat(parser).isInstanceOf(SqliteStatementParser::class.java)
    }

    @Test
    fun `Correct parser is returned for Room schema files`() {
        val parsers = mapOf(
            "schemas/v1.json" to RoomSchemaStatementParserV1::class
        )

        parsers.forEach { (resourcePath, parserClass) ->
            val file = File(ClassLoader.getSystemResource(resourcePath).file)

            val parser = parserFactory.createParser(file)
            assertThat(parser).isInstanceOf(parserClass.java)
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
