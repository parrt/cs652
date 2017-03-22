# Model-driven translator

In the previous [syntax-directed translation lab](https://github.com/parrt/cs652/blob/master/labs/trans.md), we built a translator that collected strings representing output phrases, one for declaration. We're going to move the abstraction level up in this lab by creating objects that represent phrases rather than strings.

## Model objects

First, copy all of the code from the previous lab into a new module or project.

Now, let's create classes that represent the various kinds of output phrases. At the root, let's place an abstract `OutputModelObject` class so that we can create data structures of generic output objects:

```java
/** A generic root for any object representing an output component */
public abstract class OutputModelObject {
	/** The method used to generate code for a specific object */
	@Override
	public String toString() { return super.toString();	}
}
```

After walking the tree with our code generator, we will have access to an object representing an output file. Our "emitter" is then just:
 
```java
System.out.println(listener.file.toString());
```

Now create the following class hierarchy:

<img src=images/trans-objects.png width=300>

* `OutputFile` has a list of `OutputModelObject` objects and its `toString()` method just returns a string with all of those objects rendered to string;each one is followed by newline. You can also add a handy method to add declarations:<br>
<pre>
public void addDecl(OutputModelObject decl) { decls.add(decl); }
</pre>
* `PrimitiveDecl` has two strings, `type` and `id`and its `toString()` simply returns "*type* *id* `;`" 
* `ObjectRefDecl` is the same really except that its `toString()` returns "*type* `*`*id* `;`"

Now that we have the appropriate output model objects, we have to alter the code generator, `Gen`, so that it does not collect a list of strings. Instead, it should

```java
public class Gen extends LangBaseListener {
	public OutputFile file;
		
	@Override
	public void enterFile(LangParser.FileContext ctx) {
		// set file field
	}
	
	@Override
	public void enterDecl(LangParser.DeclContext ctx) {
		// very much like last lab except add
		// ObjectRefDecl or PrimitiveDecl not strings
		// and add to file
	}
	
	/** Pretend we have type information */
	public boolean isClassName(String typename) {
		return Character.isUpperCase(typename.charAt(0));
	}
}
```