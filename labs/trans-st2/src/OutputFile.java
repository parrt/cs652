import org.stringtemplate.v4.ST;

import java.util.ArrayList;
import java.util.List;

public class OutputFile extends OutputModelObject {
	public List<OutputModelObject> decls = new ArrayList<>();

	public void addDecl(OutputModelObject decl) { decls.add(decl); }

	public ST getTemplate() {
		ST t = new ST("<decls:{d | <d>\n}>");
		for (OutputModelObject decl : decls) {
			// convert objects to templates and add to output file template
			t.add("decls", decl.getTemplate());
		}
		return t;
	}
}
