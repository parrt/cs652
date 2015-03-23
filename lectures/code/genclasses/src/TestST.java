public class TestST {
	public static void main(String[] args) {
		TableST t = new TableST("User");
		t.columns.add( new ColumnST("ID", "integer") );
		t.columns.add( new ColumnST("name", "char(30)") );
		t.columns.add( new ColumnST("username", "char(10)") );
		String output = t.toString();
		System.out.println(output);
	}
}
