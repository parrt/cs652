import org.antlr.symtab.GlobalScope;
import org.antlr.symtab.PrimitiveType;
import org.antlr.symtab.Scope;
import org.antlr.symtab.Symbol;
import org.antlr.symtab.Type;
import org.antlr.symtab.VariableSymbol;

public class Def extends ExprBaseListener {
	public static final PrimitiveType INT_TYPE = new PrimitiveType("int");
	public static final PrimitiveType FLOAT_TYPE = new PrimitiveType("float");

	public Scope currentScope;

	@Override
	public void enterS(ExprParser.SContext ctx) {
		ctx.scope = new GlobalScope(null);
		currentScope = ctx.scope;
		currentScope.define(INT_TYPE);
		currentScope.define(FLOAT_TYPE);
	}

	@Override
	public void enterVar(ExprParser.VarContext ctx) {
		Symbol type = currentScope.resolve(ctx.typename().getText());
		VariableSymbol var = new VariableSymbol(ctx.ID().getText());
		var.setType((Type)type);
		currentScope.define(var);
	}
}
