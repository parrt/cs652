package symtab;

import java.util.HashMap;
import java.util.Map;

public class BasicScope implements Scope {
	Map<String,Symbol> symbols = new HashMap<>();

	@Override
	public String getScopeName() {
		return "<unknown>";
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
