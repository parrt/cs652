# Loading CSV into a 2D list

The goal of this lab is to read in a CSV file and save the data in a `List<List<String>>` 2D list data structure using a listener.

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

Create a new project or module with that grammar and a main program that reads from a file and parses by invoking function `file`. You will need [ANTLRFileStream](http://www.antlr.org/api/Java/org/antlr/v4/runtime/ANTLRFileStream.html) to read from a file instead of a string. Saves the tree coming back from `file`.  The data is in [colleges.csv]()

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

## Collecting the data

Now, we need a listener so create a `CSVLoader` class that extends `CSVBaseListener`.


