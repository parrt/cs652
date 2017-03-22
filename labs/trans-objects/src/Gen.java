public class Gen extends LangBaseListener {
	public OutputFile file;

	@Override
	public void enterFile(LangParser.FileContext ctx) {
		file = new OutputFile();
	}

	@Override
	public void enterDecl(LangParser.DeclContext ctx) {
		String typename = ctx.typename().getText();
		String varname = ctx.ID().getText();
		if ( isClassName(typename) ) {
			file.decls.add(new ObjectRefDecl(typename, varname));
		}
		else {
			file.decls.add(new PrimitiveDecl(typename, varname));
		}
	}

	public boolean isClassName(String typename) {
		return Character.isUpperCase(typename.charAt(0));
	}
}
