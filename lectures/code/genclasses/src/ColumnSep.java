import java.util.ArrayList;
import java.util.List;

public class ColumnSep {
	public String name;                      		// SQL column name
	public String type;                      		// SQL column type
	public List<String> attrs = new ArrayList<>(); // SQL column attributes

	public ColumnSep(String name, String type) {
		this.name = name;
		this.type = type;
	}
}
