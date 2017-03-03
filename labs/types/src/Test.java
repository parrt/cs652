import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.xpath.XPath;

import java.util.Collection;

public class Test {
	public static void main(String[] args) {
		String prop =
			"var x : int;\n" +
			"var y : float;\n" +
			"x = 2 * 3;\n" +
			"x = x + 1;" +
			"y = 3.4;\n";
		ANTLRInputStream input = new ANTLRInputStream(prop);
		ExprLexer lexer = new ExprLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		ExprParser parser = new ExprParser(tokens);
		ParseTree tree = parser.s();
		System.out.println(tree.toStringTree(parser));
//		Trees.inspect(tree, parser);

		Def def = new Def();
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(def, tree);

		ComputeTypes types = new ComputeTypes();
		walker = new ParseTreeWalker();
		walker.walk(types, tree);

		Collection<ParseTree> expressions = XPath.findAll(tree, "//e", parser);
		for (ParseTree e : expressions) {
			System.out.println(((ExprParser.EContext)e).type);
		}

	}
}
