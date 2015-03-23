import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

public class GeneratorST {
	public String toString(TableSep table, String templateFile) {
		STGroup templates = new STGroupFile(templateFile);
		ST tableST = templates.getInstanceOf("table");
		tableST.add("t", table);
		return tableST.render();
	}
}
