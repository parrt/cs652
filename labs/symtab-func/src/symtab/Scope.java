package symtab;

public interface Scope {
	String getScopeName();
	void define(Symbol s);
	Symbol resolve(String name); // bind or lookup
	Scope getEnclosingScope();
}