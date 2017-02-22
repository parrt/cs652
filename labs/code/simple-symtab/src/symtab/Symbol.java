package symtab;

public abstract class Symbol {
	String name;
	Type type;

	public Symbol(String name) {
		this.name = name;
	}
}

