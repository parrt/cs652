import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;

public class Load {
	public static void main(String[] args) throws IOException {
		ANTLRInputStream input = new ANTLRFileStream("colleges.csv");
		CSVLexer lexer = new CSVLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		CSVParser parser = new CSVParser(tokens);
		ParseTree tree = parser.file();
		System.out.println(tree.toStringTree(parser));
	}
}
