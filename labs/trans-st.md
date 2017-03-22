# Translation with templates

In the previous [Model-based translation lab](https://github.com/parrt/cs652/blob/master/labs/trans-objects.md), we built a translator that created an abstraction of the desired output using output model objects. Translation was a matter of constructing the appropriate model and then a final `toString` call on the root to get a string representation in the output language of interest.

One of the problems with that lab was that we used Java's relatively poor string construction facilities to construct output. In this lab, we're going to use my [StringTemplate](http://www.stringtemplate.org/) to do a better job.

## Using templates not strings

Begin by copying all of that previous code to a new module or project.  Next, let's change the root of our model hierarchy so that it knows how to return a string template (and delete the old `toString()`):

```java
import org.stringtemplate.v4.ST;

/** A generic root for any object representing an output component */
public abstract class OutputModelObject {
	public abstract ST getTemplate();
}
```

Then, each of the subclasses must implement this method. `PrimitiveDecl`'s implementation looks like this:

```java
public ST getTemplate() {
	ST t = new ST("<type> <id>;");
	t.add("type", type); // fill in the <type> whole with field type
	t.add("id", id);
	return t;
}
```	

The `<type>` notation is called an attribute reference and we use `add()` to add attribute key-value pairs to the template. These attributes are used when we ask the template to `render()`.

Do the same for `ObjectRefDecl` except of course you need `*` before the `<id>` in the template.

The template construction for file is a bit more complicated:

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

The `<decls:{d | <d>\n}>` notation indicates that the template should iterate through `decls`, calling each element `d` and inject it in the output followed by a newline. You can think of this as Python-like: *for d in decls: print d*.

The code does not add output model objects to the output file template. Instead, it gets templates from the model object and injects those into the output file template.

Finally, we need to alter the main program so that the emitter is not calling `toString()`. It should get a template from the output file object, render it, and printed out:

```java
ST output = listener.file.getTemplate();
System.out.println(output.render());
```

This then represents our emitter, the final stage of our translator.

**Something important to notice:** Our code generator created a hierarchy of output model objects. Using `getTemplate()` methods, it returns a hierarchy of templates--in other words, an output tree. When we call `render()` on the root of that output tree, ST will descend the tree and build up an output string bottom-up.
