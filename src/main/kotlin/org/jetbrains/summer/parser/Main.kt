package org.jetbrains.summer.parser

import java.lang.RuntimeException

fun main() {
    val program = generateSequence(::readLine).joinToString("\n").trim()
    try {
        println(Parser.evaluate(program))
    } catch (e: IllegalSyntaxException) {
        println(e.message)
    } catch (e: ParameterNotFoundException) {
        println(e.message)
    } catch (e: FunctionNotFoundException) {
        println(e.message)
    } catch (e: ArgumentNumberMismatchException) {
        println(e.message)
    } catch (e: MultipleFunctionDefinitionException) {
        println(e.message)
    } catch (e: RuntimeException) {
        println("RUNTIME ERROR")
    }
}