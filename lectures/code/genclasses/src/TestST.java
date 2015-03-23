public class TestST {
	public static void main(String[] args) {
		TableST t = new TableST("User");
		t.columns.add( new Column("ID", "integer") );
		t.columns.add( new Column("name", "char(30)") );
		t.columns.add( new Column("username", "char(10)") );
		String output = t.toString();
		System.out.println(output);
	}
}
