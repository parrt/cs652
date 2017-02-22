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

