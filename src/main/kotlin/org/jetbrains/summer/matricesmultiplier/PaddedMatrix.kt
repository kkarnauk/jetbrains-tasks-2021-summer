package org.jetbrains.summer.matricesmultiplier

import kotlin.math.max
import kotlin.math.min

/**
 * Util class for matrix multiplication.
 *
 * @param fakeRows number of additional null-filled rows in the bottom of matrix.
 * @param fakeCols number of additional null-filled cols in the right of matrix.
 */
internal class PaddedMatrix(val matrix: Matrix, val fakeRows: Int = 0, val fakeCols: Int = 0) {
    val totalRows = matrix.rows + fakeRows
    val totalCols = matrix.cols + fakeCols

    companion object {
        /**
         * Merges four square matrices of the same size into one big matrix.
         *
         * The opposite of [splitIntoFour].
         */
        fun mergeFromFour(parts: Array<PaddedMatrix>): PaddedMatrix {
            assert(parts.size == 4)

            val rows = parts[0].matrix.rows + parts[2].matrix.rows
            val cols = parts[0].matrix.cols + parts[1].matrix.cols
            val upRows = parts[0].matrix.rows
            val leftCols = parts[0].matrix.cols
            val resultMatrix = Matrix(rows, cols)
            for (i in 0 until rows) {
                for (j in 0 until cols) {
                    if (i < upRows && j < leftCols) {
                        resultMatrix[i, j] += parts[0].matrix[i, j]
                    }
                    if (i < upRows && j >= leftCols) {
                        resultMatrix[i, j] += parts[1].matrix[i, j - leftCols]
                    }
                    if (i >= upRows && j < leftCols) {
                        resultMatrix[i, j] += parts[2].matrix[i - upRows, j]
                    }
                    if (i >= upRows && j >= leftCols) {
                        resultMatrix[i, j] += parts[3].matrix[i - upRows, j - leftCols]
                    }
                }
            }

            return PaddedMatrix(
                resultMatrix,
                parts[0].fakeRows + parts[2].fakeRows,
                parts[0].fakeCols + parts[1].fakeCols
            )
        }

        /**
         * Determines what multiplication should be called.
         */
        private const val smallSize = 128
        /**
         * Determines whether we have to call [basicMultiply]
         */
        private const val verySmallSize = 128

        /**
         * Implements basic multiplication with complexity O(n^3).
         *
         * It is not private for testing.
         */
        fun basicMultiply(first: PaddedMatrix, second: PaddedMatrix): PaddedMatrix {
            assert(first.totalCols == second.totalRows)

            val result = Matrix(first.matrix.rows, second.matrix.cols)
            val firstMatrix = first.matrix // optimization
            val secondMatrix = second.matrix // optimization
            val realThirdSize = min(firstMatrix.cols, secondMatrix.rows)
            for (row in 0 until result.rows) {
                for (i in 0 until realThirdSize) { // swap loops for optimization
                    for (col in 0 until result.cols) {
                        result[row, col] += firstMatrix[row, i] * secondMatrix[i, col]
                    }
                }
            }

            return PaddedMatrix(result, first.fakeRows, second.fakeCols)
        }

        /**
         * Implements Strassen Algorithm for fast matrix multiplication with complexity O(n^2.81).
         */
        private fun fastMultiply(first: PaddedMatrix, second: PaddedMatrix): PaddedMatrix {
            assert(first.totalCols == second.totalRows)

            val firstParts = first.splitIntoFour()
            val secondParts = second.splitIntoFour()
            val tmpMatrices = arrayOf(
                multiply(firstParts[0] + firstParts[3], secondParts[0] + secondParts[3]),
                multiply(firstParts[2] + firstParts[3], secondParts[0]),
                multiply(firstParts[0], secondParts[1] - secondParts[3]),
                multiply(firstParts[3], secondParts[2] - secondParts[0]),
                multiply(firstParts[0] + firstParts[1], secondParts[3]),
                multiply(firstParts[2] - firstParts[0], secondParts[0] + secondParts[1]),
                multiply(firstParts[1] - firstParts[3], secondParts[2] + secondParts[3])
            )
            val parts = arrayOf(
                tmpMatrices[0] + tmpMatrices[3] - tmpMatrices[4] + tmpMatrices[6],
                tmpMatrices[2] + tmpMatrices[4],
                tmpMatrices[1] + tmpMatrices[3],
                tmpMatrices[0] - tmpMatrices[1] + tmpMatrices[2] + tmpMatrices[5]
            )

            return mergeFromFour(parts)
        }

        /**
         * Combines two approaches of multiplication.
         *
         * Needs matrices to have sizes (2^n x 2^n).
         */
        private fun multiply(first: PaddedMatrix, second: PaddedMatrix): PaddedMatrix {
            assert(first.totalCols == second.totalRows)

            return if (max(first.matrix.rows, second.matrix.cols) <= smallSize ||
                       min(first.matrix.rows, second.matrix.cols) <= verySmallSize) {
                basicMultiply(first, second)
            } else {
                fastMultiply(first, second)
            }
        }
    }

