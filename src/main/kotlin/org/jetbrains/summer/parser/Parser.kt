package org.jetbrains.summer.parser

class Parser private constructor(program: String) {
    companion object {
        private fun parse(program: String): Node {
            val parser = Parser(program)
            return parser.parseProgram()
        }

        fun evaluate(program: String): Int {
            return parse(program).evaluate(HashMap())
        }
    }

    private val lexer = Lexer(program)
    private val functions: HashMap<String, FunctionDefinition> = HashMap()

    private fun parseProgram(): Node {
        val resultNode = parseExpression()
        getNextExpectedLexema(Lexema.EOF)
        return resultNode
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
        return when (lexer.lookAheadLexema) {
            Lexema.LeftParen ->
                parseBinaryExpression()
            Lexema.LeftBracket ->
                parseIfExpression()
            else ->
                parseConstantExpression()
        }
    }

    private fun parseConstantExpression(): Node {
        val minusLexema = tryGetNextExpectedLexema(Lexema.Minus)
        getNextExpectedLexema(Lexema.Number)
        val number = lexer.currentNumber
        return ConstantNode(if (minusLexema == null) number else -number)
    }

    private fun parseBinaryExpression(): Node {
        getNextExpectedLexema(Lexema.LeftParen)
        val leftNode = parseExpression()
        val operation = getOperationByLexema(lexer.nextLexema())
        val rightNode = parseExpression()
        getNextExpectedLexema(Lexema.RightParen)
        return BinaryExpressionNode(leftNode, rightNode, operation)
    }

    private fun getOperationByLexema(lexema: Lexema): (Int, Int) -> Int {
        return when (lexema) {
            Lexema.Plus -> { a, b -> a + b }
            Lexema.Minus -> { a, b -> a - b }
            Lexema.Slash -> { a, b -> a / b }
            Lexema.Asterisk -> { a, b -> a * b }
            Lexema.PercentSign -> { a, b -> a % b }
            Lexema.GreaterSign -> { a, b -> if (a > b) 1 else 0 }
            Lexema.LessSign -> { a, b -> if (a < b) 1 else 0 }
            Lexema.EqualsSign -> { a, b -> if (a == b) 1 else 0 }
            else -> throw IllegalSyntaxException()
        }
    }

    private fun parseIfExpression(): Node {
        getNextExpectedLexema(Lexema.LeftBracket)
        val conditionNode = parseExpression()
        getNextExpectedLexema(Lexema.RightBracket)
        getNextExpectedLexema(Lexema.QuestionMark)
        getNextExpectedLexema(Lexema.LeftBrace)
        val ifTrueNode = parseExpression()
        getNextExpectedLexema(Lexema.RightBrace)
        getNextExpectedLexema(Lexema.Semicolon)
        getNextExpectedLexema(Lexema.LeftBrace)
        val ifFalseNode = parseExpression()
        getNextExpectedLexema(Lexema.RightBrace)
        return IfExpressionNode(conditionNode, ifTrueNode, ifFalseNode)
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