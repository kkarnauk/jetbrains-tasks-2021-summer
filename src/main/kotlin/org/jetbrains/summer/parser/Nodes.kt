package org.jetbrains.summer.parser

internal abstract class Node(protected val line: Int) {
    abstract fun evaluate(varValues: HashMap<String, Int>): Int
}

internal class ConstantNode(line: Int, private val value: Int) : Node(line) {
    override fun evaluate(varValues: HashMap<String, Int>): Int = value
}

internal class IdentifierNode(line: Int, private val value: String) : Node(line) {
    override fun evaluate(varValues: HashMap<String, Int>): Int {
        if (varValues.containsKey(value)) {
            return varValues.getValue(value)
        } else {
            throw ParameterNotFoundException(value, line)
        }
    }
}

internal class BinaryExpressionNode(line: Int, private val left: Node, private val right: Node,
                                    private val operation: (Int, Int) -> Int) : Node(line) {
    override fun evaluate(varValues: HashMap<String, Int>): Int =
        operation(left.evaluate(varValues), right.evaluate(varValues))
}

internal class IfExpressionNode(line: Int, private val condition: Node, private val ifTrue: Node,
                                private val ifFalse: Node) : Node(line) {
    override fun evaluate(varValues: HashMap<String, Int>): Int {
        return if (condition.evaluate(varValues) != 0)
            ifTrue.evaluate(varValues)
        else
            ifFalse.evaluate(varValues)
    }
}

internal class CallExpressionNode(line: Int, private val functionStorage: FunctionStorage,
                                  private val argsValues: List<Node>) : Node(line) {

    override fun evaluate(varValues: HashMap<String, Int>): Int {
        if (functionStorage.functionDefinition == null) {
            throw FunctionNotFoundException(functionStorage.functionName, line)
        }

        val functionDefinition = functionStorage.functionDefinition!!
        if (functionDefinition.args.size != argsValues.size) {
            throw ArgumentNumberMismatchException(functionStorage.functionName, line)
        }

        val newVarValues = HashMap<String, Int>()
        for (i in argsValues.indices) {
            newVarValues[functionDefinition.args[i]] = argsValues[i].evaluate(varValues)
        }
        return functionDefinition.expression.evaluate(newVarValues)
    }
}
