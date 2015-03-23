import java.util.ArrayList;
import java.util.List;

public class Table {
	String name; // SQL table name
	List<Column> columns = new ArrayList<>();

	public Table(String name) {
		this.name = name;
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("CREATE TABLE "+ name+" (\n");
		int i = 0;
		for (Column c : columns) {
			if ( i>0 ) buf.append(",\n");
			buf.append("    "); // indent a bit
			buf.append(c);
			i++;
		}
		buf.append(");\n");
		return buf.toString();
	}
}
