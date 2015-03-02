import java.io.*;
import java.util.HashMap;
import java.util.Map;

public abstract class Lexer implements TokenStream {
	protected Reader reader = null;

	/** Lookahead char */
	protected int c;

	/** Text of currently matched token */
	protected StringBuffer text = new StringBuffer(100);

	public Lexer(Reader reader) throws IOException {
		this.reader = reader;
		nextChar();
	}

	protected void nextChar() throws IOException {
		c = (char)reader.read();
	}

	public abstract Token nextToken() throws IOException;

	public static void readAndPrint(Lexer scanner) throws IOException {
		Token t = scanner.nextToken();
		while ( t.type!=Token.EOF_TYPE ) {
			System.out.println(t);
			t = scanner.nextToken();
		}
	}
}
