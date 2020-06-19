package io.github.simonschiller.prefiller.internal.parser

import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import io.github.simonschiller.prefiller.internal.parser.room.RoomSchemaStatementParserV1
import io.github.simonschiller.prefiller.internal.parser.sqlite.SqliteStatementParser
import java.io.File

internal class StatementParserFactory {

    fun createParser(target: File): StatementParser = when {
        target.extension.equals("json", ignoreCase = true) -> createRoomSchemaParser(target)
        target.extension.equals("sql", ignoreCase = true) -> SqliteStatementParser(target)
        else -> throw IllegalArgumentException("Could not create parser for .${target.extension} file")
    }

    private fun createRoomSchemaParser(target: File): StatementParser {
        val json = try {
            target.reader().use(JsonParser::parseReader).asJsonObject
        } catch (exception: JsonParseException) {
            throw IllegalArgumentException("Could not parse JSON file")
        }

        // Parse the version of the Room schema
        val version = try {
            json.get("formatVersion").asInt
        } catch (exception: Exception) {
            throw IllegalArgumentException("Could not read format version, make sure the Room schema is valid")
        }

        return when (version) {
            1 -> RoomSchemaStatementParserV1(json)
            else -> throw IllegalArgumentException("Room schema format version $version is currently not supported")
        }
    }
}
