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
    }
}