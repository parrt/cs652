import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;

import java.io.IOException;

public class Test {
	public static void main(String[] args) throws IOException {
		ANTLRInputStream input = new ANTLRFileStream(args[0]);
		SimpleLexer l = new SimpleLexer(input);
		TokenStream tokens = new CommonTokenStream(l);

		SimpleParser parser = new SimpleParser(tokens);
		ParserRuleContext tree = parser.file();
		System.out.println(tree.toStringTree(parser));
	}
}
