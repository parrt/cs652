# Parse Tree construction in an LL(1) Parser

## Goal

In this lab, you will add parse tree construction to the LL(1) parser you build in the previous lab.  I have provided the tree node infrastructure for you, leaving just the details of instructing the trees to you. You're free to use any resource, such as my Language Implementation Patterns book.

[Here is your lab starter kit](https://github.com/parrt/cs652/tree/master/labs/code/LL1WithTree/src).

## Tasks

As you can see from the methods of `ExprParser.java`, each rule must now return a `ParseTree` subtree. The code now also prints out the tree in LISP format:

```java
ParseTree tree = parser.expr();
System.out.println(tree.toStringTree());
System.out.println("OK");
```