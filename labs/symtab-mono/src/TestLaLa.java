import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class TestLaLa {
	public static void main(String[] args) throws Exception {
		ANTLRInputStream input = new ANTLRFileStream("bad.lala");
		LaLaLexer lexer = new LaLaLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		LaLaParser parser = new LaLaParser(tokens);
		ParseTree tree = parser.prog();
		System.out.println(tree.toStringTree(parser));

		ParseTreeWalker walker = new ParseTreeWalker();
		DefSymbols def = new DefSymbols();
		walker.walk(def, tree);

		System.out.println(def.globals.symbols);
	}
}
