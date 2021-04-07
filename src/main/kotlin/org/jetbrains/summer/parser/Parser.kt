package org.jetbrains.summer.parser

import kotlin.math.exp

class Parser private constructor(program: String) {
    companion object {
        fun parse(program: String): Node {
            val parser = Parser(program)
            return parser.parseProgram()
        }
    }

    private val lexer = Lexer(program)
    private val functions: HashMap<String, FunctionDefinition> = HashMap()

    private fun parseProgram(): Node {
        TODO("Not yet implemented")
    }

    private fun parseFunctionDefinition() {
        getNextExpectedLexema(Lexema.Identifier)
        val funcName = lexer.currentIdentifier
        val params = parseParametersList()
        getNextExpectedLexema(Lexema.EqualsSign)
        getNextExpectedLexema(Lexema.LeftBrace)
        val expression = parseExpression()

        if (functions.containsKey(funcName)) {
            throw MultipleFunctionDefinitionException()
        }

        functions[funcName] = FunctionDefinition(params, expression)

        getNextExpectedLexema(Lexema.RightBrace)
    }

    private fun parseExpression(): Node {
        TODO("Not yet implemented")
    }

    private fun parseParametersList(): List<String> {
        getNextExpectedLexema(Lexema.LeftParen)

        val params = ArrayList<String>()
        while (true) {
            getNextExpectedLexema(Lexema.Identifier)
            params.add(lexer.currentIdentifier)
            if (lexer.nextLexema() != Lexema.Comma) {
                break
            }
        }

        if (lexer.currentLexema != Lexema.RightParen) {
            throw IllegalSyntaxException()
        }

        return params
    }

    private fun tryGetNextExpectedLexema(expectedLexema: Lexema): Lexema? {
        val lexema = lexer.lookAheadLexema
        if (lexema != expectedLexema) {
            return null
        }
        lexer.nextLexema()
        return lexema
    }

    private fun getNextExpectedLexema(expectedLexema: Lexema): Lexema {
        val lexema = lexer.nextLexema()
        if (lexema != expectedLexema) {
            throw IllegalSyntaxException()
        }
        return lexema
    }

    private data class FunctionDefinition(val args: List<String>, val expression: Node)
}