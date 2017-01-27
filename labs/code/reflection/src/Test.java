import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

class Test {
	public static void main(String[] args) throws Exception {
		Test t = new Test();
		// call f()
		// call t.g() and print result
		// call t.f(8)

		URL tmpURL = new File("/tmp").toURI().toURL();
		ClassLoader loader = new URLClassLoader(new URL[]{tmpURL});
		Class cl = ...;
		Method foo = cl.getDeclaredMethod("foo");
		...
	}

	public static void f() {
		System.out.println("Hi from f()\n");
	}

	public int g() { return 99; }

	public void f(int x) {
		System.out.printf("f(%d)\n", x);
	}
}