    operator fun plus(other: PaddedMatrix): PaddedMatrix {
        assert(totalRows == other.totalRows)
        assert(totalCols == other.totalCols)

        val result = Matrix(max(matrix.rows, other.matrix.rows), max(matrix.cols, other.matrix.cols))
        for (i in 0 until result.rows) {
            for (j in 0 until result.cols) {
                if (i < matrix.rows && j < matrix.cols) {
                    result[i, j] = matrix[i, j]
                }
                if (i < other.matrix.rows && j < other.matrix.cols) {
                    result[i, j] += other.matrix[i, j]
                }
            }
        }

        return PaddedMatrix(result, min(fakeRows, other.fakeRows), min(fakeCols, other.fakeCols))
    }

    operator fun minus(other: PaddedMatrix): PaddedMatrix {
        assert(totalRows == other.totalRows)
        assert(totalCols == other.totalCols)

        val result = Matrix(max(matrix.rows, other.matrix.rows), max(matrix.cols, other.matrix.cols))
        for (i in 0 until result.rows) {
            for (j in 0 until result.cols) {
                if (i < matrix.rows && j < matrix.cols) {
                    result[i, j] = matrix[i, j]
                }
                if (i < other.matrix.rows && j < other.matrix.cols) {
                    result[i, j] -= other.matrix[i, j]
                }
            }
        }

        return PaddedMatrix(result, min(fakeRows, other.fakeRows), min(fakeCols, other.fakeCols))
    }

    private fun ceilToPowerOfTwo(num: Int): Int {
        return if (num == num.takeHighestOneBit()) num else num.takeHighestOneBit() * 2
    }

    operator fun times(other: PaddedMatrix): PaddedMatrix {
        assert(totalCols == other.totalRows)

        // Algorithm requires sizes (2^n x 2^n)
        val size = max(max(totalRows, totalCols), max(other.totalRows, other.totalCols))
        val roundedSize = ceilToPowerOfTwo(size)

        // Memorize expected size
        val resultRows = totalRows
        val resultCols = other.totalCols

        val resultMatrix = multiply(
            PaddedMatrix(matrix, roundedSize - matrix.rows, roundedSize - matrix.cols),
            PaddedMatrix(other.matrix, roundedSize - other.matrix.rows, roundedSize - other.matrix.cols)
        ).matrix

        return PaddedMatrix(resultMatrix, resultRows - resultMatrix.rows, resultCols - resultMatrix.cols)
    }

    /**
     * Splits the matrix into four square matrices of the same size.
     * If [Matrix.cols] or [Matrix.rows] is odd, adds one more fake col/row.
     *
     * The opposite of [mergeFromFour].
     */
    fun splitIntoFour(): Array<PaddedMatrix> {
        val needRows = (totalRows + 1) / 2
        val needCols = (totalCols + 1) / 2
        val rows = min(needRows, matrix.rows)
        val cols = min(needCols, matrix.cols)
        val matrixParts = arrayOf(
            Matrix(rows, cols),
            Matrix(rows, matrix.cols - cols),
            Matrix(matrix.rows - rows, cols),
            Matrix(matrix.rows - rows, matrix.cols - cols)
        )

        for (i in 0 until matrix.rows) {
            val rowIndex = if (i < rows) 0 else 2
            val rowOffset = if (i < rows) 0 else rows
            for (j in 0 until matrix.cols) {
                val colIndex = if (j < cols) 0 else 1
                val colOffset = if (j < cols) 0 else cols
                matrixParts[rowIndex + colIndex][i - rowOffset, j - colOffset] = matrix[i, j]
            }
        }

        val newFakeUpRows = needRows - rows
        val newFakeDownRows = needCols - matrixParts[2].rows
        val newFakeLeftCols = needCols - cols
        val newFakeRightCols = needCols - matrixParts[1].cols

        return arrayOf(
            PaddedMatrix(matrixParts[0], newFakeUpRows, newFakeLeftCols),
            PaddedMatrix(matrixParts[1], newFakeUpRows, newFakeRightCols),
            PaddedMatrix(matrixParts[2], newFakeDownRows, newFakeLeftCols),
            PaddedMatrix(matrixParts[3], newFakeDownRows, newFakeRightCols)
        )
    }

    fun toMatrix(): Matrix {
        val result = Matrix(totalRows, totalCols)
        for (i in 0 until matrix.rows) {
            for (j in 0 until matrix.cols) {
                result[i, j] = matrix[i, j]
            }
        }

        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PaddedMatrix

        if (matrix != other.matrix) return false
        if (fakeRows != other.fakeRows) return false
        if (fakeCols != other.fakeCols) return false

        return true
    }

    override fun hashCode(): Int {
        var result = matrix.hashCode()
        result = 31 * result + fakeRows
        result = 31 * result + fakeCols
        return result
    }
}