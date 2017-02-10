public class ExprEval extends ExprBaseVisitor<Integer> {
	@Override
	public Integer visitMult(ExprParser.MultContext ctx) {
		return visit(ctx.e(0)) * visit(ctx.e(1));
	}

	@Override
	public Integer visitAdd(ExprParser.AddContext ctx) {
		return visit(ctx.e(0)) + visit(ctx.e(1));
	}

	@Override
	public Integer visitNumber(ExprParser.NumberContext ctx) {
		return Integer.valueOf(ctx.INT().getText());
	}
}
