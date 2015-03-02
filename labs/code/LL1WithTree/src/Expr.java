import java.io.IOException;
import java.io.InputStreamReader;

public class Expr {
	public static void main(String[] args) throws IOException {
		ExprLexer lexer = new ExprLexer(new InputStreamReader(System.in));
		ExprParser parser = new ExprParser(lexer);
		ParseTree tree = parser.expr();
		System.out.println(tree.toStringTree());
		System.out.println("OK");
	}
}
