import org.antlr.v4.runtime.misc.NotNull;

public class CollectCalls extends LangBaseListener {
	String currentFunc;
	LinkViz graph;

	public CollectCalls(LinkViz graph) {
		this.graph = graph;
	}

	@Override
	public void enterFunc(@NotNull LangParser.FuncContext ctx) {
		currentFunc = ctx.ID().getText();
	}

	@Override
	public void enterStat(@NotNull LangParser.StatContext ctx) {
		graph.addEdge(currentFunc, ctx.ID().getText());
	}
}
