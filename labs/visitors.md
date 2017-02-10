# Parse tree visitors

In this lab, we are going to implement a visitor and demonstrate how it differs from a listener. A listener does not have to visit its children because the parse tree walker automatically does the traversal. In the case of a visitor, however, the visit methods must visit the children explicitly or the traversal will not continue into the children.

Our goal is to build a simple calculator that evaluates an expression and prints the result. `1+2*3` should print out `7`.

## Simple version

Get a new module or project together and enter the following grammar (*no Java packages unless you really know what you're doing*):
 
```
grammar Expr;

s : e ; // <-- start rule
e : e '*' e
  | e '+' e
  | INT	
  ;

INT : [0-9]+ ;
WS : [ \r\t\n]+ -> skip ;
```

Then create a main program to parse and input string; probably a good idea just to cut and paste to the main program from the listener lab and cut out the listener code.

Verify that you can print out a tree properly. For example, input `1+2*3` should print out:

```
(s (e (e 1) + (e (e 2) * (e 3))))
```

Now, create a file with the following visitor:

```java
public class ExprEval extends ExprBaseVisitor<Integer> {
	@Override
	public Integer visitS(ExprParser.SContext ctx) {
		return super.visitS(ctx);
	}

	@Override
	public Integer visitE(ExprParser.EContext ctx) {
		if ( ctx.INT()!=null ) {
			return Integer.valueOf(ctx.INT().getText());
		}
		ExprParser.EContext left = ctx.e(0);
		ExprParser.EContext right = ctx.e(1);
		int vleft = visit(left);
		int vright = visit(right);
		if ( ctx.MULT()!=null ) {
			return vleft * vright;
		}
		else if ( ctx.ADD()!=null ) {
			return vleft + vright;
		}
		return null; // will not occur for valid input
	}
}
```

Notice that it extends the `ExprBaseVisitor` class created by ANTLR. It also specifies that the return type from every visit method is `Integer`. This is because we want to evaluate and return values.

Incorporate the following code into your main program to create one of those visitors, trigger a visit, and print the result:

```java
ExprEval visitor = new ExprEval();
int v = visitor.visit(tree);
System.out.println(v);
```

A key thing to notice here is that the result is from the top level visit method, rather than then from a field in the visitor. That is what we did in the listener because listeners cannot return values from the various enter and exit methods.

## With alternative labels

Anytime you see if-then-else type code in the visitor or listener methods that test various pieces of the tree, you are probably "parsing". We should leave all of that parsing to the parser and not have to repeated in the visitor code.

Alter the grammar so there our alternative labels for the expression rule:

```
e : e '*' e	# Mult
  | e '+' e	# Add
  | INT		# Number
  ;
```

Regenerate the parser. Now, alter the visitor so it looks like the following:

```java
public class ExprEval extends ExprBaseVisitor<Integer> {
	@Override
	public Integer visitMult(ExprParser.MultContext ctx) {
		return visit(ctx.e(0)) * visit(ctx.e(1));
	}

	@Override
	public Integer visitAdd(ExprParser.AddContext ctx) {
		return visit(ctx.e(0)) + visit(ctx.e(1));
	}

	@Override
	public Integer visitNumber(ExprParser.NumberContext ctx) {
		return Integer.valueOf(ctx.INT().getText());
	}
}
```

Notice that there is now a very specific method that identifies which alternative of the expression was matched by the parser. Our code in the visitor can be much simpler because each visitor method knows precisely what operation to perform. There are no if-then-else!
