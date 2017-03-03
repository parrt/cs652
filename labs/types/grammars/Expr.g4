grammar Expr;

@header {import org.antlr.symtab.*;}

s returns [GlobalScope scope]: stat+ ;

var : 'var' ID ':' typename ';' ;

typename : 'int' | 'float' ;

stat : var | assign ;

assign : ID '=' e ';' ;

e returns [Type type]
  : e '*' e	# Mult
  | e '+' e	# Add
  | INT		# IntRef
  | FLOAT	# FloatRef
  | ID		# VarRef
  ;

ID : [a-zA-Z_] [a-zA-Z0-9_]* ;
INT : [0-9]+ ;
FLOAT
    :   '-'? INT '.' INT EXP?   // 1.35, 1.35E-9, 0.3, -4.5
    |   '-'? INT EXP            // 1e10 -3e4
    |   '-'? INT                // -3, 45
    ;

fragment EXP :   [Ee] [+\-]? INT ;
WS : [ \r\t\n]+ -> skip ;
