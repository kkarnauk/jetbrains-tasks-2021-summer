package org.jetbrains.summer.parser

abstract class Node {
    abstract fun evaluate(varValues: HashMap<String, Int>): Int
}

class ConstantNode(private val value: Int) : Node() {
    override fun evaluate(varValues: HashMap<String, Int>): Int = value
}

class IdentifierNode(private val value: String) : Node() {
    override fun evaluate(varValues: HashMap<String, Int>): Int {
        if (varValues.containsKey(value)) {
            return varValues.getValue(value)
        } else {
            throw ParameterNotFoundException()
        }
    }
}

class BinaryExpressionNode(private val left: Node, private val right: Node,
                           private val operation: (Int, Int) -> Int) : Node() {
    override fun evaluate(varValues: HashMap<String, Int>): Int =
        operation(left.evaluate(varValues), right.evaluate(varValues))
}

class IfExpressionNode(private val condition: Node, private val ifTrue: Node, private val ifFalse: Node) : Node() {
    override fun evaluate(varValues: HashMap<String, Int>): Int =
        if (condition.evaluate(varValues) != 0) ifTrue.evaluate(varValues) else ifFalse.evaluate(varValues)
}
