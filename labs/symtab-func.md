# Nested scopes

The goal of this lab is to build a more sophisticated simple table that supports nested scopes. Let's extend our LaLa language to include functions and call it FaLaLa.  There are global variables but all executable statements must be within functions. Here is some sample input:

```
var x : int;
function foo(x : int, z : float) {
	 var y : float;
	 x = 1;
	 y = x;
}
function bar() { x = 9; }
```

## Parsing and test rig

Here is a suitable grammar

```
grammar FaLaLa;

prog : (var|func)+ ;

var : 'var' def ';' ;

def : ID ':' type ;

type : 'int' | 'float' ;

func : 'function' ID '(' args ')' block ;

args: def (',' def)*
	|
	;

block : '{' (var|stat)* '}' ;

stat : ID '=' expr ';' ;

expr : INT | ID ;

ID : [a-zA-Z_] [a-zA-Z0-9_]* ;
INT : [0-9]+ ;
WS : [ \t\n\r]+ -> channel(HIDDEN) ;
```

For your convenience, here is a main method of `TestLaLa` to get you started:

```java
ANTLRInputStream input = new ANTLRFileStream("test.falala");
FaLaLaLexer lexer = new FaLaLaLexer(input);
CommonTokenStream tokens = new CommonTokenStream(lexer);
FaLaLaParser parser = new FaLaLaParser(tokens);
ParseTree tree = parser.prog();
System.out.println(tree.toStringTree(parser));

ParseTreeWalker walker = new ParseTreeWalker();
DefSymbols def = new DefSymbols();
walker.walk(def, tree);
```

As usual, remember to:

* Add antlr-4.6-complete.jar as a dependency of your module
* Generate the ANTLR recognizer (puts it in `gen`)
* Make sure the `gen` dir is set as a source directory for this module.
* Make sure that your working directory is set to the root of your module.

## Listener to create symbol table

Start by copying the solution to the previous lab to a new `DefSymbols.java` file and then modify "`LaLa`" to "`FaLaLa`". Because the knew grammar uses a shared `def` rule to define both variables and arguments, we can conveniently override `enterDef` instead of both event methods for variables and arguments. Fill in the blank functions below to push and pop scopes. Also note that the function scope is comprised of two scopes: the function itself which holds the arguments and the block of code. That implies that local variables with the same name as arguments hide the argument because they aren't two different scopes, the local scope nested within the function scope.

The [symbol table](https://github.com/parrt/cs652/tree/master/labs/symtab-func/src/symtab) code, which I have augmented from the previous lab, is available.

```
protected BasicScope globals;
protected Scope currentScope = null;

// Handle scopes

@Override
public void enterProg(FaLaLaParser.ProgContext ctx) {
	globals = new BasicScope();
	currentScope = globals;
}

@Override
public void exitProg(FaLaLaParser.ProgContext ctx) {
	currentScope = currentScope.getEnclosingScope();
}

@Override
public void enterFunc(FaLaLaParser.FuncContext ctx) {
	// Define a function symbol in the current scope
	// make that function symbol the current scope
}

@Override
public void exitFunc(FaLaLaParser.FuncContext ctx) {
	// pop the scope
}

@Override
public void enterBlock(FaLaLaParser.BlockContext ctx) {
	// push a new BasicScope
}

@Override
public void exitBlock(FaLaLaParser.BlockContext ctx) {
	// pop the scope
}

// Handle defs

@Override
public void enterDef(FaLaLaParser.DefContext ctx) {
	String varName = ctx.ID().getText();
	VariableSymbol sym = new VariableSymbol(varName);
	currentScope.define(sym);
}

// Handle refs

@Override
public void enterStat(FaLaLaParser.StatContext ctx) {
	String varName = ctx.ID().getText();
	Symbol sym = currentScope.resolve(varName);
	if ( sym==null ) {
		System.err.println("No such var: "+varName);
	}
}

@Override
public void enterExpr(FaLaLaParser.ExprContext ctx) {
	if ( ctx.ID()!=null ) {
		String varName = ctx.ID().getText();
		Symbol sym = currentScope.resolve(varName);
		if ( sym==null ) {
			System.err.println("No such var: "+varName);
		}
	}
}
```	

## Annotating the parse tree

As we did in the previous lab, augment the rules for which we want to have `Scope` fields:

```
grammar FaLaLa;

@header {import symtab.*;}
...
prog returns [Scope scope] : (var|func)+ ;
...
func returns [Scope scope] : 'function' ID '(' args ')' block ;
...
block returns [Scope scope] : '{' (var|stat)* '}' ;
```

Now, go back into your listener and add
 
```java
ctx.scope = currentScope;
```

statements to the listener event methods that push new scopes. This annotates the parse tree with all of the appropriate scope pointers into the scope tree.

The next thing we will do is split symbol definitions from symbol references. Annotating the parse tree is a good first step in that direction.