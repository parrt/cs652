# Automatic string template tree construction

In our [previous lab](https://github.com/parrt/cs652/blob/master/labs/trans-st2.md), we used simple string templates to generate output.  The only ugly thing was that we had to have nontrivial `getTemplate()` methods that walked the `decls` field to create templates for the nested output model objects.

In this lab, I'm going to demonstrate how the magic `ModelConverter` in your `vtable` project can be used to automatically perform model to string template conversions.

##  Using visitors

To make things more interesting, let's add another construct to our language: functions like:

```
fun f() { int y; T t; }
```

The grammar looks like:

```
grammar Lang;

file : (fun|decl)+ ;                   // add fun!

fun : 'fun' ID '(' ')' '{' decl+ '}' ; // new stuff

decl : typename ID ';' ;

typename : 'int' | ID ;

ID : [a-zA-Z]+ ;
WS : [ \r\t\n]+ -> skip;
```

At this point it's a good idea to shift away from the simple listener mechanism to a visitor pattern so that we can use the return values from the various visit methods. The header of the class now looks like:

```java
public class Gen extends LangBaseVisitor<OutputModelObject> {...}
```

Instead of filling a `file` field in `enterDecl`, we return the appropriate object:

```java
@Override
public OutputModelObject visitDecl(LangParser.DeclContext ctx) {
	String typename = ctx.typename().getText();
	String varname = ctx.ID().getText();
	if ( isClassName(typename) ) {
		return new ObjectRefDecl(typename, varname);
	}
	else {
		return new PrimitiveDecl(typename, varname);
	}
}
```

And then we have the visitor for the file collected the results of visiting the children, which are either functions or declarations:

```java
@Override
public OutputModelObject visitFile(LangParser.FileContext ctx) {
	OutputFile file = new OutputFile();
	for (ParseTree child : ctx.children) {
		OutputModelObject m = visit(child);
		file.add(m);
	}
	return file;
}
```

Now, create an appropriate `visitFun` method that returns a new `Function` model object. Don't forget to have this method walk the `decl` children and fill a list in the function object:

```java
public class Function extends OutputModelObject {
	public String id;
	public List<OutputModelObject> decls = new ArrayList<>();
	...
}
```

Because an output file now has both declarations and functions, I've changed my model object field and method names slightly:
 
```java
public class OutputFile extends OutputModelObject {
	public List<OutputModelObject> elements = new ArrayList<>();

	public void add(OutputModelObject decl) { elements.add(decl); }
	...
}
```

Because of that change, we also should change the templates (renaming `decl` to `elements`):

```
OutputFile(model,elements) ::= <<
#include \<stdio.h>
<elements:{d | <d><\n>}>
>>
```

And, of course, we need a new template with the same name as the new `Function` model object:

```
Function(model, decls) ::= <<
void <model.id>() {
	<decls:{d | <d><\n>}>
}
>>
```

Because we have a visitor and not a listener now, we have to alter the main program:

```java
Gen gen = new Gen();
OutputModelObject file = gen.visit(tree);

ST output = file.getTemplate();
System.out.println(output.render());
```

Notice how we use the object coming back from the `visit` method as the result of translation.

## Automatic model conversion

The `getTemplate()` method in our model objects triggers the conversion of the model to a tree of templates (nested templates). But, we have some nontrivial methods to write. Not only that, they look almost identical. Compare the following to methods for converting the overall file and function model objects:

```java
// OutputFile model object
public ST getTemplate() {
	ST t = super.getTemplate();
	for (OutputModelObject el : elements) {
		// convert objects to templates and add to output file template
		t.add("elements", el.getTemplate());
	}
	return t;
}
```

```java
// Function model object
public ST getTemplate() {
	ST t = super.getTemplate();
	for (OutputModelObject decl : decls) {
		// convert objects to templates and add to output file template
		t.add("decls", decl.getTemplate());
	}
	return t;
}
```

The pattern is "*convert list of model objects to list of templates*", which of course we can automate by using Java reflection. All we need is a list of the fields in a particular model object, and we can automatically perform the conversion.

Naturally, we can only convert fields that are of the appropriate type, namely `OutputModelObject`. We have to filter the fields. It's also the case that we might want to track pointers to model objects without having them converted. To make it easier to identify which fields require conversion to templates, I created a Java annotation called [ModelElement](https://github.com/antlr/antlr4/blob/master/tool/src/org/antlr/v4/codegen/model/ModelElement.java) (used in ANTLR itself).

Alter the `Function` model object so that `decls` is tagged with the annotation:

```java
public class Function extends OutputModelObject {
	public String id;

	@ModelElement
	public List<OutputModelObject> decls = new ArrayList<>();
	...
}
```

Did the same for `OutputFile`.

Then, delete `getTemplate()` from all model objects. We don't even need the generic version from `OutputModelObject`:

```java
public ST getTemplate() {
	String className = getClass().getSimpleName();
	ST st = Gen.templates.getInstanceOf(className);
	st.add("model", this);
	return st;
}
```

The `ModelConverter` will take care of everything. Speaking of which, to employ the *magic*, replace this in `Trans`:

```java
ST output = file.getTemplate();
System.out.println(output.render());
```

with:

```java
ModelConverter converter = new ModelConverter(Gen.templates);
ST output = converter.walk(file);
System.out.println(output.render());
```

*Voila!* The model is converted automatically to a nested template object which we can render and print out.