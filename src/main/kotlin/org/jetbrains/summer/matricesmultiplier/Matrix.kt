package org.jetbrains.summer.matricesmultiplier

class Matrix(val rows: Int, val cols: Int, init: Int = 0) {
    companion object {
        fun valueOf(rows: Int, cols: Int, values: Array<Int>): Matrix {
            if (rows * cols != values.size) {
                throw IllegalArgumentException("Cannot create matrix from array: rows and cols don't match array size.")
            }

            val matrix = Matrix(rows, cols)
            for (i in matrix.values.indices) {
                matrix.values[i] = values[i]
            }

            return matrix
        }

        fun valueOf(rows: Int, cols: Int, vararg values: Int): Matrix {
            return valueOf(rows, cols, values.toTypedArray())
        }

        fun identity(rows: Int): Matrix {
            val matrix = Matrix(rows, rows)
            for (i in 0 until rows) {
                matrix[i, i] = 1
            }

            return matrix
        }

        /**
         * Implements basic multiplication.
         *
         * Needs only for testing correctness of general fast version.
         */
        fun basicMultiply(first: Matrix, second: Matrix): Matrix {
            if (first.cols != second.rows) {
                throw IllegalArgumentException("Cannot multiply matrices: sizes don't match.")
            }
            val paddedMatrix = PaddedMatrix.basicMultiply(PaddedMatrix(first), PaddedMatrix(second))
            return paddedMatrix.toMatrix()
        }
    }

    private val values = Array(rows * cols) { init } // using 1d-array for performance

    operator fun get(row: Int, col: Int): Int {
        return values[row * cols + col]
    }

    operator fun set(row: Int, col: Int, value: Int) {
        values[row * cols + col] = value
    }

    operator fun plus(other: Matrix): Matrix {
        if (rows != other.rows || cols != other.cols) {
            throw IllegalArgumentException("Cannot add two matrices: different sizes.")
        }

        val result = Matrix(rows, cols)
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                result[row, col] = this[row, col] + other[row, col]
            }
        }

        return result
    }

    operator fun times(other: Matrix): Matrix {
        if (cols != other.rows) {
            throw IllegalArgumentException("Cannot multiply matrices: sizes don't match.")
        }
        val paddedResult = PaddedMatrix(this) * PaddedMatrix(other)
        return paddedResult.toMatrix()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Matrix) {
            return false
        }
        return values.contentEquals(other.values)
    }

    override fun hashCode(): Int {
        var result = rows
        result = 31 * result + cols
        result = 31 * result + values.contentHashCode()
        return result
    }
}