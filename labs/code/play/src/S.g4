grammar S;

code: stat+ ;

stat: 'return' expr ';'
    | ID '=' expr ';'
    ;

expr: expr '*' expr
    | expr '+' expr
    | INT
    | ID
    ;

INT : [0-9]+ ;
ID  : [a-zA-Z]+ ;

WS  : [ \r\n]+ -> skip ;