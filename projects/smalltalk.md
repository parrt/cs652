# Smalltalk (*subset*) Compiler and Interpreter

## Goal

This project is to build a full compiler and interpreter / virtual machine (VM) for a subset of Smalltalk.



## Discussion

### Smalltalk language definition

no class variables but allows class methods
we disallow globals. x:=expr will generate code for expr but not the store if x is not a valid argument, local variable, or field. no ';' extended msg send notation, no `#(1 2 3)` array literal notation, but with dynamic array notation `{1. 2. 3}`. Much of the implementation is not exposed to the programmer, such as method invocation contexts.

| Syntax | Semantics |
|--------:|--------|
|  `nil` | undefined object |
|`true,false`|boolean liberals|
|`self`||
|`super`||

| Syntax | Semantics |
|---------:|--------|
|`"..."`|comment (allowed anywhere)|
|`'abc'`|string literal|
|`$a`|character literal `a`|
|`123`|integer literal|
|`1.23`|floating-point literal (single precision), no scientific notation|
|`.`|expression separator (not terminator)|
|`x := `*expr*|assignment to local or field (there are no global variables)|
|`^`*expr*|return expression from method, even when nested in a `[...]`block|
|`|x y|`|define two local variables or fields|
|`{a . 1+2 . aList size}`|dynamic array constructed from three expressions separated by periods|
|`[:x | 2*x]`|code block taking one parameter and evaluating to twice that parameter; in common usage, of these are called lambdas or closures.|
|`[:x :y | x*y]`|code block taking two parameters|


## Tasks
