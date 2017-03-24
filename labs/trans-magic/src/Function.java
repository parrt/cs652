import org.stringtemplate.v4.ST;

import java.util.ArrayList;
import java.util.List;

public class Function extends OutputModelObject {
	public String id;
	public List<OutputModelObject> decls = new ArrayList<>();

	public void add(OutputModelObject decl) { decls.add(decl); }

	public Function(String id) {
		this.id = id;
	}

	public ST getTemplate() {
		ST t = super.getTemplate();
		for (OutputModelObject decl : decls) {
			// convert objects to templates and add to output file template
			t.add("decls", decl.getTemplate());
		}
		return t;
	}
}
