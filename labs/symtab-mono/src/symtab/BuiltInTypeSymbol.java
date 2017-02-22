package symtab;

public class BuiltInTypeSymbol extends Symbol implements Type {
	public BuiltInTypeSymbol(String name) {
		super(name);
	}

	@Override
	public String getTypeName() {
		return name;
	}
}
