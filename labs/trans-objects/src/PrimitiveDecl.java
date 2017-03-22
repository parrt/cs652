public class PrimitiveDecl extends OutputModelObject {
	public String type;
	public String id;

	public PrimitiveDecl(String type, String id) {
		this.type = type;
		this.id = id;
	}

	@Override
	public String toString() {
		return type+" "+id+";";
	}
}
