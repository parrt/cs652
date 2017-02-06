# Calling ANTLR from Java code

The goal of this lab is to learn to have antlr generate code and invoke it from Java. At this point, all we have done is used the ANTLR interpreter from within intellij.
 
1. Create an intellij project; call it whatever you want.
1. To make this project work, we need to [download the antlr-4.6-complete.jar](http://www.antlr.org/download/antlr-4.6-complete.jar). I suggest storing it in `/usr/local/lib` if you are on a Mac or on UNIX. On a Windows machine, you can simply store it in the root directory of your project. Now, tell intellij to include that as a dependency.
<img src=images/antlrlib.png width=500>
1. Download [CDecl.g4](https://github.com/USF-CS652-starterkits/parrt-cdecl/blob/master/grammars/cs652/cdecl/CDecl.g4) that you will be using for your next project; save it in a `grammars` directory that you create in the project root directory. You will not be using a package so don't put it in a subdirectory below `grammars`.
1. From intellij, open the grammar file. Right-click in the editor and choose the "Generate recognizer" option, which will trigger ANTLR to generate code in the `gen` directory under the root of the project.  Go into the project settings and tell it that `gen` is a source directory:
<img src=images/setsrc.png width=500>
1. Create a `src` directory in the project root directory and create a main program called `Test`.
1. Enter Java code that creates an [ANTLRInputStream](http://www.antlr.org/api/Java/org/antlr/v4/runtime/ANTLRInputStream.html) reading from a string, such as "`int *;`". Create a `CDeclLexer` attached to that input stream and then a `CommonTokenStream` attached to the lexer. Create a `CDeclParser` object attached to the token stream. Call start symbol `declaration` and save the return value as a parse tree.
1. Enter code to the tree out in LISP form using `toStringTree(Parser)` on the result tree. Run the program and for `int *i;` it should print:
"`(declaration (typename int) (declarator * (declarator i)) ;)`"
1.  Enter code to open a dialog box showing the tree visually using `Trees.inspect(Tree, parser)`. Running should produce this dialog box:
<img src=images/cdecl.png width=300>