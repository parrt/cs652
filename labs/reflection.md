# Java reflection

There are lots of good resources on the net about using Java reflection, but the basic idea is that we can use Java code to ask questions about Java code.

As a simple exercise, let's define some simple functions and use reflection to call them the hard way.  Use `Test.class` to get the `Class` object associated with class `Test` (or `t.getClass()`) and then use `getDeclaredMethod()` to find the appropriate `Method` object.

```java
class Test {
	public static void main(String[] args) {
		Test t = new Test();
		// call f()
		// call t.g() and print result
		// call t.f(8)
	}

	public static void f() {
		System.out.println("Hi from f()\n");
	}

	public int g() { return 99; }

	public void f(int x) {
		System.out.printf("f(%d)\n", x);
	}
}
```

Now, instead of using `new Test()`, use the following to create instances of `Test`: `Class.forName` and `newInstance()`.

## ClassLoaders

Save this `T.java` file to `/tmp` or some other dir:

```java
public class T {
	public static void foo() { System.out.println("T.foo() called"); }
	public void bar() { System.out.println("T.bar() called"); }
}
```

Compile it with:

```bash
$ cd /tmp
$ javac T.java
$ ls T.*
T.class  T.java
```

In the main of a new test class such as `Test2.java`, Use a `URLClassLoader` and then `loadClass("T")` to load the `Class` associated with `T` from dir `/tmp`.

```java
URL tmpURL = new File("/tmp").toURI().toURL();
ClassLoader loader = new URLClassLoader(new URL[]{tmpURL});
Class cl = ...;
```

Once you have the class definition object, find the `foo` method and call it:

```java
Method foo = cl.getDeclaredMethod("foo");
...
```

Now, do the same for `bar` so you can call it.  Output should be:

```bash
T.foo() called
T.bar() called
```