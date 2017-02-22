grammar LaLa;

@header {
import symtab.*;
}

prog returns [Scope globals] : (var|stat)+ ;

var : 'var' ID ':' type ';' ;

type : 'int' | 'float' ;

stat : ID '=' expr ';' ;

expr : INT | ID ;

ID : [a-zA-Z_] [a-zA-Z0-9_]* ;

INT : [0-9]+ ;

WS : [ \t\n\r]+ -> channel(HIDDEN) ;

