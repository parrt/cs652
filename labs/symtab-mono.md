# Single scope symbol table

The goal of this lab is to get your first taste of building a simple table. We are going to use the [symbol table](https://github.com/parrt/cs652/tree/master/labs/code/simple-symtab) created during our previous class. Our language, *LaLa*, is very simple and has a single scope and just variable definitions and assignments. Here is a sample (`test.lala`):
 
```
var x : int;
var y : float;
x = 1;
y = x;
```

## Parsing and test rig

Here is a suitable grammar, though you should be able to build a grammar for such an example quickly and easily at this point!

```
grammar LaLa;

prog : (var|stat)+ ;

var : 'var' ID ':' type ';' ;

type : 'int' | 'float' ;

stat : ID '=' expr ';' ;

expr : INT | ID ;

ID : [a-zA-Z_] [a-zA-Z0-9_]* ;

INT : [0-9]+ ;

WS : [ \t\n\r]+ -> channel(HIDDEN) ;
```

For your convenience, here is a main method of `TestLaLa` to get you started:

```java
ANTLRInputStream input = new ANTLRFileStream("test.lala");
LaLaLexer lexer = new LaLaLexer(input);
CommonTokenStream tokens = new CommonTokenStream(lexer);
LaLaParser parser = new LaLaParser(tokens);
ParseTree tree = parser.prog();
System.out.println(tree.toStringTree(parser));
```

As usual, remember to:
 
* Add antlr-4.6-complete.jar as a dependency of your module
* Generate the ANTLR recognizer (puts it in `gen`)
* Make sure the `gen` dir is set as a source directory for this module.
* Make sure that your working directory is set to the root of your module.

## Listener to create symbol table

First, copy the [symbol table](https://github.com/parrt/cs652/tree/master/labs/code/simple-symtab) to your `src` directory in this module.

Next, fill in the following class:

```java
public class DefSymbols extends LaLaBaseListener {
	protected BasicScope globals;
	protected Scope currentScope = null;

	@Override
	public void enterProg(LaLaParser.ProgContext ctx) {
		// create a global scope
		// set currentScope to globals;
	}

	@Override
	public void exitProg(LaLaParser.ProgContext ctx) {
		// pop the scope
	}

	@Override
	public void enterVar(LaLaParser.VarContext ctx) {
		String varName = ...
		// create VariableSymbol
		// define the current scope
	}

	@Override
	public void enterStat(LaLaParser.StatContext ctx) {
		String varName = ...;
		Symbol sym = ...; // resolve in the current scope
		if ( sym==null ) {
			System.err.println("No such var: "+varName);
		}
	}

	@Override
	public void enterExpr(LaLaParser.ExprContext ctx) {
		if ( ctx.ID()!=null ) {
			String varName = ...;
			Symbol sym = ...; // resolve varName in the current scope
			if ( sym==null ) {
				System.err.println("No such var: "+varName);
			}
		}
	}
}
```

Finally, update your main program so that it creates a `DefSymbols` listener object and walks the tree with it. Because there are no errors, you should not get an error message. If you alter the input to the following (`bad.lala`), it should print out an error for the left side of the assignment and the right-hand side of the second assignment.

```
var y : float;
x = 1;
y = x;
```

The errors are:
 
```
No such var: x
No such var: x
```