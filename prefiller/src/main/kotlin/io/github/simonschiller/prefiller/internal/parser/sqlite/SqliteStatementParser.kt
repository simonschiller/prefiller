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

        override fun syntaxError(
            recognizer: Recognizer<*, *>?,
            offendingSymbol: Any?,
            line: Int,
            charPositionInLine: Int,
            message: String?,
            exception: RecognitionException?
        ) {
            throw IllegalArgumentException("Could not parse script, error at line $line:$charPositionInLine: $message")
        }
    }
}
