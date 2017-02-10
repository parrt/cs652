grammar Expr;

s : e ;
e : e '*' e	# Mult
  | e '+' e	# Add
  | INT		# Number
  ;

INT : [0-9]+ ;
WS : [ \r\t\n]+ -> skip ;
