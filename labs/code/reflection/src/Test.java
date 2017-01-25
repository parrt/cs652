class Test {
	public static void main(String[] args) {
		// call f()
		// call g() and print result
		// call f(8)
	}

	public void f() {
		System.out.println("Hi from f()\n");
	}

	public int g() { return 99; }

	public void f(int x) {
		System.out.printf("f(%d)\n", x);
	}
}