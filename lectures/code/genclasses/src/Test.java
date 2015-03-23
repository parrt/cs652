public class Test {
	public static void main(String[] args) {
		Column ID = new Column("ID", "integer");
		ID.attrs.add("NOTNULL");
		Table t = new Table("User");
		t.columns.add(ID);
		t.columns.add( new Column("name", "char(30)") );
		t.columns.add( new Column("username", "char(10)") );
		String output = t.toString();
		System.out.println(output);
	}
}
