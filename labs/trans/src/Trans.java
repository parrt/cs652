import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Trans {
	public static void main(String[] args) {
		String code =
			"int x;\n" +
			"A b;\n";
		ANTLRInputStream input = new ANTLRInputStream(code);
		LangLexer lexer = new LangLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		LangParser parser = new LangParser(tokens);
		ParseTree tree = parser.file(); // start up

		System.out.println(tree.toStringTree(parser));

		ParseTreeWalker walker = new ParseTreeWalker();
		Gen listener = new Gen();
		walker.walk(listener, tree);

		for (String decl : listener.decls) {
			System.out.println(decl);
		}
	}
}
