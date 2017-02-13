# Loading CSV into a 2D list

The goal of this lab is to read in a CSV file and save the data in a `List<List<String>>` 2D list data structure using a listener. The data is in [colleges.csv](https://github.com/parrt/cs652/blob/master/labs/csvloader/colleges.csv) and looks like:

```
ID, OPEID, OPEID6, INSTNM, CITY, STABBR
243638, 1034300, 10343, College of Micronesia-FSM, Pohnpei, FM
243647, 1100900, 11009, Palau Community College, Koror, PW
376695, 3022400, 30224, College of the Marshall Islands, Majuro, MH
102553, 1146200, 11462, University of Alaska Anchorage, Anchorage, AK
102669, 106100, 1061, Alaska Pacific University, Anchorage, AK
102845, 2576900, 25769, Charter College-Anchorage, Anchorage, AK
103501, 2541000, 25410, Alaska Career College, Anchorage, AK
103644, 2295000, 22950, Everest College-Phoenix, Phoenix, AZ
103723, 2160305, 21603, Collins College, Phoenix, AZ
103732, 2173204, 21732, Empire Beauty School-Paradise Valley, Phoenix, AZ
103741, 966403, 9664, Empire Beauty School-Tucson, Tucson, AZ
103787, 2199900, 21999, American Indian College Inc, Phoenix, AZ
```

## Parsing

Start with the [CSV grammar](https://github.com/parrt/cs652/blob/master/labs/csv.md) from your previous lab:

```
grammar CSV;

file  : (row '\n')* ;
row   : field (',' field)* ;
field : INT ;

INT   : [0-9]+ ;
WS    : [ \r\t]+ -> skip ;
```

In our current data set, fields are not just integers. They are strings of "stuff" in between the commas. Augment `field` so that it also accepts a `STRING` and then define `STRING` so that it matches anything other than a comma or a newline:

<img src=images/STRING.png width=130>

Create a new project or module with that grammar and a main program that reads from a file and parses by invoking function `file`. You will need [ANTLRFileStream](http://www.antlr.org/api/Java/org/antlr/v4/runtime/ANTLRFileStream.html) to read from a file instead of a string. Saves the tree coming back from `file`.  

All programs have the notion of a **working directory** and any references to files are relative to this directory. When you run from intellij, you should "edit configurations" so that the working directory is the directory where you saved that file. (*I do this by telling it to run the main program, letting it fail, and then going to edit configurations to set the appropriate directory.*) Otherwise it won't be able to see that input file when you run with `new ANTLRFileStream("colleges.csv")`.

## Collecting the data

In order to collect data, we will use a listener so create a `CSVLoader` class that extends `CSVBaseListener`. Leave it blank at first (i.e., don't implement any methods). Then, augment your main program so that it walks the parse tree using your `CSVLoader`. Make sure that the program still runs.

To collect data, define a field:

```java
List<List<String>> data = new ArrayList<>();
```

And then fill it in `exitRow`. To create a `List<String>` for a single row, you will walk the list of `field` subtrees underneath a `RowContext`:

```java
for (CSVParser.FieldContext f : ctx.field()) {
	row.add(f.getText().trim());
}
```

Back in the main program, print out the `loader.data` list:

```java
for (List<String> row : loader.data) {
	System.out.println(row);
}
```

The output should look something like:

```
[ID, OPEID, OPEID6, INSTNM, CITY, STABBR]
[243638, 1034300, 10343, College of Micronesia-FSM, Pohnpei, FM]
[243647, 1100900, 11009, Palau Community College, Koror, PW]
[376695, 3022400, 30224, College of the Marshall Islands, Majuro, MH]
[102553, 1146200, 11462, University of Alaska Anchorage, Anchorage, AK]
[102669, 106100, 1061, Alaska Pacific University, Anchorage, AK]
[102845, 2576900, 25769, Charter College-Anchorage, Anchorage, AK]
[103501, 2541000, 25410, Alaska Career College, Anchorage, AK]
[103644, 2295000, 22950, Everest College-Phoenix, Phoenix, AZ]
[103723, 2160305, 21603, Collins College, Phoenix, AZ]
[103732, 2173204, 21732, Empire Beauty School-Paradise Valley, Phoenix, AZ]
[103741, 966403, 9664, Empire Beauty School-Tucson, Tucson, AZ]
[103787, 2199900, 21999, American Indian College Inc, Phoenix, AZ]
```

We could also use a visitor to collect the information, but we have a type issue regarding the return value from `row` and `file`. `row` should return `List<String>` but `file` should return `List<List<String>>`.   Of course, we could set the return value to `Object` to force fit a visitor into this application. Rather than hush the type system, let's simply build a data structure stored as a field of the loader.