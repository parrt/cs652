import org.stringtemplate.v4.ST;

public abstract class OutputModelObject {
	public ST getTemplate() {
		String className = getClass().getSimpleName();
		ST st = Gen.templates.getInstanceOf(className);
		st.add("model", this);
		return st;
	}
}
