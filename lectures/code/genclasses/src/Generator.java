
public class Generator {
	public String toString(TableSep table) {
		StringBuilder buf = new StringBuilder();
		buf.append("CREATE TABLE "+ table.name+" (\n");
		int i = 0;
		for (ColumnSep c : table.columns) {
			if ( i>0 ) buf.append(",\n");
			buf.append("    "); // indent a bit
			buf.append(toString(c));
			i++;
		}
		buf.append(");\n");
		return buf.toString();
	}

	public String toString(ColumnSep column) {
		StringBuilder attrBuf = new StringBuilder();
		int i = 0;
		for (String a : column.attrs) {
			if ( i>0 ) attrBuf.append(", ");
			attrBuf.append(a);
			i++;
		}
		return column.name+" "+column.type+" "+attrBuf.toString();
	}
}
