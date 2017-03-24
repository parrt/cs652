import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.stringtemplate.v4.ST;

public class Trans {
	public static void main(String[] args) {
		String code =
			"int x;\n" +
			"A b;\n" +
			"fun f() { int y; T t; }\n";
		ANTLRInputStream input = new ANTLRInputStream(code);
		LangLexer lexer = new LangLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		LangParser parser = new LangParser(tokens);
		ParseTree tree = parser.file(); // start up

		System.out.println(tree.toStringTree(parser));

		Gen gen = new Gen();
		OutputModelObject file = gen.visit(tree);

		ST output = file.getTemplate();
		System.out.println(output.render());
	}
}
