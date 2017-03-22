# Translation with templates (Part 2)

In the previous [Translation with templates lab](https://github.com/parrt/cs652/blob/master/labs/trans-st.md), we started using string templates (`ST`s) instead of Java's native strings to construct output. It works well, but we can simplify it.

## Templates that use model objects

Take a look at the definition of our primitive declaration object:

```java
public class PrimitiveDecl extends OutputModelObject {
	public String type;
	public String id;

	public PrimitiveDecl(String type, String id) {
		this.type = type;
		this.id = id;
	}

	public ST getTemplate() {
		ST t = new ST("<type> <id>;");
		t.add("type", type);
		t.add("id", id);
		return t;
	}
}
```

Instead of injecting attributes with specific names, we can simply inject the entire output model object! Then, the template can reference fields (or methods with restrictions) from the model object. This makes our Java code that creates the template hierarchy more generic:

```java
public ST getTemplate() {
	ST t = new ST("<model.type> <model.id>;");
	t.add("model", this);
	return t;
}
```

`ObjectRefDecl`'s would look nearly identical:

```java
public ST getTemplate() {
	ST t = new ST("<model.type> *<model.id>;");
	t.add("model", this);
	return t;
}
```

Now, they differ only in the actual template.

The method for `OutputFile` has to stay the same because we have to manually inject templates rather than just model objects for the declaration list of the output file:

```java
public ST getTemplate() {
	ST t = new ST("<decls:{d | <d>\n}>");
	for (OutputModelObject decl : decls) {
		// convert objects to templates and add to output file template
		t.add("decls", decl.getTemplate());
	}
	return t;
}
```

Run your main program to verify you get the same output as before.

## ST group files

The biggest remaining problem with our translator design is that we still have an entangled model and view. In other words, output literals (text) mixed in with the model construction. ST provides `STGroup` files so that we can move everything outside of code and put it inside a separate file.

Make a `resources` directory and create a `C.stg` file:

```
OutputFile(model,decls) ::= <<
#include \<stdio.h>
<decls:{d | <d><\n>}>
>>

ObjectRefDecl(model) ::= "<model.type> *<model.id>;"

PrimitiveDecl(model) ::= "<model.type> <model.id>;"
```

To load those templates and make them available to our code generator, we can add the following declaration to `Gen`:

```java
public static STGroup templates = new STGroupFile("C.stg");
```

Now, instead of creating a string template from a string literal with `new ST("...")`, we ask for an instance from the group using a template name: `templates.getInstanceOf("templatename")`.

```
ST st = Gen.templates.getInstanceOf(className);
```

Modify the `getTemplate()` method of `PrimitiveDecl` like this:

```java
	public ST getTemplate() {
		ST t = Gen.templates.getInstanceOf("PrimitiveDecl");
		t.add("model", this);
		return t;
	}
```

The method in `ObjectRefDecl` would be the same except it creates an instance of template `ObjectRefDecl`.  The method for `OutputFile` looks like:

```java
public ST getTemplate() {
	ST t = Gen.templates.getInstanceOf("OutputFile");
	for (OutputModelObject decl : decls) {
		// convert objects to templates and add to output file template
		t.add("decls", decl.getTemplate());
	}
	return t;
}
```

In case it hasn't jumped out at you, notice that the template names mirror the object model names. Because of that, we can factor out the "get instance" portion of `getTemplate()` completely from all model objects and put it in `OutputModelObject`:

```java
public ST getTemplate() {
    String className = getClass().getSimpleName();
    ST st = Gen.templates.getInstanceOf(className);
    st.add("model", this);
    return st;
}
```

The method disappears entirely from `ObjectRefDecl` and `PrimitiveDecl` but `OutputFile` must still create the ST objects from model objects for each declaration:

```java
public ST getTemplate() {
	ST t = super.getTemplate();
	for (OutputModelObject decl : decls) {
		// convert objects to templates and add to output file template
		t.add("decls", decl.getTemplate());
	}
	return t;
}
```

Run your main program to verify you get the same output as before.

## Generating multiple output languages

Because we have moved all output strings to a separate file, away from the code, we have properly separated model and view. If you want to generate code for a different language, you can easily just switch out the template group for another. For example, here is one that does things a little bit differently:

```
OutputFile(model,decls) ::= <<
#include \<stdio.h>
// this is the debug version
<decls>
>>

ObjectRefDecl(model) ::= "<model.type> *<model.id>; // found on line ...<\n>"

PrimitiveDecl(model) ::= "<model.type> <model.id>; // found on line ...<\n>"
```

Alter the `Gen` code generator to load the different template file, `CDbg.stg`:

```
public static STGroup templates = new STGroupFile("CDbg.stg");
```

The output now looks like:

```
(file (decl (typename int) x ;) (decl (typename A) b ;))
#include <stdio.h>
// this is the debug version
int x; // found on line ...
A *b; // found on line ...
```

Amazing, right? :)