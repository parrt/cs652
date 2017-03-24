import org.antlr.v4.codegen.model.ModelElement;
import org.stringtemplate.v4.ST;

import java.util.ArrayList;
import java.util.List;

public class Function extends OutputModelObject {
	public String id;

	@ModelElement
	public List<OutputModelObject> decls = new ArrayList<>();

	public void add(OutputModelObject decl) { decls.add(decl); }

	public Function(String id) {
		this.id = id;
	}

	/* Not needed with ModelConverter
	public ST getTemplate() {
		ST t = super.getTemplate();
		for (OutputModelObject decl : decls) {
			// convert objects to templates and add to output file template
			t.add("decls", decl.getTemplate());
		}
		return t;
	}
	*/
}
