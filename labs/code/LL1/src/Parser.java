import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {
	protected TokenStream input = null;

	/** Buffer of all tokens including EOF */
	protected Token[] tokens = null;

	/** Pointer into tokens buffer; p=-1 means uninitialized */
	protected int p = -1;

	public Parser(TokenStream input) throws IOException {
		this.input = input;
		initTokenBuffer(input);
	}

	protected void initTokenBuffer(TokenStream input) throws IOException {
		// fill token buffer to make lookahead easy
		List<Token> tokenBuf = new ArrayList<Token>();
		Token t;
		do {
			t = input.nextToken();
			tokenBuf.add(t);
		} while ( t.type!=Token.EOF_TYPE ); // add the end of file to the buffer
		tokens = new Token[tokenBuf.size()];
		tokenBuf.toArray(tokens);
		p = 0; // point at first token
	}

	protected void consume() {
		p++;
	}

	protected int lookahead() {
		if ( p>=tokens.length ) {
			return Token.EOF_TYPE;
		}
		return tokens[p].type;
	}

	protected Token token() {
		return tokens[p];
	}

	protected void match(int type) {
		//System.out.println("match "+type+" with "+tokens[p]);
		if ( tokens[p].type!=type ) {
			throw new RuntimeException("token mismatch: expecting token type "+type+"; found "+
									   token().text);
		}
		consume();
	}
}
