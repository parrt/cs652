
public class ObjectRefDecl extends OutputModelObject {
	public String type;
	public String id;

	public ObjectRefDecl(String type, String id) {
		this.type = type;
		this.id = id;
	}

	@Override
	public String toString() {
		return type+" *"+id+";";
	}
}
