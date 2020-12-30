package io.github.simonschiller.prefiller.internal

import java.io.File

internal class RoomSchemaLocator {

    fun findLatestRoomSchema(schemaDirectory: File): File {
        return schemaDirectory
            .listFiles { file -> file.extension.equals("json", ignoreCase = true) } // Find all JSON files
            ?.filter { file -> file.nameWithoutExtension.toIntOrNull() != null } // Ignore non-numeric file names
            ?.maxBy { file -> file.nameWithoutExtension.toInt() } // Take the file with the highest database version
            ?: error("Cannot find Room schema definition in $schemaDirectory, make sure Room is configured correctly")
    }
}
