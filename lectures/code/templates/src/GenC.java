import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

public class GenC {
	public static class Field {
		public String name;
		public String type;

		public Field(String name, String type) {
			this.name = name;
			this.type = type;
		}
	}
	public static void main(String[] args) {
		STGroup templates = new STGroupFile("C.stg");
		ST fileST = templates.getInstanceOf("file");
		fileST.add("name", "Foo");
		fileST.add("fields", new Field("i", "int"));
		fileST.add("fields", new Field("j", "float"));
		System.out.println(fileST.render());
	}
}
