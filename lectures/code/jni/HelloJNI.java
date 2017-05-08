public class HelloJNI {
	static {
		// Load native library dynamically
		System.loadLibrary("hello"); 
	}

	native void sayHello();

	public static void main(String[] args) {
		HelloJNI hello = new HelloJNI();
		hello.sayHello();
	}
}
