import org.antlr.symtab.Scope;
import org.antlr.symtab.TypedSymbol;

public class ComputeTypes extends ExprBaseListener {
	public Scope currentScope;

	@Override
	public void enterS(ExprParser.SContext ctx) {
		currentScope = ctx.scope;
	}

	@Override
	public void enterIntRef(ExprParser.IntRefContext ctx) {
		ctx.type = Def.INT_TYPE;
	}

	@Override
	public void enterFloatRef(ExprParser.FloatRefContext ctx) {
		ctx.type = Def.FLOAT_TYPE;
	}

	@Override
	public void enterVarRef(ExprParser.VarRefContext ctx) {
		TypedSymbol var = (TypedSymbol)currentScope.resolve(ctx.ID().getText());
		ctx.type = var.getType();
	}

	@Override
	public void exitAdd(ExprParser.AddContext ctx) {
		ExprParser.EContext leftOpnd = (ExprParser.EContext) ctx.getChild(0);
		ctx.type = leftOpnd.type;
	}

	@Override
	public void exitMult(ExprParser.MultContext ctx) { // must be exit not enter!!
		ExprParser.EContext leftOpnd = (ExprParser.EContext) ctx.getChild(0);
		ctx.type = leftOpnd.type;
	}
}
