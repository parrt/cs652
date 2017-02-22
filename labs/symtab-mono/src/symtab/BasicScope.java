package symtab;

import java.util.HashMap;
import java.util.Map;

public class BasicScope implements Scope {
	public Map<String,Symbol> symbols = new HashMap<>();

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
		Symbol s = symbols.get(name);
		if ( s!=null ) return s;
		if ( getEnclosingScope()!=null ) {
			return getEnclosingScope().resolve(name);
		}
		return null;
	}

	@Override
	public Scope getEnclosingScope() {
		return null;
	}
}
