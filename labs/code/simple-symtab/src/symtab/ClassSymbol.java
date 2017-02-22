package symtab;

import java.util.HashMap;
import java.util.Map;

class ClassSymbol extends Symbol implements Type, Scope {
	Map<String,Symbol> symbols = new HashMap<>();

	public ClassSymbol(String name) {
		super(name);
	}

	@Override
	public String getTypeName() {
		return name;
	}

	@Override
	public String getScopeName() {
		return name;
	}

	@Override
	public void define(Symbol s) {
		symbols.put(s.name, s);
	}

	@Override
	public Symbol resolve(String name) {
		return symbols.get(name);
	}

	@Override
	public Scope getEnclosingScope() {
		return null;
	}
}
