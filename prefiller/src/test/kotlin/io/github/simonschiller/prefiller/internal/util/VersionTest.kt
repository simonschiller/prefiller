package io.github.simonschiller.prefiller.internal.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class VersionTest {

    @Test
    fun `Parsing versions works correctly`() {
        assertThrows<IllegalArgumentException> { Version.parse("12") }
        assertEquals(Version(12, 451), Version.parse("12.451"))
        assertEquals(Version(12, 451, 6), Version.parse("12.451.6"))
        assertEquals(Version(12, 451, 6, "beta05"), Version.parse("12.451.6-beta05"))
        assertThrows<IllegalArgumentException> { Version.parse("12.451.6.4-beta05") }
    }

    @Test
    fun `Base versions are generated correctly`() {
        assertEquals(Version(12, 451, 6), Version(12, 451, 6, "beta05").baseVersion())
    }

    @Test
    fun `Comparing versions works correctly`() {
        assertTrue(Version(12, 5) > Version(12, 4))
        assertTrue(Version(12, 5, 1) > Version(12, 5))
        assertTrue(Version(12, 3, 17) < Version(12, 4))
        assertTrue(Version(12, 3, 17, "alpha03") < Version(12, 3, 17))
    }

    @Test
    fun `String representation works correctly`() {
        assertEquals("12.451", Version(12, 451).toString())
        assertEquals("12.451.6", Version(12, 451, 6).toString())
        assertEquals("12.451.6-beta05", Version(12, 451, 6, "beta05").toString())
    }
}
