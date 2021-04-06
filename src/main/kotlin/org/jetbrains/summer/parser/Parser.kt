package org.jetbrains.summer.parser

class Parser private constructor(program: String) {
    companion object {
        fun parse(program: String): Node {
            val parser = Parser(program)
            return parser.parseProgram()
        }
    }

    private val lexer = Lexer(program)

    private fun parseProgram(): Node {
        TODO("Not yet implemented")
    }
}