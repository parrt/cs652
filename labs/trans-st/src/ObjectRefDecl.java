import org.stringtemplate.v4.ST;

public class ObjectRefDecl extends OutputModelObject {
	public String type;
	public String id;

	public ObjectRefDecl(String type, String id) {
		this.type = type;
		this.id = id;
	}

	public ST getTemplate() {
		ST t = new ST("<type> *<id>;");
		t.add("type", type);
		t.add("id", id);
		return t;
	}}
