package vm;

import static vm.Bytecode.CALL;
import static vm.Bytecode.CALLIDX;
import static vm.Bytecode.FUNCIDX;
import static vm.Bytecode.HALT;
import static vm.Bytecode.ICONST;
import static vm.Bytecode.IMUL;
import static vm.Bytecode.LOAD;
import static vm.Bytecode.PRINT;
import static vm.Bytecode.RET;

public class TestFuncPtr {
	static int[] f = {
	//								ADDRESS
	//.def main() { print f(10); }
		ICONST, 10,					// 0
		FUNCIDX, 0,                 // 2
		CALLIDX,					// 4
		PRINT,						// 5
		HALT,						// 6
	//.def f(x): ARGS=1, LOCALS=0
	// return 2*x
		LOAD, 0,                    // 7
		ICONST, 2,
		IMUL,
		RET
	};
	static FuncMetaData[] f_metadata = {
		//.def f(x): ARGS=1, LOCALS=0	ADDRESS
		new FuncMetaData("f", 1, 0, 7)
	};

	static int[] g = {
	//								ADDRESS
	//.def main() { print f(&g,10); }
		FUNCIDX, 1,                 // 0    (push index of g)
		ICONST, 10,					// 2
		CALL, 0,					// 4
		PRINT,						// 6
		HALT,						// 7
	//.def f(p,x): ARGS=1, LOCALS=0
	// return (*p)(x)
		LOAD, 1,                    // 8    (push arg x)
		LOAD, 0,                    // 10   (push func ptr/index p)
		CALLIDX,                    // 12
		RET,                        // 13
	//.def g(x): ARGS=1, LOCALS=0
	// return 2*x
		LOAD, 0,                    // 14
		ICONST, 2,
		IMUL,
		RET
	};
	static FuncMetaData[] g_metadata = {
		//.def f(p,x): ARGS=2, LOCALS=0	ADDRESS
		new FuncMetaData("f", 2, 0, 8),
		//.def g(x): ARGS=1, LOCALS=0	ADDRESS
		new FuncMetaData("g", 1, 0, 14)
	};

	public static void main(String[] args) {
//		VM vm = new VM(f, 0, 0, f_metadata);
//		vm.trace = true;
//		vm.exec();

		VM vm = new VM(g, 0, 0, g_metadata);
		vm.trace = true;
		vm.exec();
	}
}
