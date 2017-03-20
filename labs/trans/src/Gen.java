public class Gen extends LangBaseListener {
	@Override
	public void enterDecl(LangParser.DeclContext ctx) {
		String typename = ctx.typename().getText();
		String varname = ctx.ID().getText();
		if ( isClassName(typename) ) {
			System.out.println(typename+" *"+varname+";");
		}
		else {
			System.out.println(typename+" "+varname+";");
		}
	}

	/** Pretend we have type information */
	public boolean isClassName(String typename) {
		return Character.isUpperCase(typename.charAt(0));
	}
}
