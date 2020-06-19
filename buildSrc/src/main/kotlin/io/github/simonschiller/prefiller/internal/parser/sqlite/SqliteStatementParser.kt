package io.github.simonschiller.prefiller.internal.parser.sqlite

import io.github.simonschiller.prefiller.antlr.SQLiteLexer
import io.github.simonschiller.prefiller.antlr.SQLiteParser
import io.github.simonschiller.prefiller.internal.parser.StatementParser
import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.misc.Interval
import java.io.File

internal class SqliteStatementParser(private val file: File) : StatementParser {

    override fun parse(): List<String> {
        val statements = mutableListOf<String>()

        val lexer = SQLiteLexer(CharStreams.fromPath(file.toPath()))
        lexer.addErrorListener(ErrorListener())

        val parser = SQLiteParser(CommonTokenStream(lexer))
        parser.addErrorListener(ErrorListener())

        val context = parser.parse()

        // Read the individual SQL statements from the parsed tree
        context.sql_stmt_list().flatMap { statementList -> statementList.sql_stmt() }.forEach { statement ->
            val interval = Interval(statement.start.startIndex, statement.stop.stopIndex)
            statements += statement.start.inputStream.getText(interval)
        }

        return statements
    }

    // ANTLR does not throw exceptions by default, we have to do that ourselves
    private class ErrorListener : BaseErrorListener() {

        override fun syntaxError(recognizer: Recognizer<*, *>?,
                                 offendingSymbol: Any?,
                                 line: Int,
                                 charPositionInLine: Int,
                                 message: String,
                                 exception: RecognitionException) {
            throw IllegalArgumentException("Could not parse script, error at line $line:$charPositionInLine: $message")
        }
    }
}
