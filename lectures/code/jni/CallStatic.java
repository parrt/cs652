public class CallStatic {
	static { System.loadLibrary("statichello"); }

	static native void sayHello();

	public static void main(String[] args) {
		sayHello();
	}
}
