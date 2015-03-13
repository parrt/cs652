grammar Simple;

@header {
import org.antlr.symbols.*;
}

file returns [Scope scope]
  : (func|var)* EOF
  ;

func returns [Scope scope]
  : 'def' name=ID '(' arg (',' arg)* ')' ':' block
  ;

arg : ID ;

body : (var|stat)* ;

block returns [Scope scope]
     : '[' body ']'
     ;

var  : 'var' ID ;

stat : 'print' ID | block ;

ID : [a-z]+ ;
WS : [ \r\t\n] -> skip ;