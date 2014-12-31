Java REPL
====

This project teaches you about dynamic compilation (similar to what Java server Pages (JSP) do) and class loaders. It will demonstrate that Java can be extremely dynamic. Servers use this technology all the time to reload classes without having to restart the server program. We'll build a read-eval-print loop (REPL) interface for Java code.

Goal
Your goal is to create a commandline Java interpreter that mimics the behavior of the Python interpreter. In other words you should be able to type the following, hit return, and see the result immediately:

$ java jInterp
System.out.println("Hi");
Hi
^D
$
where ^D is "control-D". That sends end-of-file to standard input on UNIX. It's ^Z on Windows.

You should print a line with a "> " prompt for the user. All commands execute upon newline.

You must allow variable definitions and function definitions as well as executable statements. We will assume that all definitions are on a single line, even function definitions.

int i = 3;
void f() { System.out.println(i); }
f();
That code should emit "3\n" after you hit return after f(); here's some more valid input:

int i = 3;
int i = i; // error
i = 4;
int f(int x) { return x*2; }
class T { int y; }
System.out.println(f(i));
new T();
Before you freak out that you have to build the Java interpreter, I'll give you the solution. We are actually going to generate a Java file when people hit return then compile it in memory, in the same VM, and then use the class loader to bring back code into memory and execute it.

We're going to construct a series of class definitions, class1..classN, one for each command the user types on the command line. Each class inherits from the previous class, except for the first one of course which has no explicit superclass. For declarations of variables and functions, we put them as members of the generated class. For statements, we put them into a method called exec(). For example, let's translate the following commands.

int i = 3;
void f() { System.out.println(i); }
f();
We'd get the following class definitions:

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
There are 2 key elements here: inheritance allows us to see previously defined symbols and the use of static variables and functions means that we don't have to create instances of objects.

Resources
I am providing for you a class called StreamVacuum that you can use to pull all of the data out of an InputStream. You'll need it to capture the standard output from executing the exec() method on your newly created classes. Look at System.setOut().  You may find this code full of useful goodies:

ANTLR v4's BaseTest.java

You will also need to learn about compilation on-the-fly:

Interface JavaCompiler
Java Compiler API
yet another example
I use this to get a java compiler to use in memory:

JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
Then, using a DiagnosticCollector and a StandardJavaFileManager, I get a CompilationTask and basket to compile by calling call(). I pull any error messages out of the DiagnosticCollector. There are lots of simple versions of compilation like this:

JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
int result = compiler.run(null, null, null,
                          "src/org/kodejava/example/tools/Hello.java");
But this doesn't deal with capturing stdout / stderr from the compiler.

Dynamic class loading:

http://tutorials.jenkov.com/java-reflection/dynamic-class-loading-reloading.html
http://docs.oracle.com/javase/6/docs/api/java/lang/ClassLoader.html
I use URLClassLoader to load the compiled class then used Class.getDeclaredMethod() to find the "exec" method.

You can cut and paste little pieces of code from around the web to make this work on this project. For example, I cut and paste 5 or 6 lines from Compile a Java file with JavaCompiler. You must indicate by a comment in the code where you have copied from the web!

Deliverables
You must deliver jInterp.java that contains a main() method that embodies the interpreter we demo class and described above.  

Submission
Make sure that you have a cs345 directory associated with your user account:

svn mkdir https://www.cs.usfca.edu/svn/YOUR_CS_STUDENT_LOGIN/cs345 -m 'add dir'
To map that directory to a directory on your disk, do something like this:

$ cd ~
$ svn co https://www.cs.usfca.edu/svn/YOUR_CS_STUDENT_LOGIN/cs345
This makes a cs345 in your home directory, but of course you can put it wherever you want on your local disk tree. I only care about the pathnames in subversion. Be careful not to create extra subdirectories that get mapped to subversion. I will pull your files exactly as shown below. Failure to put the files in the right directory means 10% off. We will be using a robot to pull and test your source code.

https://www.cs.usfca.edu/svn/YOUR_CS_STUDENT_LOGIN/cs345/jInterp
You can use the svn account for development of the software too if you would like, but I will only be looking at your jar file in the build directory.

For more information, see svn in CS601. Naturally you will have to substitute cs345 for cs601.
