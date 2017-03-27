import org.antlr.v4.codegen.model.ModelElement;
import org.stringtemplate.v4.ST;

import java.util.ArrayList;
import java.util.List;

public class OutputFile extends OutputModelObject {
	@ModelElement
	public List<OutputModelObject> decls = new ArrayList<>();
	@ModelElement
	public List<OutputModelObject> functions = new ArrayList<>();

	public void addDecl(OutputModelObject decl) { decls.add(decl); }
	public void addFun(OutputModelObject fun) { functions.add(fun); }
}
