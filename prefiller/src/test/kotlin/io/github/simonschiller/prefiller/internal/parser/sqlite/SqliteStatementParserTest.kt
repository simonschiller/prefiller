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

package io.github.simonschiller.prefiller.internal.parser.sqlite

import com.google.common.truth.Truth.assertThat
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
        assertThat(parser.parse()).containsExactly(
            "INSERT INTO people(firstname, lastname, age) VALUES (\"Mikael\", \"Burke\", 38)",
            "INSERT INTO people(firstname, lastname, age) VALUES (\"Ayana\", \"Clarke\", 12)",
            "INSERT INTO people(firstname, lastname, age) VALUES (\"Malachy\", \"Wall\", 24)",
        )
    }

    @Test
    fun `Exception is thrown if script contains invalid SQL`() {
        val scriptFile = tempDir.resolve("script.sql")
        scriptFile.writeText("INSERT INTO people(firstname, lastname) VAUES (1; 2; 3)")

        val parser = SqliteStatementParser(scriptFile)
        assertThrows<IllegalArgumentException> { parser.parse() }
    }
}
