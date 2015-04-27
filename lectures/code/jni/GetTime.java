public class GetTime {
	static {
		// Load native library dynamically
		System.loadLibrary("gettime"); 
	}

	public native String getTime();

	public static void main(String[] args) {
		System.out.println(new GetTime().getTime());
	}
}
