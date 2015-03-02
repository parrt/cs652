import java.util.ArrayList;

public class TokenNode extends ParseTree {
	public TokenNode(Token token) {
		super(token);
	}

	@Override
	public Token getPayload() {
		return (Token)super.getPayload();
	}

	/** Get the token text for this node */
	public String getText() {
		return getPayload().text;
	}

	/** Get the token type for this node */
	public int getType() {
		return getPayload().type;
	}
}
