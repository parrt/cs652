package symtab;

import java.util.HashMap;
import java.util.Map;

public class FunctionSymbol extends Symbol implements Scope {
	protected Scope enclosingScope;
	protected Map<String,VariableSymbol> args = new HashMap<>();

	public FunctionSymbol(String name, Scope enclosingScope) {
		super(name);
		this.enclosingScope = enclosingScope;
	}

	@Override
	public String getScopeName() {
		return name;
	}

	@Override
	public void define(Symbol s) {
		args.put(s.name, (VariableSymbol)s);
	}

	@Override
	public Symbol resolve(String name) {
		Symbol s = args.get(name);
		if ( s!=null ) return s;
		if ( getEnclosingScope()!=null ) {
			return getEnclosingScope().resolve(name);
		}
		return null;
	}

	@Override
	public Scope getEnclosingScope() {
		return enclosingScope;
	}
}
