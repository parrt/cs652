grammar CSV;

file  : (row '\n')* ;
row   : field (',' field)* ;
field : INT | STRING ;

INT   : [0-9]+ ;

STRING: ~[,\n]+ ;