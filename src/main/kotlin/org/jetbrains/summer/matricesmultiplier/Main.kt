package org.jetbrains.summer.matricesmultiplier

import java.util.*

fun main() {
    val matrix1 = readMatrix()
    val matrix2 = readMatrix()
    val result = matrix1 * matrix2
    println(result)
}

private fun readMatrix(): Matrix {
    val reader = Scanner(System.`in`)
    val rows = reader.nextInt()
    val cols = reader.nextInt()
    val values = Array(rows * cols) { reader.nextInt() }
    return Matrix.valueOf(rows, cols, values)
}