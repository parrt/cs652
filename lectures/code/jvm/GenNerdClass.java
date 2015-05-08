
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.FileOutputStream;
import java.io.PrintWriter;

public class GenNerdClass implements Opcodes {
	public static void main(String[] args) throws Exception {
		ClassWriter cw = new ClassWriter(0);
		TraceClassVisitor tracer =
				new TraceClassVisitor(cw, new PrintWriter(System.out));
		ClassVisitor cv = tracer;
		String name = "Nerd";
		String generics = null;
		String superName = "java/lang/Object";
		String[] interfaces = null;
		int access = ACC_PUBLIC + ACC_INTERFACE;
		int version = V1_5;
		cv.visit(version, access, name, generics, superName, interfaces);

		int fieldAccess = ACC_PUBLIC + ACC_FINAL + ACC_STATIC;
		String shortDescriptor = Type.SHORT_TYPE.getDescriptor();
		FieldVisitor hair = cv.visitField(fieldAccess, "hair", shortDescriptor,
										  null, new Integer(0));
		hair.visitEnd();

		MethodVisitor playVideoGame =
			cv.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "playVideoGame",
						   "()V", null, null);
		// no code to define, just finish it up
		playVideoGame.visitEnd();
		cv.visitEnd(); // prints if using tracer
		byte[] b = cw.toByteArray();
		// can define or write to file:
		// defineClass(name, b, 0, b.length)
		// from findClass() in subclass of ClassLoader

		FileOutputStream fos = new FileOutputStream("Nerd.class");
		fos.write(b);
		fos.close();
	}
}
