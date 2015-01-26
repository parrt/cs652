Java REPL
====

This project teaches you about dynamic compilation, class loaders, and simple lexical analysis (matching nested structures).  The project demonstrates that Java can be extremely dynamic. Servers use this technology all the time to reload classes without having to restart the server program. We'll build a read-eval-print loop (REPL) interface for Java code similar to what you can use for Python.

![java repl](images/repl.png)

## Goal

Your goal is to create an interactive "shell" tool that mimics the behavior of the Python interpreter shell. In other words you should be able to type the following, hit return, and see the result immediately:

```bash
$ java JavaREPL
> System.out.println("Hi");
Hi
^D
$
```

where ^D is "control-D". That sends end-of-file to standard input on UNIX. It's ^Z on Windows.

You should print a line with a "> " prompt for the user.

One of the tricky bits is figuring out when the user has finished typing a complete declaration or statement. For example, we would not want to have to type complete functions all on a single line. You'll notice that Python tracks the nesting level of parentheses, brackets, etc. and executes whenever it sees a newline and it is not nested or comment. Your should behave in a similar fashion:

```java
> // do nothing
> void f() {
    System.out.println("hi");
}
> f();
hi
> print (
3
+
4
);
7
> 
```

You do not have to worry about `/*...*/` comments, just the line comments that start with `//`.

You must allow variable definitions, function definitions, and class definitions as well as executable statements:

```java
> class T {
	int y;
	void f() {
		System.out.println("T:f");
	}
}
> T t = new T();
> t.f();
T:f
> 
```

Pretty cool, eh?!

Assume that the code entered by the user is restricted to use classes in:

```java
import java.io.*;
import java.util.*;
```

Before you freak out that you have to build the Java interpreter, my solution is less than 200 lines of code for the core functionality. Then, I have 100 lines of code for the scanner that figures out when the user has completed a statement or function or whatever.

For simplicity, I will use the term *line* to mean whatever complete declaration or statement the user types in from here on.

## How to build interactive shell

Here's the trick you need to make this work without having to do much. When the user enters a statement or declaration (*line*), generate a class definition with that line and then compile it using Java's built-in compiler API; see packages `javax.tools.*`, `com.sun.source.util.JavacTask`. Once you compile the class just conjured up, use the `ClassLoader` to bring back code into memory and execute it.

Each line results in a new class definition, such as `class` name Interp_*i*. Each class inherits from the previous class, except for the first one of course which has no explicit superclass. For declarations, such as variables and functions, make them `static` members of the generated class. Put statements into a method some called such as `exec()`. For example, let's translate the following commands.

```java
int i = 3;
void f() { System.out.println(i); }
f();
```

We'd get the following class definitions:

```java
import java.io.*;
import java.util.*;
public class Interp_0 {
    public static int i = 3;
    public static void exec() {
    }
}
import java.io.*;
import java.util.*;
public class Interp_1 extends Interp_0 {
    public static void f() { System.out.println(i); }
    public static void exec() {
    }
}
import java.io.*;
import java.util.*;
public class Interp_2 extends Interp_1 {
    public static void exec() {
        f();
    }
}
```

There are 3 key elements here:

1. Inheritance allows us to see previously defined symbols.
2. The use of `static` variables and functions means that we don't have to create instances of objects.
3. We inject the user line in the proper place within the generated class depending on whether it is a declaration or an executable statement.

To distinguish between declarations and statements, we need a final trick.  Using the Java compiler API, try to parse the input line as a declaration. If it fails to parse, then it is either a syntax error or a statement. We assume it's a valid statement and then simply try to compile the line as a statement within the generated `exec()`. For example, if the user enters a print statement, try to parse it as a declaration:

```java
import java.io.*;
import java.util.*;
public class Bogus {
	public static System.err.println("hi");
	public static void exec() {
	}
}
```

(The name `Bogus` is just what I happen to use when generating code in an effort to distinguish between statements and declarations.)

The compiler will puke on this input obviously and your program should then try to compile it as a statement:

```java
import java.io.*;
import java.util.*;
public class Interp_0 {
	public static void exec() {
		System.err.println("hi");
	}
}
```

