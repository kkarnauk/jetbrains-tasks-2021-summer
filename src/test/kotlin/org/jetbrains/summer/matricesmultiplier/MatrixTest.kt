package org.jetbrains.summer.matricesmultiplier

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.math.abs

class MatrixTest {
    private val random = Random(11917)

    private fun getRandomMatrix(maxSize: Int, maxValue: Int): Matrix {
        val rows = abs(random.nextInt()) % maxSize + 1
        val cols = abs(random.nextInt()) % maxSize + 1
        val values = Array(rows * cols) { random.nextInt() % maxValue }
        return Matrix.valueOf(rows, cols, values)
    }

    @Test
    fun testBasics() {
        run {
            val matrix = Matrix(3, 2)
            for (i in 0 until matrix.rows) {
                for (j in 0 until matrix.cols) {
                    assertEquals(0, matrix[i, j])
                }
            }
            matrix[0, 0] = 1
            matrix[0, 1] = 2
            assertEquals(1, matrix[0, 0])
            assertEquals(2, matrix[0, 1])
        }
        run {
            val matrix = Matrix.valueOf(3, 2, arrayOf(1, 2, 3, 4, 5 ,6))
            for (i in 0 until matrix.rows) {
                for (j in 0 until matrix.cols) {
                    assertEquals(i * matrix.cols + j + 1, matrix[i, j])
                }
            }
        }
        run {
            val matrix = Matrix.valueOf(2, 4, 0, 1, 2, 3, 4, 5, 6, 7)
            for (i in 0 until matrix.rows) {
                for (j in 0 until matrix.cols) {
                    assertEquals(i * matrix.cols + j, matrix[i, j])
                }
            }
        }
        run {
            val matrix = Matrix.identity(5)
            for (i in 0 until matrix.rows) {
                for (j in 0 until matrix.cols) {
                    assertEquals(if (i == j) 1 else 0, matrix[i, j])
                }
            }
        }
    }

    @Test
    fun testAdd() {
        run {
            val matrix1 = Matrix.valueOf(2, 2, 1, 2, 3, 4)
            val matrix2 = Matrix.valueOf(2, 2, 2, 7, 8, 1)
            val expected = Matrix.valueOf(2, 2, 3, 9, 11, 5)
            assertEquals(expected, matrix1 + matrix2)
        }
        run {
            val matrix1 = Matrix(4, 3, 7)
            val matrix2 = Matrix.valueOf(4, 3, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
            val result = matrix1 + matrix2
            for (i in 0 until matrix1.rows) {
                for (j in 0 until matrix1.cols) {
                    assertEquals(8 + i * matrix1.cols + j, result[i, j])
                }
            }
        }
    }

    @Test
    fun testBasicMultiply() {
        run {
            for (i in 0..10) {
                val matrix = getRandomMatrix(100, 1000)
                assertEquals(matrix, matrix * Matrix.identity(matrix.cols))
                assertEquals(matrix, Matrix.identity(matrix.rows) * matrix)
            }
        }
        run {
            val matrix1 = Matrix.valueOf(3, 3, 0, 2, 1, -10, 2, 123, -1212, 213, 654)
            val matrix2 = Matrix.valueOf(3, 2, 231, 12, -231, -9, 1, 6)
            val expected = Matrix.valueOf(3, 2, -461, -12, -2649, 600, -328521, -12537)
            assertEquals(expected, matrix1 * matrix2)
        }
        run {
            val matrix1 = Matrix.valueOf(1, 3, 2, 3, -1)
            val matrix2 = Matrix.valueOf(3, 1, -3, 2, 10)
            val expected = Matrix.valueOf(1, 1, -10)
            assertEquals(expected, matrix1 * matrix2)
        }
    }
}