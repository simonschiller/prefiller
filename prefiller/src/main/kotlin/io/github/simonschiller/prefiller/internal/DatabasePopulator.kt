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

import org.sqlite.JDBC
import java.io.File
import java.sql.DriverManager
import java.sql.SQLException

internal class DatabasePopulator {

    fun populateDatabase(target: File, statements: List<String>, overwrite: Boolean) {
        if (overwrite) {
            target.delete() // Start from scratch when overwriting
        }

        // Populate the database
        try {
            DriverManager.getConnection("jdbc:sqlite:$target").use { connection ->
                connection.createStatement().use { statement ->
                    statements.forEach(statement::addBatch)
                    statement.executeBatch()
                }
            }
        } catch (exception: SQLException) {
            throw IllegalStateException("Could not populate database, please check your script", exception)
        }
    }

    // Make the SQLite JDBC driver usable
    companion object {
        init {
            DriverManager.registerDriver(JDBC())
        }
    }
}
