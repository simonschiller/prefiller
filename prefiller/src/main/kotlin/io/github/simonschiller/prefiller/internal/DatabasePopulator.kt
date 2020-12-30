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
