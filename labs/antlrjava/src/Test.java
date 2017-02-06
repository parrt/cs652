import org.antlr.v4.gui.Trees;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

public class Test {
	public static void main(String[] args) throws Exception {
		ANTLRInputStream input = new ANTLRInputStream("int *i;");
		CDeclLexer lexer = new CDeclLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		CDeclParser parser = new CDeclParser(tokens);
		CDeclParser.DeclarationContext tree = parser.declaration();
		System.out.println(tree.toStringTree(parser));
		Trees.inspect(tree, parser);
	}
}
