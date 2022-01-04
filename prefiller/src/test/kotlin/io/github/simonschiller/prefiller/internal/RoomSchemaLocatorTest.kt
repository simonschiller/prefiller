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

package io.github.simonschiller.prefiller.internal

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import java.io.File

class RoomSchemaLocatorTest {
    private val locator = RoomSchemaLocator()

    @TempDir
    lateinit var tempDir: File

    @Test
    fun `Schema with the latest version is returned`() {
        tempDir.resolve("1.json").createNewFile()
        tempDir.resolve("2.JSON").createNewFile()
        tempDir.resolve("v3.json").createNewFile()
        tempDir.resolve("3.txt").createNewFile()

        assertEquals("2.JSON", locator.findLatestRoomSchema(tempDir).name)
    }

    @Test
    fun `Exception is thrown if no schema can be found`() {
        tempDir.resolve("v3.json").createNewFile()
        tempDir.resolve("3.txt").createNewFile()

        assertThrows<IllegalStateException> { locator.findLatestRoomSchema(tempDir) }
    }
}
