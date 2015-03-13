grammar Simple;

file : (func|var)* EOF ;

func : 'def' ID '(' ')' ':' body ;

body : (var|stat)* ;

var  : 'var' ID ;

stat : 'print' ID ;

ID : [a-z]+ ;
WS : [ \r\t\n] -> skip ;