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