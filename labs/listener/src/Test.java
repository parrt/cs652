import org.antlr.v4.gui.Trees;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Test {
	public static void main(String[] args) {
		String prop = "id=\"parrt\"\n";
		ANTLRInputStream input = new ANTLRInputStream(prop);
		PropertyFileLexer lexer = new PropertyFileLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		PropertyFileParser parser = new PropertyFileParser(tokens);
		ParseTree tree = parser.file();
		System.out.println(tree.toStringTree(parser));
//		Trees.inspect(tree, parser);

		ParseTreeWalker walker = new ParseTreeWalker();
		PropertyFileLoader loader = new PropertyFileLoader();
		walker.walk(loader, tree);

		System.out.println(loader.props);
	}
}
