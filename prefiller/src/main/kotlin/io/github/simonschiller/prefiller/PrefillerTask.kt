package io.github.simonschiller.prefiller

import io.github.simonschiller.prefiller.internal.DatabasePopulator
import io.github.simonschiller.prefiller.internal.RoomSchemaLocator
import io.github.simonschiller.prefiller.internal.parser.StatementParserFactory
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.*

@CacheableTask
@Suppress("UnstableApiUsage")
open class PrefillerTask : DefaultTask() {

    @InputDirectory
    @PathSensitive(PathSensitivity.RELATIVE)
    val schemaDirectory: DirectoryProperty = project.objects.directoryProperty()

    @InputFile
    @PathSensitive(PathSensitivity.RELATIVE)
    val scriptFile: RegularFileProperty = project.objects.fileProperty()

    @OutputFile
    val generatedDatabaseFile: RegularFileProperty = project.objects.fileProperty()

    @TaskAction
    fun generateDatabase() {

        // Find the latest database schema file
        val schemaLocator = RoomSchemaLocator()
        val schemaFile = schemaLocator.findLatestRoomSchema(schemaDirectory.get().asFile)

        // Parse the statements
        val parserFactory = StatementParserFactory()
        val setupStatements = parserFactory.createParser(schemaFile).parse()
        val scriptStatements = parserFactory.createParser(scriptFile.get().asFile).parse()

        // Clear the old and populate the new database
        val databaseFile = generatedDatabaseFile.get().asFile
        val databasePopulator = DatabasePopulator()
        databasePopulator.populateDatabase(databaseFile, setupStatements, overwrite = true)
        databasePopulator.populateDatabase(databaseFile, scriptStatements, overwrite = false)
    }
}
