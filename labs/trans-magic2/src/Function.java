import org.antlr.v4.codegen.model.ModelElement;
import org.stringtemplate.v4.ST;

import java.util.ArrayList;
import java.util.List;

public class Function extends OutputModelObject {
	public String id;

	@ModelElement
	public List<OutputModelObject> decls = new ArrayList<>();

	@ModelElement
	public List<OutputModelObject> args = new ArrayList<>();

	public void addDecl(OutputModelObject decl) { decls.add(decl); }

	public void addArg(OutputModelObject decl) { args.add(decl); }

	public Function(String id) {
		this.id = id;
	}
}
