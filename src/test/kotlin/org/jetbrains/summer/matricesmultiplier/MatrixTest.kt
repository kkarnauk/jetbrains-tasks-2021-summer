package org.jetbrains.summer.matricesmultiplier

import org.jetbrains.summer.matricesmultiplier.Matrix.Companion.basicMultiply
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.math.abs
import kotlin.system.measureTimeMillis
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds

class MatrixTest {
    private val random = Random(11917)

    private fun getRandomMatrix(rows: Int, cols: Int, maxValue: Int): Matrix {
        val values = Array(rows * cols) { random.nextInt() % maxValue }
        return Matrix.valueOf(rows, cols, values)
    }

    private fun getRandomSize(maxSize: Int): Int {
        return abs(random.nextInt()) % maxSize + 1
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
            for (test in 0..10) {
                val rows = getRandomSize(150)
                val cols = getRandomSize(150)
                val matrix = getRandomMatrix(rows, cols, 10000)
                assertEquals(matrix, basicMultiply(matrix, Matrix.identity(matrix.cols)))
                assertEquals(matrix, basicMultiply(Matrix.identity(matrix.rows), matrix))
            }
        }
        run {
            val matrix1 = Matrix.valueOf(3, 3, 0, 2, 1, -10, 2, 123, -1212, 213, 654)
            val matrix2 = Matrix.valueOf(3, 2, 231, 12, -231, -9, 1, 6)
            val expected = Matrix.valueOf(3, 2, -461, -12, -2649, 600, -328521, -12537)
            assertEquals(expected, basicMultiply(matrix1, matrix2))
        }
        run {
            val matrix1 = Matrix.valueOf(1, 3, 2, 3, -1)
            val matrix2 = Matrix.valueOf(3, 1, -3, 2, 10)
            val expected = Matrix.valueOf(1, 1, -10)
            assertEquals(expected, basicMultiply(matrix1, matrix2))
        }
    }

    @Test
    fun testFastMultiply() {
        for (test in 0..60) {
            val resRows = getRandomSize(300)
            val resCols = getRandomSize(300)
            val tmpSize = getRandomSize(300)
            val matrix1 = getRandomMatrix(resRows, tmpSize, 5000)
            val matrix2 = getRandomMatrix(tmpSize, resCols, 5000)
            assertEquals(basicMultiply(matrix1, matrix2), matrix1 * matrix2)
        }

        for (test in 0..10) {
            val size = getRandomSize(1200)
            val smallSize = size / 5 + 1
            val resRows = if (test % 2 == 0) size else smallSize
            val resCols = if (test % 2 == 0) smallSize else size
            val tmpSize = getRandomSize(800)
            val matrix1 = getRandomMatrix(resRows, tmpSize, 10000)
            val matrix2 = getRandomMatrix(tmpSize, resCols, 10000)
            assertEquals(basicMultiply(matrix1, matrix2), matrix1 * matrix2)
        }
    }

    @ExperimentalTime
    @Test
    fun testFastMultiplyTime() {
        for (test in 0..3) {
            val resRows = getRandomSize(1500)
            val resCols = getRandomSize(1500)
            val tmpSize = getRandomSize(1500)
            val matrix1 = getRandomMatrix(resRows, tmpSize, 20000)
            val matrix2 = getRandomMatrix(tmpSize, resCols, 20000)
            val benchmark = measureTimeMillis { matrix1 * matrix2 }

            println("($resRows, $tmpSize) x ($tmpSize, $resCols): ${benchmark.milliseconds} elapsed")
        }
    }
}