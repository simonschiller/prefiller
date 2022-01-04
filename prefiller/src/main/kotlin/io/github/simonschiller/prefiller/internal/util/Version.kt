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

import java.util.Objects
import kotlin.IllegalArgumentException

/**
 * Lightweight port of [org.gradle.util.internal.VersionNumber], since this class was removed from
 * Gradle's public API in 7.1 (see https://github.com/gradle/gradle/issues/17450).
 */
class Version(
    private val major: Int,
    private val minor: Int,
    private val patch: Int? = null,
    private val qualifier: String? = null
) : Comparable<Version> {

    fun baseVersion(): Version = Version(major, minor, patch, null)

    override fun compareTo(other: Version): Int = when {
        major != other.major -> major - other.major
        minor != other.minor -> minor - other.minor
        patch != other.patch -> (patch ?: 0) - (other.patch ?: 0)
        else -> -(qualifier ?: "").compareTo(other.qualifier ?: "")
    }

    override fun equals(other: Any?): Boolean = other is Version && compareTo(other) == 0
    override fun hashCode(): Int = Objects.hash(major, minor, patch, qualifier)

    override fun toString(): String = when {
        qualifier != null -> "$major.$minor.$patch-$qualifier"
        patch != null -> "$major.$minor.$patch"
        else -> "$major.$minor"
    }

    companion object {

        fun parse(version: String): Version = try {
            val versionSplits = version.split('.')
            check(versionSplits.size == 2 || versionSplits.size == 3)

            val lastPartSplits = versionSplits.getOrNull(2)?.split('-') ?: emptyList()
            check(lastPartSplits.size in 0..2)

            Version(
                major = versionSplits[0].toInt(),
                minor = versionSplits[1].toInt(),
                patch = lastPartSplits.getOrNull(0)?.toInt(),
                qualifier = lastPartSplits.getOrNull(1)
            )
        } catch (exception: Exception) {
            throw IllegalArgumentException("Could not parse version $version", exception)
        }
    }
}
