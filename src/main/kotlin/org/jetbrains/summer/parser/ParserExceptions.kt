package org.jetbrains.summer.parser

class IllegalSyntaxException :
    Exception("SYNTAX ERROR")

class ParameterNotFoundException(name: String, line: Int) :
    Exception("PARAMETER NOT FOUND $name:$line")

class FunctionNotFoundException(name: String, line: Int) :
    Exception("FUNCTION NOT FOUND $name:$line")

class ArgumentNumberMismatchException(name: String, line: Int) :
    Exception("ARGUMENT NUMBER MISMATCH $name:$line")

class MultipleFunctionDefinitionException(name: String, line: Int) :
    Exception("MULTIPLE FUNCTION DEFINITION $name:$line")