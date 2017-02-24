import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class TestFaLaLa {
	public static void main(String[] args) throws Exception {
		ANTLRInputStream input = new ANTLRFileStream("test.falala");
		FaLaLaLexer lexer = new FaLaLaLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		FaLaLaParser parser = new FaLaLaParser(tokens);
		ParseTree tree = parser.prog();
		System.out.println(tree.toStringTree(parser));

		ParseTreeWalker walker = new ParseTreeWalker();
		DefSymbols def = new DefSymbols();
		walker.walk(def, tree);
	}
}
