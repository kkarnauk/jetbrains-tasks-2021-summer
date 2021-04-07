package org.jetbrains.summer.matricesmultiplier

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MatrixTest {
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
}