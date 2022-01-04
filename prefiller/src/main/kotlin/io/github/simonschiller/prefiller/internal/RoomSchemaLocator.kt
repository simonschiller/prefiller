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

import java.io.File

internal class RoomSchemaLocator {

    fun findLatestRoomSchema(schemaDirectory: File): File {
        return schemaDirectory
            .listFiles { file -> file.extension.equals("json", ignoreCase = true) } // Find all JSON files
            ?.filter { file -> file.nameWithoutExtension.toIntOrNull() != null } // Ignore non-numeric file names
            ?.maxByOrNull { file -> file.nameWithoutExtension.toInt() } // Take the file with the highest database version
            ?: error("Cannot find Room schema definition in $schemaDirectory, make sure Room is configured correctly")
    }
}
