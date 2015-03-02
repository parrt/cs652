# Parse Tree construction in an LL(1) Parser

## Goal

In this lab, you will add parse tree construction to the LL(1) parser you build in the previous lab.  I have provided the tree node infrastructure for you, leaving just the details of instructing the trees to you. You're free to use any resource, such as my Language Implementation Patterns book. Because you are adding tree construction and not altering the language, you do not need to modify the lexer at all that you created in the previous lab.

[Here is your lab starter kit](https://github. Icom/parrt/cs652/tree/master/labs/code/LL1WithTree/src).

## Tasks

As you can see from the methods of `ExprParser.java`, each rule must now return a `ParseTree` subtree:

```java
/**  expr : term ('+' term)* ; */
public ParseTree expr() {
	ParseTree t;
	ParseTree root = new RuleNode("expr");
	t = term();
	root.addChild(t);
	...
	return root;
}
```

The `main` code now also prints out the tree in LISP format:

```java
ParseTree tree = parser.expr();
System.out.println(tree.toStringTree());
System.out.println("OK");
```

Here is a sample run:

```bash
$ java Expr
1
(expr (term (factor 1)))
OK
$ java Expr
1+2
(expr (term (factor 1)) + (term (factor 2)))
OK
$ java Expr
1+2+3
(expr (term (factor 1)) + (term (factor 2)) + (term (factor 3)))
OK
$ java Expr
1+2*3
(expr (term (factor 1)) + (term (factor 2) * (factor 3)))
OK
$ java Expr
(1+2)*3
(expr (term (factor ( (expr (term (factor 1)) + (term (factor 2))) )) * (factor 3)))
OK
```