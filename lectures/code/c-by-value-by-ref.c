void f(int x) { x++; }
void g(int *x) { *x++; }
void blort() {
	int x = 0;
	f(x);
	g(&x);
}
