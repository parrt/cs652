import java.util.ArrayList;
import java.util.List;

public class ColumnSep {
	String name;                      		// SQL column name
	String type;                      		// SQL column type
	List<String> attrs = new ArrayList<>(); // SQL column attributes

	public ColumnSep(String name, String type) {
		this.name = name;
		this.type = type;
	}
}
