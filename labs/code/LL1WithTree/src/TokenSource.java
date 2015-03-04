import java.io.IOException;

public interface TokenSource {
    public Token nextToken() throws IOException;
}
