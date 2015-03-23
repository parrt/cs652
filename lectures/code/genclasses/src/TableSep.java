import java.util.ArrayList;
import java.util.List;

public class TableSep {
	String name; // SQL table name
	List<ColumnSep> columns = new ArrayList<>();

	public TableSep(String name) {
		this.name = name;
	}
}
