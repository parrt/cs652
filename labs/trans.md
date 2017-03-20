# Simple translation

The goal of this lab is to build a very simple translator for a mini declarations language. Input looks like:

```
int x;
T t;
```

We will start out emitting text directly with print statements in a listener and then buffer up the strings.

## Syntax-directed translation

The simplest approach to building a translator is to print text when we see items of interest from the input. Let's get started with our grammar, which is just:

```
grammar Lang;

file : decl+ ;

decl : typename ID ';' ;

typename : 'int' | ID ;

ID : [a-zA-Z]+ ;
WS : [ \r\t\n]+ -> skip;
```

Then we need the usual main program that fires up the parser and prints out the tree:

```java
String code =
	"int x;\n" +
	"A b;\n";
ANTLRInputStream input = new ANTLRInputStream(code);
LangLexer lexer = new LangLexer(input);
CommonTokenStream tokens = new CommonTokenStream(lexer);
LangParser parser = new LangParser(tokens);
ParseTree tree = parser.file(); // start up

System.out.println(tree.toStringTree(parser));
```

On to the translator then.  We want to trigger print statements when we see certain input phrases, which we can easily do with a listener.