package vm;

public class Bytecode {
	public static class Instruction {
		String name; // E.g., "iadd", "call"
		int n = 0;
		public Instruction(String name) { this(name,0); }
		public Instruction(String name, int nargs) {
			this.name = name;
			this.n = nargs;
		}
	}

	// INSTRUCTION BYTECODES (byte is signed; use a short to keep 0..255)
	public static final short IADD = 1;     // int add
	public static final short ISUB = 2;
	public static final short IMUL = 3;
	public static final short ILT  = 4;     // int less than
	public static final short IEQ  = 5;     // int equal
	public static final short BR   = 6;     // branch
	public static final short BRT  = 7;     // branch if true
	public static final short BRF  = 8;    // branch if false
	public static final short ICONST = 9;  // push constant integer
	public static final short LOAD   = 10;  // load from local context
	public static final short GLOAD  = 11;  // load from global memory
	public static final short STORE  = 12;  // store in local context
	public static final short GSTORE = 13;  // store in global memory
	public static final short PRINT  = 14;  // print stack top
	public static final short POP  = 15;    // throw away top of stack

	public static final short FADD = 16;     // float add
	public static final short FSUB = 17;
	public static final short FMUL = 18;
	public static final short FLT  = 19;     // float less than
	public static final short FEQ  = 20;     // float equal

	public static final short HALT = 21;

	public static Instruction[] instructions = new Instruction[] {
		null, // <INVALID>
		new Instruction("iadd"), // index is the opcode
		new Instruction("isub"),
		new Instruction("imul"),
		new Instruction("ilt"),
		new Instruction("ieq"),
		new Instruction("br", 1),
		new Instruction("brt", 1),
		new Instruction("brf", 1),
		new Instruction("iconst", 1),
		new Instruction("load", 1),
		new Instruction("gload", 1),
		new Instruction("store", 1),
		new Instruction("gstore", 1),
		new Instruction("print"),
		new Instruction("pop"),
		new Instruction("fadd"),
		new Instruction("fsub"),
		new Instruction("fmul"),
		new Instruction("flt"),
		new Instruction("feq"),
		new Instruction("halt")
	};
}
