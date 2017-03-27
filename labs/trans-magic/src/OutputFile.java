import org.antlr.v4.codegen.model.ModelElement;
import org.stringtemplate.v4.ST;

import java.util.ArrayList;
import java.util.List;

public class OutputFile extends OutputModelObject {
	@ModelElement
	public List<OutputModelObject> elements = new ArrayList<>();

	public void add(OutputModelObject decl) { elements.add(decl); }
}
