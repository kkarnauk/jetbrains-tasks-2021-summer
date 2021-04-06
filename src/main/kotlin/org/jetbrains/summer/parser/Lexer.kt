package org.jetbrains.summer.parser

import java.io.StringReader

class Lexer(program: String) {
    private val reader: StringReader = StringReader(program)

    var currentLexema: Lexema = Lexema.EOF
        private set
    var currentNumber: Int = 0
        private set
        get() {
            if (currentLexema != Lexema.Number) {
                throw IllegalStateException("Cannot get number value: current lexema is not Number")
            }
            return field
        }
    var currentIdentifier: String = ""
        private set
        get() {
            if (currentLexema != Lexema.Identifier) {
                throw IllegalStateException("Cannot get identifier value: current lexema is not Identifier")
            }
            return field
        }

    private var currentChar = '\u0000'

    init {
        nextChar()
    }

    private fun nextChar() {
        val value = reader.read()
        currentChar = if (value == -1) {
            '\u0000'
        } else {
            value.toChar()
        }
    }

    fun nextLexema(): Lexema {
        var lexema: Lexema = Lexema.EOF
        when (currentChar) {
            '\u0000' -> {
                currentLexema = Lexema.EOF
                return currentLexema
            }
            '+' -> lexema = Lexema.Plus
            '-' -> lexema = Lexema.Minus
            '*' -> lexema = Lexema.Asterisk
            '/' -> lexema = Lexema.Slash
            '%' -> lexema = Lexema.PercentSign
            '>' -> lexema = Lexema.GreaterSign
            '<' -> lexema = Lexema.LessSign
            '=' -> lexema = Lexema.EqualsSign
            ',' -> lexema = Lexema.Comma
            ':' -> lexema = Lexema.Semicolon
            '(' -> lexema = Lexema.LeftParen
            ')' -> lexema = Lexema.RightParen
            '[' -> lexema = Lexema.LeftBracket
            ']' -> lexema = Lexema.RightBracket
            '{' -> lexema = Lexema.LeftBrace
            '}' -> lexema = Lexema.RightBrace
            '?' -> lexema = Lexema.QuestionMark
            '\n' -> lexema = Lexema.EOL
        }

        when {
            lexema != Lexema.EOF -> {
                currentLexema = lexema
                nextChar()
            }
            currentChar.isDigit() -> {
                val numberBuilder = StringBuilder()
                while (currentChar.isDigit()) {
                    numberBuilder.append(currentChar)
                    nextChar()
                }
                currentNumber = numberBuilder.toString().toInt()
                currentLexema = Lexema.Number
            }
            currentChar.isLetter() -> {
                val identifierBuilder = StringBuilder()
                while (currentChar.isLetter()) {
                    identifierBuilder.append(currentChar)
                    nextChar()
                }
                currentIdentifier = identifierBuilder.toString()
                currentLexema = Lexema.Identifier
            }
            else -> {
                throw IllegalSyntaxException()
            }
        }

        return currentLexema
    }
}