import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class TestLaLa {
	public static void main(String[] args) throws Exception {
		ANTLRInputStream input = new ANTLRFileStream("test.lala");
		LaLaLexer lexer = new LaLaLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		LaLaParser parser = new LaLaParser(tokens);
		ParseTree tree = parser.prog();
		System.out.println(tree.toStringTree(parser));
	}
}
