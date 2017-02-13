grammar JSON;

json:   object
    |   array
    ;

object
    :   '{' pair (',' pair)* '}'
    |   '{' '}' // empty object
    ;

pair:   STRING ':' value ;

array
    :   '[' value (',' value)* ']'
    |   '[' ']' // empty array
    ;

value
    :   STRING
    |   INT
    |   object  // recursion
    |   array   // recursion
    ;

STRING :  '"' .*? '"' ;

INT :   [0-9]+ ;

WS  :   [ \t\n\r]+ -> skip ;