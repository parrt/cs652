import org.stringtemplate.v4.ST;

/** A generic root for any object representing an output component */
public abstract class OutputModelObject {
	/* Not needed
	public ST getTemplate() {
		String className = getClass().getSimpleName();
		ST st = Gen.templates.getInstanceOf(className);
		st.add("model", this);
		return st;
	}
	*/
}
