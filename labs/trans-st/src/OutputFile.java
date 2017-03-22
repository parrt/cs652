import org.stringtemplate.v4.ST;

import java.util.ArrayList;
import java.util.List;

public class OutputFile extends OutputModelObject {
	public List<OutputModelObject> decls = new ArrayList<>();
	public List<ST> declSTs = new ArrayList<>();

	@Override
	public ST getTemplate() {
		ST st = super.getTemplate();
		for (OutputModelObject modelObject : decls) {
			declSTs.add(modelObject.getTemplate());
		}
		return st;
	}

	@Override
	public String toString() {
		return "a CFile with "+decls.size()+" decls";
	}
}
