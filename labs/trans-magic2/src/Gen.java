import org.antlr.v4.runtime.tree.ParseTree;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

public class Gen extends LangBaseVisitor<OutputModelObject> {
	public static STGroup templates = new STGroupFile("C.stg");

	@Override
	public OutputModelObject visitFile(LangParser.FileContext ctx) {
		OutputFile file = new OutputFile();
		for (ParseTree child : ctx.children) {
			OutputModelObject m = visit(child);
			if ( m instanceof Function ) {
				file.addFun(m);
			}
			else {
				file.addDecl(m);
			}
		}
		return file;
	}

	@Override
	public OutputModelObject visitFun(LangParser.FunContext ctx) {
		Function function = new Function(ctx.ID().getText());
		for (LangParser.ArgContext arg : ctx.arg()) {
			OutputModelObject m = visit(arg);
			function.addArg(m);
		}
		for (LangParser.DeclContext decl : ctx.decl()) {
			OutputModelObject m = visit(decl);
			function.addDecl(m);
		}
		return function;
	}

	@Override
	public OutputModelObject visitArg(LangParser.ArgContext ctx) {
		String typename = ctx.typename().getText();
		String varname = ctx.ID().getText();
		return getDecl(typename, varname);
	}

	@Override
	public OutputModelObject visitDecl(LangParser.DeclContext ctx) {
		String typename = ctx.typename().getText();
		String varname = ctx.ID().getText();
		return getDecl(typename, varname);
	}

	private OutputModelObject getDecl(String typename, String varname) {
		if ( isClassName(typename) ) {
			return new ObjectRefDecl(typename, varname);
		}
		else {
			return new PrimitiveDecl(typename, varname);
		}
	}

	/** Pretend we have type information */
	public boolean isClassName(String typename) {
		return Character.isUpperCase(typename.charAt(0));
	}
}
