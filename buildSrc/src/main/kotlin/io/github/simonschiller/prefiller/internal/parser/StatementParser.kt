package io.github.simonschiller.prefiller.internal.parser

internal interface StatementParser {
    fun parse(): List<String>
}
