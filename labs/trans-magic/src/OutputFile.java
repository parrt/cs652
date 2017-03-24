import org.stringtemplate.v4.ST;

import java.util.ArrayList;
import java.util.List;

public class OutputFile extends OutputModelObject {
	public List<OutputModelObject> elements = new ArrayList<>();

	public void add(OutputModelObject decl) { elements.add(decl); }

	public ST getTemplate() {
		ST t = super.getTemplate();
		for (OutputModelObject el : elements) {
			// convert objects to templates and add to output file template
			t.add("elements", el.getTemplate());
		}
		return t;
	}
}
