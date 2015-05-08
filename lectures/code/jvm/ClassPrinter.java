import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.Arrays;

public class ClassPrinter extends ClassVisitor {
	public ClassPrinter() {
		super(Opcodes.ASM4);
	}

	public void visit(int version, int access, String name,
					  String signature, String superName, String[] interfaces) {
		System.out.print(name.replaceAll("/","."));
		if ( superName!=null ) {
			System.out.println(" extends " + superName.replaceAll("/",".") + " {");
		}
	}

	public FieldVisitor visitField(int access, String name, String desc,
								   String signature, Object value) {
		System.out.println(" " + desc + " " + name);
		return null;
	}

	public MethodVisitor visitMethod(int access, String name,
									 String desc, String signature,
									 String[] exceptions)
	{
		String ret = Type.getReturnType(desc).getClassName();
		Type[] args = Type.getArgumentTypes(desc);
		System.out.println(" " + ret + " "+ name+"("+
						   Arrays.toString(args)+"): "+desc);
		return null; // don't visit method
	}

	public void visitEnd() {
		System.out.println("}");
	}

	public static void main(String[] args) throws Exception {
		ClassReader cr = new ClassReader("ClassPrinter");
		ClassPrinter cp = new ClassPrinter();
		int flags = 0;
		cr.accept(cp, flags);
	}
}
