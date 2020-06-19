package io.github.simonschiller.prefiller

import io.github.simonschiller.prefiller.internal.DatabasePopulator
import io.github.simonschiller.prefiller.internal.RoomSchemaLocator
import io.github.simonschiller.prefiller.internal.parser.StatementParserFactory
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import java.io.File

@CacheableTask
open class PrefillerTask : DefaultTask() {

    @InputDirectory
    @PathSensitive(PathSensitivity.RELATIVE)
    lateinit var schemaDirectory: File

    @InputFile
    @PathSensitive(PathSensitivity.RELATIVE)
    lateinit var scriptFile: File

    @OutputFile
    lateinit var generatedDatabaseFile: File

    @TaskAction
    fun generateDatabase() {

        // Find the latest database schema file
        val schemaLocator = RoomSchemaLocator()
        val schemaFile = schemaLocator.findLatestRoomSchema(schemaDirectory)

        // Parse the statements
        val parserFactory = StatementParserFactory()
        val setupStatements = parserFactory.createParser(schemaFile).parse()
        val scriptStatements = parserFactory.createParser(scriptFile).parse()

        // Clear the old and populate the new database
        val databasePopulator = DatabasePopulator()
        databasePopulator.populateDatabase(generatedDatabaseFile, setupStatements, overwrite = true)
        databasePopulator.populateDatabase(generatedDatabaseFile, scriptStatements, overwrite = false)
    }
}
