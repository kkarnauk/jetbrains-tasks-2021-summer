package org.jetbrains.summer.parser

internal data class FunctionDefinition(val args: List<String>, val expression: Node)

/*
Needed for recursion (difficult order of definitions)
 */
internal data class FunctionStorage(var functionDefinition: FunctionDefinition?)
