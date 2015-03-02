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

	public ParseTree expr() {
		ParseTree t;
		ParseTree root = new RuleNode("expr");
		t = term();
		root.addChild(t);
		...
		return root;
	}

	public ParseTree term() {
	}

	public ParseTree factor() {
	}
}
