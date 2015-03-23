public class Column {
    public static final int NOT_NULL=1;      // Column attribute constants
    public static final int UNIQUE=2;
    public static final int PRIMARY_KEY=4;
    public static final int ID_ATTRS =1|2|4; // NOT NULL UNIQUE PRIMARY

    public String name;                      // SQL column name
    public String type;                      // SQL column type
    public String attrs;                     // SQL column attributes
    
    public Column(String name, String type) { this(name,type,null); }
    public Column(String name, String type, String attrs) {
        this.name = name;
        this.type = type;
        this.attrs = attrs;
    }
}
