grammar Lang;

file : (fun|decl)+ ;

fun : 'fun' ID '(' (arg (',' arg)*)? ')' '{' decl+ '}' ;

arg : typename ID ;

decl : typename ID ';' ;

typename : 'int' | ID ;

ID : [a-zA-Z]+ ;
WS : [ \r\t\n]+ -> skip;