package symtab;

public abstract class Symbol {
	public String name;
	public Type type;

	public Symbol(String name) {
		this.name = name;
	}
}

