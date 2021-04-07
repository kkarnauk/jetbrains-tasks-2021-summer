package org.jetbrains.summer.parser

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

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
}