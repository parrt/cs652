grammar Lang;

file : (fun|decl)+ ;

fun : 'fun' ID '(' ')' '{' decl+ '}' ;

decl : typename ID ';' ;

typename : 'int' | ID ;

ID : [a-zA-Z]+ ;
WS : [ \r\t\n]+ -> skip;

