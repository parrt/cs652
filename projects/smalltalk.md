# Smalltalk (*subset*) Compiler and Interpreter

## Goal

This project is to build a full compiler and interpreter / virtual machine (VM) for a subset of Smalltalk.



## Discussion

### Smalltalk language definition

no class variables but allows class methods
we disallow globals. x:=expr will generate code for expr but not the store if x is not a valid argument, local variable, or field. no ';' extended msg send notation, no `#(1 2 3)` array literal notation, but with dynamic array notation `{1. 2. 3}`. Much of the implementation is not exposed to the programmer, such as method invocation contexts.

<table border="0">
<tr><th><b>Syntax</b></th><th><b>Semantics</b></th></tr>
<tr>
<td><pre>nil</pre></td>
<td>undefined object</td>
</tr>
<tr>
<td><pre>true,false</pre></td>
<td>boolean liberals</td>
</tr>
<tr>
<td><pre>self</pre></td>
<td></td>
</tr>
<tr>
<td><pre>super</pre></td>
<td></td>
</tr>
</table>

<table border="0">
<tr><th><b>Syntax</b></th><th><b>Semantics</b></th></tr>
<tr>
<td><pre>"..."</pre></td>
<td>comment (allowed anywhere)</td>
</tr>
<tr>
<td><pre>'abc'</pre></td>
<td>string literal</td>
</tr>
<tr>
<td><pre>$a</pre></td>
<td>character literal `a`</td>
</tr>
<tr>
<td><pre>123</pre></td>
<td>integer literal</td>
</tr>
<tr>
<td><pre>1.23</pre></td>
<td>floating-point literal (single precision), no scientific notation</td>
</tr>
<tr>
<td><pre>.</pre></td>
<td>expression separator (not terminator)</td>
</tr>
<tr>
<td><pre>x := </pre><i>expr</i></td>
<td>assignment to local or field (there are no global variables)</td>
</tr>
<tr>
<td>^<i>expr</i></td>
<td>return expression from method, even when nested in a `[...]`block</td>
</tr>
<tr>
<td><pre>|x y|</pre></td>
<td>define two local variables or fields</td>
</tr>
<tr>
<td><pre>{a . 1+2 . aList size}</pre></td>
<td>dynamic array constructed from three expressions separated by periods</td>
</tr>
<tr>
<td><pre>[:x | 2*x]</pre></td>
<td>code block taking one parameter and evaluating to twice that parameter; in common usage, of these are called lambdas or closures.</td>
</tr>
<tr>
<td><pre>[:x :y| x*y]</pre></td>
<td>code block taking two parameters</td>
</tr>
</table>

<table border="0">
<tr><th><b>Syntax</b></th><th><b>Semantics</b></th></tr>
<tr>
<td><pre></pre></td>
<td></td>
</tr>
</table>

## Tasks
