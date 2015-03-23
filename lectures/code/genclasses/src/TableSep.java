import java.util.ArrayList;
import java.util.List;

public class TableSep {
	public String name; // SQL table name
	public List<ColumnSep> columns = new ArrayList<>();

	public TableSep(String name) {
		this.name = name;
	}
}
