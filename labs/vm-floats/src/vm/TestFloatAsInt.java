package vm;

public class TestFloatAsInt {
	public static void main(String[] args) {
		float x = 3.14159f;
		int xi = (int)x;
		System.out.printf("%f as int is %d\n", x, xi);
		int xbits = Float.floatToIntBits(x);
		System.out.printf("%f as int is %d (0x%x)\n", x, xbits, xbits);
		x = Float.intBitsToFloat(xbits);
		System.out.printf("Bits 0x%x as float is %f\n", xbits, x);
	}
}
