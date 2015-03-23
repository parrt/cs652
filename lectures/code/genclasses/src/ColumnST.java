import java.util.ArrayList;
import java.util.List;

public class ColumnST {
	String name;                      		// SQL column name
	String type;                      		// SQL column type
	List<String> attrs = new ArrayList<>(); // SQL column attributes

	public ColumnST(String name, String type) {
		this.name = name;
		this.type = type;
	}

	public String toString() {
		StringBuilder attrBuf = new StringBuilder();
		int i = 0;
		for (String a : attrs) {
			if ( i>0 ) attrBuf.append(", ");
			attrBuf.append(a);
			i++;
		}
		return name+" "+type+" "+attrBuf.toString();
	}
}
