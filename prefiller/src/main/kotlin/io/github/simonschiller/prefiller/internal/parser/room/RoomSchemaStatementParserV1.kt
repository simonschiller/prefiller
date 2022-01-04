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

package io.github.simonschiller.prefiller.internal.parser.room

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import io.github.simonschiller.prefiller.internal.parser.StatementParser

internal class RoomSchemaStatementParserV1(private val json: JsonObject) : StatementParser {

    override fun parse(): List<String> = try {
        val root = json.getAsJsonObject("database")
        val statements = mutableListOf<String>()

        // Create and fill master table
        val setupQueries = root.getAsJsonArray("setupQueries")
        setupQueries.map(JsonElement::getAsString).forEach { setupQuery ->
            statements += setupQuery
        }

        // Create entity tables
        val entities = root.getAsJsonArray("entities")
        entities.forEach { entity ->
            val tableName = entity.asJsonObject.get("tableName").asString
            val createTable = entity.asJsonObject.get("createSql").asString
            statements += createTable.replace("\${TABLE_NAME}", tableName)

            // Create indices for table
            val indices = entity.asJsonObject.getAsJsonArray("indices")
            indices.forEach { index ->
                val createIndex = index.asJsonObject.get("createSql").asString
                statements += createIndex.replace("\${TABLE_NAME}", tableName)
            }
        }

        // SQLite (temporarily) allows foreign keys that reference non-existent tables, so the order in which the
        // create statements are executed does not matter. We don't have to create views, since Room will create them
        // when importing the database.
        statements
    } catch (exception: Exception) {
        throw IllegalArgumentException("Could not parse Room schema, make sure it is valid")
    }
}
