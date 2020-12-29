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
