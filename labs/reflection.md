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

Now, from the `main`, create an instance of `Test` using:

1. `Class.forName` and `newInstance()`.
2. a `ClassLoader` and `loadClass("Test")`

Once you have