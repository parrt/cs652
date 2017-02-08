# Parse tree listener

In this lab, you will learn the basics of parse tree listeners. It builds on the previous lab where we integrated ANTLR-generated parsers and lexers into a Java program.

This lab is derived from section 7.2 in the ANTLR book. You can look at that when you get stuck doing this lab.

## Getting started

Create a new project using the same structure from the previous lab and make a `PropertyFile.g4` file in the `grammars` directory. Here is the core, of course you need to add the grammar header and definitions of those tokens:

```
file : prop+ ;
prop : ID '=' STRING '\n' ;
```

Now ask ANTLR to generate the parser/lexer by right clicking in the grammar and saying "Generate recognizer". Then look in the `gen` directory at the root of your project and you will see `PropertyFileListener` and `PropertyFileBaseListener`. Now tell intellij to treat that directory like source code.

Add `antlr-4.6-complete.jar` as a dependency for the module and try to build.

Copy and paste the main program from the previous lab and then change `CDecl` to `PropertyFile` to reflect the new grammar:

<img src=images/paste-tweak.png width=500>

You should end up with something like
 
```java
String prop = "id=\"parrt\"\n";
ANTLRInputStream input = new ANTLRInputStream(prop);
PropertyFileLexer lexer = new PropertyFileLexer(input);
CommonTokenStream tokens = new CommonTokenStream(lexer);
PropertyFileParser parser = new PropertyFileParser(tokens);
ParseTree tree = parser.file();
System.out.println(tree.toStringTree(parser));
Trees.inspect(tree, parser);
```

Run that and you should get output `(file (prop id = "parrt" \n))` as well as a dialog box showing the parse tree.

## Listener action

Okay, now that we have a valid parser and we've pulled out the tree, let's walk the tree and perform some actions.

Create a new class called `PropertyFileLoader` that we will use to fill a `Map` with properties.

<img src=images/prop-loader.png width=500>

Now modify the main program so that it creates a tree walker and an instance of that property followed her then triggers a walk using `walk()`:

```java
ParseTreeWalker walker = new ParseTreeWalker();
PropertyFileLoader loader = new PropertyFileLoader();
walker.walk(loader, tree);
```

This will fill the `loader.props` field, which we can then print out in the main program:

```java
System.out.println(loader.props);
```

You should get output:

```
{id="parrt"}
```