Finally, before you try to and analyze the Java code and executed, translate statements such as `print` *expr*`;` to `System.out.println(`*expr*`);`

###  Handling stderr and stdout

For invalid Java, or at least what we can't handle, the compiler will generate errors to `stderr`. The Java compiler API does not emit errors automatically to standard error so you must collect these messages and emit them yourself to `stderr`. Here are some examples (notice that the errors are not necessarily intuitive because of the way we generate classes with the user input.):

![repl errors](images/repl-compile-errors.png)

That colorization comes from the fact that I'm running it within the intellij console. From the regular command line running `java JavaREPL`, you would not see such colorization.

If there is an error during execution, the Java virtual machine will emit errors to `stderr` automatically and you don't have to do anything.

![repl errors](images/repl-errors.png)

The Java code entered by the user might also generate `stderr` or `stdout`. You have to make sure that this output is still sent to the user. Fortunately, you don't have to do anything to make that happen. Because we are operating within the same process, and indeed the same thread, standard streams will go to their usual places. For example, you might see something like this:

![repl errors](images/repl-stderr.png)

# Resources

I am providing for you a class called StreamVacuum that you can use to pull all of the data out of an InputStream. You'll need it to capture the standard output from executing the exec() method on your newly created classes. Look at System.setOut().  You may find this code full of useful goodies:

ANTLR v4's BaseTest.java

You will also need to learn about compilation on-the-fly:

[Interface JavaCompiler](http://docs.oracle.com/javase/6/docs/api/javax/tools/JavaCompiler.html)
[Java Compiler API](http://www.javabeat.net/articles/73-the-java-60-compiler-api-1.html)
[yet another example](http://www.accordess.com/wpblog/an-overview-of-java-compilation-api-jsr-199/)

I use this to get a java compiler to use in memory:

```java
JavaCompiler compiler =
	ToolProvider.getSystemJavaCompiler();
```

Then, using a DiagnosticCollector and a StandardJavaFileManager, I get a CompilationTask and basket to compile by calling call(). I pull any error messages out of the DiagnosticCollector. There are lots of simple versions of compilation like this:

```java
JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
int result = compiler.run(null, null, null,
"src/org/kodejava/example/tools/Hello.java");
```

But this doesn't deal with capturing stdout / stderr from the compiler.

Dynamic class loading:

http://tutorials.jenkov.com/java-reflection/dynamic-class-loading-reloading.html
http://docs.oracle.com/javase/6/docs/api/java/lang/ClassLoader.html
I use URLClassLoader to load the compiled class then used Class.getDeclaredMethod() to find the "exec" method.

*You can cut and paste little pieces of code from around the web to make this work on this project.* For example, I cut and paste 5 or 6 lines from [Compile a Java file with JavaCompiler](http://www.java2s.com/Code/Java/JDK-6/CompileaJavafilewithJavaCompiler.htm). You must indicate by a comment in the code where you have copied from the web!

# Deliverables

You must deliver JavaREPL.java that contains a main() method that embodies the interpreter we demo class and described above.  

# Submission

Make sure that you have a cs345 directory associated with your user account:

svn mkdir https://www.cs.usfca.edu/svn/YOUR_CS_STUDENT_LOGIN/cs345 -m 'add dir'
To map that directory to a directory on your disk, do something like this:

$ cd ~
$ svn co https://www.cs.usfca.edu/svn/YOUR_CS_STUDENT_LOGIN/cs345
This makes a cs345 in your home directory, but of course you can put it wherever you want on your local disk tree. I only care about the pathnames in subversion. Be careful not to create extra subdirectories that get mapped to subversion. I will pull your files exactly as shown below. Failure to put the files in the right directory means 10% off. We will be using a robot to pull and test your source code.

https://www.cs.usfca.edu/svn/YOUR_CS_STUDENT_LOGIN/cs345/JavaREPL
You can use the svn account for development of the software too if you would like, but I will only be looking at your jar file in the build directory.

For more information, see svn in CS601. Naturally you will have to substitute cs345 for cs601.
