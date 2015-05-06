class SumArray {
    static { System.loadLibrary("sum"); }

    native int sumArray(int arr[]);

    public static void main(String args[]) {
	SumArray s = new SumArray();
	int arr[] = new int [10];
	for (int i = 0; i < 10; i++) {
            arr[i] = i;
        }
	int sum = s.sumArray(arr);
	System.out.println("sum = " + sum);
    }
}
