import java.io.*;

public interface TokenStream {
    public Token nextToken() throws IOException;
}
