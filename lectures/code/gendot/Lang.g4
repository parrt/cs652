grammar Lang;

file : func+ EOF ;

func : 'def' ID '()' '{' stat+ '}' ;

stat : ID '()' ';' ;

ID : [a-zA-Z]+ ;
WS : [ \t\n\r]+ -> channel(HIDDEN) ;
