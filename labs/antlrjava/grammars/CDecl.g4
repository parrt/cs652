grammar CDecl;

declaration
    :   typename declarator ';'
    ;

typename
    :   'void'
    |   'float'
    |   'int'
    |	ID
    ;

declarator
    :   declarator '[' ']'		# Array		// right operators have highest precedence
    |   declarator '(' ')'		# Func
    |	'*' declarator			# Pointer
	|   '(' declarator ')'		# Grouping
    |	ID						# Var
    ;

// the following also would work but with less cool trees

declaration2
    :   typename declarator2 ';'
    ;

declarator2
    :   (  '*' declarator2
        |  '(' declarator2 ')'
        |  ID
        )
        (  '[' ']'
        |  '(' ')'
        )*
    ;

ID : [a-zA-Z_]* [a-zA-Z0-9_]+ ;
WS : [ \t\n\r]+ -> skip ;