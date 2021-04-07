package org.jetbrains.summer.matricesmultiplier

class Matrix(val rows: Int, val cols: Int, private val init: Int = 0) {
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

    override fun equals(other: Any?): Boolean {
        if (other !is Matrix) {
            return false
        }
        return values.contentEquals(other.values)
    }

    override fun hashCode(): Int {
        var result = rows
        result = 31 * result + cols
        result = 31 * result + init
        result = 31 * result + values.contentHashCode()
        return result
    }
}