import java.util.ArrayList;
import java.util.List;

public class Gen extends LangBaseListener {
	public List<String> decls = new ArrayList<>();

	@Override
	public void enterDecl(LangParser.DeclContext ctx) {
		String typename = ctx.typename().getText();
		String varname = ctx.ID().getText();
		if ( isClassName(typename) ) {
			decls.add(typename+" *"+varname+";");
		}
		else {
			decls.add(typename+" "+varname+";");
		}
	}

	/** Pretend we have type information */
	public boolean isClassName(String typename) {
		return Character.isUpperCase(typename.charAt(0));
	}
}
