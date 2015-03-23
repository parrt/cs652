public class TestSep {
	public static void main(String[] args) {
		// create model
		ColumnSep ID = new ColumnSep("ID", "integer");
		ID.attrs.add("NOTNULL");
		TableSep t = new TableSep("User");
		t.columns.add(ID);
		t.columns.add( new ColumnSep("name", "char(30)") );
		t.columns.add( new ColumnSep("username", "char(10)") );

		// emit output
		Generator gen = new Generator();
		String output = gen.toString(t);
		System.out.println(output);
	}
}
