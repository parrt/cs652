import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;

public class GenCallGraph {
	public static final String input =
		"def f() { g(); h();}\n" +
		"def z() { g(); z();}\n" +
		"def g() { g(); f(); }";

	public static void main(String[] args) throws IOException {
		LangLexer lexer = new LangLexer(new ANTLRInputStream(input));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		LangParser parser = new LangParser(tokens);
		ParserRuleContext tree = parser.file();
		LinkViz graph = new LinkViz();
		ParseTreeWalker walker = new ParseTreeWalker();
		CollectCalls calls = new CollectCalls(graph);
		walker.walk(calls, tree);
		System.out.println(GenDOT.gen(graph));
	}
}
