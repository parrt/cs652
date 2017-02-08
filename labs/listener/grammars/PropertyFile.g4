grammar PropertyFile;

file : prop+ ;
prop : ID '=' STRING '\n' ;

ID : [a-zA-Z]+ ;
STRING : '"' .*? '"' ;
WS : [ \r]+ -> skip ;