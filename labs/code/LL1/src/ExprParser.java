import java.io.IOException;

/** Parse according to this grammar:
 expr : term ('+' term)* ;
 term : factor ('*' factor)* ;
 factor : ID | INT | '(' expr ')' ;
 */
public class ExprParser extends Parser {
	public ExprParser(TokenStream input) throws IOException {
		super(input);
	}

	public void expr() {
	}

	public void term() {
	}

	public void factor() {
	}
}
