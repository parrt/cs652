import java.util.ArrayList;
import java.util.List;

public abstract class ParseTree {
	/** The children of this node */
	protected List<ParseTree> children;

	/** The payload of a tree node */
	protected Object payload;

	public ParseTree(Object payload) {
		this.payload = payload;
	}

	public Object getPayload() {
		return payload;
	}

	/**Add a node to the end of the child list for this node */
	public void addChild(ParseTree node) {
		if ( node==null ) return;
		if ( children==null ) {
			children = new ArrayList<>();
		}
		children.add(node);
	}

	public void addChild(Token t) {
		addChild(new TokenNode(t));
	}

	/** Get the token text for this node */
	public abstract String getText();

	/** Get the token type for this node */
	public abstract int getType();

	/** Print out a child-sibling tree in LISP notation */
	public String toStringTree() {
		if ( children==null || children.size()==0 ) {
			return this.getText();
		}
		StringBuffer buf = new StringBuffer();
		if ( children!=null ) {
			buf.append("(");
			buf.append(this.getText());
			buf.append(' ');
		}
		for (int i = 0; children!=null && i < children.size(); i++) {
			ParseTree t = children.get(i);
			if ( i>0 ) {
				buf.append(' ');
			}
			buf.append(t.toStringTree());
		}
		if ( children!=null ) {
			buf.append(")");
		}
		return buf.toString();
	}
}
