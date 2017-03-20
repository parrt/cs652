import org.stringtemplate.v4.ST;

public class PrimitiveDecl extends OutputModelObject {
	public String type;
	public String id;

	public PrimitiveDecl(String type, String id) {
		this.type = type;
		this.id = id;
	}
}
