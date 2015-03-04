import java.io.IOException;
import java.io.InputStreamReader;

/*
$ java Expr
1+2*3
(+ 1 (* 2 3))
OK
$ java Expr
(1+2)*3
(* (+ 1 2) 3)
OK
 */
public class Expr {
	public static void main(String[] args) throws IOException {
		ExprLexer lexer = new ExprLexer(new InputStreamReader(System.in));
		ExprParser parser = new ExprParser(lexer);
		ParseTree tree = parser.expr();
		System.out.println(tree.toStringTree());
		System.out.println("OK");
	}
}
