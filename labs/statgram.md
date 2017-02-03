# Simple grammar

The goal in this lab is to get a grammar working for a simple programming language.

Create a grammar file called `S.g4`

Make a rule called `stat` that matches return statements of the form `return` *expr* `;` and assignment statements of the form `ID` `=` *expr* `;` Use the following expression rule:

```
expr: expr '*' expr
    | expr '+' expr
    | INT
    | ID
    ;
```

Define lexical rules `INT` and `ID` according to what you see in the ANTLR 4 reference book.  Create a whitespace lexical rule to skip whitespace characters such as space and newline.

Finally, make a rule called `code` that matches one or more `stat`s.

Test your grammar using `code` as the start symbol on input sentences like:

```
x = 3;
return x*10;
```




