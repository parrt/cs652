import java.util.ArrayList;
import java.util.List;

public class CSVLoader extends CSVBaseListener {
	List<List<String>> data = new ArrayList<>();

	@Override
	public void exitRow(CSVParser.RowContext ctx) {
		List<String> row = new ArrayList<>();
		for (CSVParser.FieldContext f : ctx.field()) {
			row.add(f.getText().trim());
		}
		data.add(row);
	}
}
