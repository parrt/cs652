import org.stringtemplate.v4.ST;

import java.util.ArrayList;
import java.util.List;

public class CFile extends OutputModelObject {
	public List<OutputModelObject> decls = new ArrayList<>();

	@Override
	public String toString() {
		return "a CFile with "+decls.size()+" decls";
	}
}
