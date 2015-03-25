import org.antlr.v4.runtime.misc.NotNull;

public class CollectCalls extends LangBaseListener {
	String currentFunc;
	LinkViz links;

	public CollectCalls(LinkViz links) {
		this.links = links;
	}

	@Override
	public void enterFunc(@NotNull LangParser.FuncContext ctx) {
		currentFunc = ctx.ID().getText();
	}

	@Override
	public void enterStat(@NotNull LangParser.StatContext ctx) {
		links.addLink(currentFunc, ctx.ID().getText());
	}
}
