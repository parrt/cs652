public class Token {
	public static final int INVALID_TYPE = 0;
    public static final int EOF_TYPE = -1;

    public String text;
    public int type;

    public Token(int type, String text) {
        this.type = type;
        this.text = text;
    }

    public String toString() {
        return "['"+text+"':<"+type+">]";
    }
}
