grammar FaLaLa;

@header {import symtab.*;}

prog returns [Scope scope] : (var|func)+ ;

var : 'var' def ';' ;

def : ID ':' type ;

type : 'int' | 'float' ;

func returns [Scope scope] : 'function' ID '(' args ')' block ;

args: def (',' def)*
	|
	;

block returns [Scope scope] : '{' (var|stat)* '}' ;

stat : ID '=' expr ';' ;

expr : INT | ID ;

ID : [a-zA-Z_] [a-zA-Z0-9_]* ;

INT : [0-9]+ ;

WS : [ \t\n\r]+ -> channel(HIDDEN) ;

