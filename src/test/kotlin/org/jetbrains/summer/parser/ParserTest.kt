package org.jetbrains.summer.parser

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ParserTest {
    private fun evaluate(program: String): Int {
        return Parser.Companion.evaluate(program)
    }

    @Test
    fun testCalculator() {
        assertEquals(10, evaluate("(2+(2*4))"))
        assertEquals(4, evaluate("(2+((3*4)/5))"))
        assertEquals(-2, evaluate("((-2*10)%6)"))
        assertEquals(-105, evaluate("(((2*-3)*((2+3)*7))/2)"))
        assertEquals(0, evaluate("(0*(-105+10))"))
        assertEquals(140, evaluate("(((((1+2)/2)*3231)/23)%(213+(10/9)))"))
        assertEquals(7, evaluate("((1<2)*7)"))
        assertEquals(2, evaluate("(1+(2>-2))"))
        assertEquals(1, evaluate("(1=(3/2))"))
        assertEquals(0, evaluate("(1<(2-3))"))
        assertEquals(469668928, evaluate("(2312*(213123-9979))"))
    }

    @Test
    fun testIf() {
        assertEquals(0, evaluate("[((10+20)>(20+10))]?{1}:{0}"))
        assertEquals(130, evaluate("(10*[(10-9)]?{13}:{0})"))
        assertEquals(12, evaluate("(-2*[(10/11)]?{((2+3)*2)}:{(-13/2)})"))
        assertEquals(-6, evaluate("(((1-2)*3)+[([1]?{10}:{-2}+[(10-10)]?{134}:{-7})]?{-3}:{3})"))
        assertEquals(5, evaluate("[[[[[1]?{0}:{1}]?{0}:{1}]?{0}:{1}]?{0}:{1}]?{5}:{10}"))
    }

    @Test
    fun testFunctionCall() {
        assertEquals(-7, evaluate(
            "f(x)={x}\n" +
                    "f(-7)"
        ))
        assertEquals(6765, evaluate(
            "f(x)={[(x>1)]?{(f((x-1))+f((x-2)))}:{x}}\n" +
                    "f(20)"
        ))
        assertEquals(60, evaluate(
            "g(x)={(f(x)+f((x/2)))}\n" +
                    "f(x)={[(x>1)]?{(f((x-1))+f((x-2)))}:{x}}\n" +
                    "g(10)"
        ))
        assertEquals(382, evaluate(
            "first(argumentF,argumentB)={(argumentF+argumentB)}\n" +
                    "second(x,y)={(x*y)}\n" +
                    "first(second(11,2),second(second(3,4),first(10,20)))"
        ))
        assertEquals(15,evaluate(
            "FIRST(x)={(x+1)}\n" +
                    "SECOND(y)={(FIRST(y)+2)}\n" +
                    "THIRD(third)={(SECOND(third)+3)}\n" +
                    "((THIRD(5)/2)+10)"
        ))
    }

    private inline fun <reified T : Throwable> assertThrowsWithMessage(message: String,
                                                                       noinline executable: () -> Unit) {
        val exception = assertThrows<T> { executable() }
        assertEquals(message, exception.message)
    }

    @Test
    fun testExceptions() {
        assertThrowsWithMessage<IllegalSyntaxException>("SYNTAX ERROR") {
            evaluate("1+2+3")
        }
        assertThrowsWithMessage<IllegalSyntaxException>("SYNTAX ERROR") {
            evaluate("(1+2)*2")
        }
        assertThrowsWithMessage<ParameterNotFoundException>("PARAMETER NOT FOUND y:1") {
            evaluate("f(x)={y}\nf(10)")
        }
        assertThrowsWithMessage<ParameterNotFoundException>("PARAMETER NOT FOUND hello:2") {
            evaluate("func(x)={1}\nfuncTwo(x,y)={(y+hello)}\nfuncTwo(1,2)")
        }
        assertThrowsWithMessage<FunctionNotFoundException>("FUNCTION NOT FOUND f:1") {
            evaluate("g(x)={f(x)}\ng(10)")
        }
        assertThrowsWithMessage<ArgumentNumberMismatchException>("ARGUMENT NUMBER MISMATCH g:2") {
            evaluate("g(x)={(x+1)}\ng(10,20)")
        }
        assertThrowsWithMessage<MultipleFunctionDefinitionException>("MULTIPLE FUNCTION DEFINITION f:2") {
            evaluate("f(x)={x}\nf(x)={(x+1)}\nf(2)")
        }
    }
}