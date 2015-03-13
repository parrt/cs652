import org.antlr.symbols.FunctionSymbol;
import org.antlr.symbols.GlobalScope;
import org.antlr.symbols.LocalScope;
import org.antlr.symbols.Scope;
import org.antlr.symbols.VariableSymbol;
import org.antlr.v4.runtime.misc.NotNull;

public class DefSymbolsAndScopes extends SimpleBaseListener {
	Scope currentScope;

	@Override
	public void enterFile(@NotNull SimpleParser.FileContext ctx) {
		GlobalScope g = new GlobalScope(null);
		ctx.scope = g;
		pushScope(g);
	}

	@Override
	public void exitFile(@NotNull SimpleParser.FileContext ctx) {
		popScope();
	}

	@Override
	public void enterFunc(@NotNull SimpleParser.FuncContext ctx) {
		FunctionSymbol f = new FunctionSymbol(ctx.name.getText(), ctx);
		f.setEnclosingScope(currentScope);
		currentScope.define(f);
		ctx.scope = f;
		pushScope(f);
	}

	@Override
	public void exitFunc(@NotNull SimpleParser.FuncContext ctx) {
		popScope();
	}

	@Override
	public void enterBlock(@NotNull SimpleParser.BlockContext ctx) {
		LocalScope l = new LocalScope(currentScope);
		ctx.scope = l;
		pushScope(l);
	}

	@Override
	public void exitBlock(@NotNull SimpleParser.BlockContext ctx) {
		popScope();
	}

	@Override
	public void enterVar(@NotNull SimpleParser.VarContext ctx) {
		VariableSymbol v = new VariableSymbol(currentScope, ctx.ID().getText());
		currentScope.define(v);
	}

	@Override
	public void enterArg(@NotNull SimpleParser.ArgContext ctx) {
		VariableSymbol v = new VariableSymbol(currentScope, ctx.ID().getText());
		currentScope.define(v);
	}

	private void pushScope(Scope s) {
		currentScope = s;
		System.out.println("entering: "+currentScope.getScopeName()+":"+s);
	}

	private void popScope() {
		System.out.println("leaving: "+currentScope.getScopeName()+":"+currentScope);
		currentScope = currentScope.getEnclosingScope();
	}
}
