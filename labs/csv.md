# A first taste of ANTLR

**Step 1.** Type in the grammar you see in the intellij editor below. Do not cut/paste from elsewhere. Get the syntax "into your fingers". Once you have entered the grammar,  right-click on the `file` rule either in the structure view or in the editor itself. Select "Test rule file". Then you can open the "ANTLR Preview" window and type some, separated values.

<img src=images/csv.png width=500>

**Step 2.** Once you have that working, you will notice that it does not allow spaces. Try `1, 2, 3\n` for example. You get a syntax error.  Add rule

```
WS : ' ' -> skip ;
```

which tells ANTLR to recognize a space character but throw it out. That is, do not send it to the parser as a token.


