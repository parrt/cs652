# Translation with more advanced models and templates

In the [previous lab](https://github.com/parrt/cs652/blob/master/labs/trans-magic.md), we moved to a visitor from a listener and learn to use the magic `ModelConverter` that automatically converts a model hierarchy to a template hierarchy.

In this lab, we are going to extend the language and model to learn more about how templates can access model data. I'm going to drag you through all of the false starts and changes necessary to make this translator work effectively.

## Adding arguments to functions

Let's augment our language to allow arguments on functions:

```
fun : 'fun' ID '(' (arg (',' arg)*)? ')' '{' decl+ '}' ;
arg : typename ID ;
```

If we run with function:
 
```
fun f(int x, B b) { int y; T t; }
```

we get a function definition without the arguments:

```C
void f() {
	int y;
	T *t;

}
```

Oops.  At this point, we need to alter the code generator so that it creates model objects for the arguments and adds them to the `Function` model object. Add the following field to `Function` and noticed that it is tagged as a model element so that the model converter will know to descend into the `args` list, creating a matching list of templates.

```java
@ModelElement
public List<OutputModelObject> args = new ArrayList<>();
```

That means we have to update the `Function` template to take in another argument: `args`:

```
Function(model, args, decls) ::= <<...>>
```
 
Let's add some helper methods (`add` is renamed to `addDecl`):

```java
public void addDecl(OutputModelObject decl) { decls.add(decl); }

public void addArg(OutputModelObject decl) { args.add(decl); }
```

Now, let's update the code generator so that it walks the list of arguments and adds objects to the `args` list of the function:

```java
public OutputModelObject visitFun(LangParser.FunContext ctx) {
	Function function = new Function(ctx.ID().getText());
	for (LangParser.ArgContext arg : ctx.arg()) {
		OutputModelObject m = visit(arg);
		function.addArg(m);
	}
	for (LangParser.DeclContext decl : ctx.decl()) {
	...
}
```

If we test it now, we still get the same output:

```C
void f() {
	int y;
	T *t;

}
```

Oh right.  We have to alter the `Function` template so that it refers to the new `args` template argument:

```
void <model.id>(<args; separator=",">) {
```

That `<args; separator=",">` notation says to emit the arguments in between the parentheses, if any exists, and use a comma as a separator between the elements. Unfortunately the output is not quite right:

```c
void f(int x;,B *b;) {
	int y;
	T *t;

}
```

We need to get rid of the semicolons on the templates for declarations since we are using those templates for both variable declarations and argument declarations:

```
ObjectRefDecl(model) ::= "<model.type> *<model.id>"
```

To get the semicolons back, we need to add them to all the places where they are needed as part of a variable declaration. For functions, its easy. We just add a semicolon to the template expression: `<decls:{d | <d>;<\n>}>`. Now, with input

```
int x;
A b;
fun f(int x, B b) { int y; T t; }
```

We get almost the right thing:

```c
#include <stdio.h>
int x
A *b

void f(int x,B *b) {
	int y;
	T *t;

}
```

We are still missing the semicolons on the end of the global variable declarations. To solve this, we need to add semicolons on this template expression from `OutputFile`: `<elements:{d | <d><\n>}>`. Unfortunately, that would put a semicolon on the end of function declarations as well. You might be tempted to put an IF statement in the template to check whether the element is a function or not, but that would make the template part of the model logic. That's not a good idea. Instead, we augment the model to separate functions and declarations because we need to treat them differently:

```java
public class OutputFile extends OutputModelObject {
	@ModelElement
	public List<OutputModelObject> decls = new ArrayList<>();
	@ModelElement
	public List<OutputModelObject> functions = new ArrayList<>();

	public void addDecl(OutputModelObject decl) { decls.add(decl); }
	public void addFun(OutputModelObject fun) { functions.add(fun); }
}
```

That change, in turn, causes a change in the template requirements. we have added a new model element field, which means we need to add a new template argument to `OutputFile` (and rename the old one from `elements` to `decls`). Also reference the functions argument so that the template emits the functions after the declarations:

```
OutputFile(model,functions,decls) ::= <<
#include \<stdio.h>
<decls:{d | <d>;<\n>}>
<functions>
>>
```

Now we can add the logic to distinguish between functions and declarations in the output file model (rather than in the template). Here is the loop in the code generator in `visitFile()`:

```java
for (ParseTree child : ctx.children) {
	OutputModelObject m = visit(child);
	if ( m instanceof Function ) {
		file.addFun(m);
	}
	else {
		file.addDecl(m);
	}
}
```	

Testing it again, we get the correct output:
 
```c
#include <stdio.h>
int x;
A *b;

void f(int x,B *b) {
	int y;
	T *t;

}
```

## Declaring functions

C likes to see declarations of functions before they are used. We don't have function calls, but we can still learn something about StringTemplate by declaring functions as well as defining them. Let's add another function to our input:

```
int x;
A b;
fun f(int x, B b) { int y; T t; }
fun g(A a) { C c; }
```

The output we'd like is the following:

```c
#include <stdio.h>
int x;
A *b;

extern void f(int x,B *b);
extern void g(A *a);

void f(int x,B *b) {
	int y;
	T *t;

}
void g(A *a) {
	C *c;

}
```

But we get these function declarations instead:

```c
extern void f();
extern void g();
```

The first thing to try is to iterate over the `functions` templates twice:

```
OutputFile(model,functions,decls) ::= <<
#include \<stdio.h>
<decls:{d | <d>;<\n>}>
<functions:{f | extern void <f.model.id>();<\n>}>
<functions>
>>
```

The `f` lambda argument is a `Function` template not a `Function` model object. If you want that, we can iterate over `<model.functions:{f | ...}>`. It would work if all we needed was the name, but we actually need the arguments to those functions as well to get a proper declaration. We need to refer to the `args` attribute of the `Function` template:

```
<functions:{f | extern void <f.model.id>(<f.args; separator=",">);<\n>}>
```

Then, we get the proper declarations out:

```c
extern void f(int x,B *b);
extern void g(A *a);
```

Notice that we are free to walk any of the attributes or templates multiple times to get the output we desire. If we want to completely reorganize the output so that the functions come first and then the global variable definitions, it's a simple matter of moving the `<decls:{d | <d>;<\n>}>` expression to the end of the `OutputFile` template.

## Misc

It bugs me that there is a blank line at the end of every function we generate:
 
```c
void g(A *a) {
	C *c;

}
```

This comes from the `<decls:{d | <d>;<\n>}>` expression from the `Function` template, which puts a newline after every declaration. We also have a newline after that expression before the `}`:

```
void <model.id>(<args; separator=",">) {
	<decls:{d | <d>;<\n>}>
}
```

All we have to do is use the `separator` option on the expression and get what we want:

```
<decls:{d | <d>;}; separator="\n">
```

The output is then:

```c
void g(A *a) {
	C *c;
}
```