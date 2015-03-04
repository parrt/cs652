import java.io.IOException;

/** Parse according to this grammar:
 expr : term ('+' term)* ;
 term : factor ('*' factor)* ;
 factor : ID | INT | '(' expr ')' ;
 */
public class ExprParser extends Parser {
	public ExprParser(TokenSource input) throws IOException {
		super(input);
	}

	/**  expr : term ('+' term)* ; */
	public void expr() {
	}

	/** term : factor ('*' factor)* ; */
	public void term() {
	}

	/** factor : ID | INT | '(' expr ')' */
	public void factor() {
	}
}
