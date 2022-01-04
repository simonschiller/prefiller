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

package io.github.simonschiller.prefiller.internal.util

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class VersionTest {

    @Test
    fun `Parsing versions works correctly`() {
        assertThrows<IllegalArgumentException> { Version.parse("12") }
        assertThat(Version.parse("12.451")).isEqualTo(Version(12, 451))
        assertThat(Version.parse("12.451.6")).isEqualTo(Version(12, 451, 6))
        assertThat(Version.parse("12.451.6-beta05")).isEqualTo(Version(12, 451, 6, "beta05"))
        assertThrows<IllegalArgumentException> { Version.parse("12.451.6.4-beta05") }
    }

    @Test
    fun `Base versions are generated correctly`() {
        assertThat(Version(12, 451, 6, "beta05").baseVersion()).isEqualTo(Version(12, 451, 6))
    }

    @Test
    fun `Comparing versions works correctly`() {
        assertThat(Version(12, 5)).isGreaterThan(Version(12, 4))
        assertThat(Version(12, 5, 1)).isGreaterThan(Version(12, 5))
        assertThat(Version(12, 3, 17)).isLessThan(Version(12, 4))
        assertThat(Version(12, 3, 17, "alpha03")).isLessThan(Version(12, 3, 17))
    }

    @Test
    fun `String representation works correctly`() {
        assertThat(Version(12, 451).toString()).isEqualTo("12.451")
        assertThat(Version(12, 451, 6).toString()).isEqualTo("12.451.6")
        assertThat(Version(12, 451, 6, "beta05").toString()).isEqualTo("12.451.6-beta05")
    }
}
