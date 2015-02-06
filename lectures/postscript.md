# PostScript case study

## Introduction

In a previous lab you explored a very conventional stack-machine interpreter--one that emulates typical computer architecture with memory, a stack, and instructions that manipulate them both. To avoid having to do register allocation when generating these instructions (which we did by hand), we used instructions that operated on the stack rather than in registers. The syntax was fairly simple and fixed:

```
.decl x
main:
; x = foo(20)
	const 20
	call foo
	store x
	halt
```

There was no way, for example, to introduce a new byte-code instruction and then use it. You can in fact see the above instructions as keywords in a very low-level language. When you see halt, you know precisely what that means.
The interpreter knows nothing of symbols, the assembler actually translates the symbols to physical addresses before execution. The byte-code interpreter really is simulating a traditional CPU.
In this lecture, we will explore another interpreter architecture, again based on stack operations, but one that has no keywords! I am referring to PostScript, a page description language, created by Adobe Systems circa 1985. PDF (Acrobat) files are essentially a compressed, restricted form of PostScript, so postscript is an actively-used programming language. It is worthwhile learning how its interpreter works and how to code a little PostScript to impress your friends. The most interesting feature of PostScript is that it's a programming language, not a data file.
Postscript is very similar to the [Forth programming language](http://www.forth.org/) and is in the class of languages like SmallTalk and LISP that use programming language constructs to extend the language. Forth could add new operations at will, SmallTalk could add a new instance variable by calling a method (addInstanceVar or some such), a LISP program is essentially just data so you can do whatever you want. Java goes part of the way, by letting you query object definitions at run-time, but you cannot add an instance variable at run-time.

Just to give you a taste of the project associated with this lecture, you will build an interpreter for a simplified version of PostScript, but one that can actually draw pictures and so on. Your goal will be to get the interpreter functional enough to display the following:

![nozzle](images/nozzle.png )

## Crash-course on PostScript

See:

* [An introduction to PostScript](http://homepage.mac.com/andykopra/pdm/tutorials/an_introduction_to_postscript.html)
* [The Blue Book](http://www-cdf.fnal.gov/offline/PostScript/BLUEBOOK.PDF)
* [Introduction to Postscript](http://www.eecs.wsu.edu/~hauser/teaching/Languages-F04/lectures/postscript.html)

A PostScript program must begin with a magic line and generally has a trailer that indicates "end of program":
```
%!PS-Adobe-3.0
... instructions ...
%%EOF
```
Single-line comments start with % and, hence, these "commands" are just special comments.
The grammatical structure of the language is so simple that you can summarize the executable instructions in one rule:
```
instruction : element* ;
```
where `element` can be code block names like `moveto`. The operand elements are typed: strings, integers, reals, identifiers, boolean (with true / false literals) or groups of operands. For example, here are some simple instructions:

```
% this is a comment
(this is a string) length     % (x) implies x is a string
5 2 add
[10 20 30] 1 get              % yields 20 (leaves on stack)
1.5 2 lt                      % yields true
```
For testing, you can use the == operator that pops and prints the top of the stack (see more debugging commands below)
```
1.5 ==                        % displays 1.5 to standard out
```
What about control structures? A byte-code interpreter does branches. PostScript is higher level and just uses another operator (if):
```
3 4 lt {(it works!) ==} if
```
In other words, if takes two operands on the stack: a boolean value (here computed by 3 4 lt "is 3 less than 4") followed by a chunk of code to execute. Here, the code chunk displays "it works!" at the current x, y location. These chunks are sequences of operands and operators enclosed in curlies; postscript calls them procedures.
You will see this again in SmallTalk.
Even with the simplicity of these generic instructions, postscript can define subroutines and call them! For example, here is a postscript program that defines a procedure called average and uses it to average two numbers:
```
/average {add 2 div} def
20 40 average
```

The result on the top of the stack would be 30.0 .
Nothing is special in postscript--you can redefine showpage to be an empty operation. This is useful for preventing encapsulated (embedded) postscript files from performing illegal operations:

```
% showpage normal here...
begin                  % begin local scope
/showpage { } def      % redefine showpage to be empty procedure
% showpage doesn't do anything anymore
...embedded postscript...
end                    % exit local scope
% showpage works again
```

For completeness, let me mention that PostScript uses late binding in general. For example, the following procedure references two operators, add and div :
```
/average {add 2 div} def
```

Upon each invocation of add and div in average , the interpreter must look up their definitions. Those operators are not keywords and you might have redefined them. You could, for example, have add redefine itself to be minus so that the next computation of average is wrong. To override this lazy evaluation, use bind to bind names to objects at def -time; during execution time, add and div are not looked up:

```
/average {add 2 div} bind def
```

This is like static versus dynamic binding of functions in C++ version Python. 

## Encapsulated PS
An encapsulated postscript file is simply a postscript file with a bounding box specification and a slight change to the magic first line (one must admit that I don't think the EPSF-3.0 is necessary):

```
%!PS-Adobe-3.0 EPSF-3.0
%%BoundingBox: 10 10 100 30
...
showpage
%%EOF
```

This basically restricts the region of the page that the postscript file writes into. This is used for embedding postscript files in other documents.

## Machine architecture

A postscript interpreter is a stack-based calculator that manipulates familiar objects such as strings, numbers, and arrays. But, since programs are just data to postscript, operators and procedures are also objects that you can manipulate.
A postscript program is a sequence of objects. When the interpreter encounters (executes) an operand, it pushes that object on the stack. When it sees a name, the interpreter looks up the definition and executes the associated object (which might be an integer or code chunk). Executing a name associated with a procedure is like calling a subprogram as execution of the current stream is temporarily suspended until the procedure's code chunk can be executed.
### Tokens
For our purposes, you will need to handle the following symbols:

| Symbol | Meaning |
|--------|--------|
|  (*some text*)      | *string*        |
|1.2 +2 6 -1.05 | *numbers* |
|[20 3 100 20] | *arrays* |
|{add 2 div} | *procedure*. code chunk |
|moveto average foo | *names*. Names do not have values, but are rather associated with a value in a dictionary |
|/average | *literal*. Do not evaluate name (do not look up in dictionary), treat it as data; it is like a pointer to the value |

### Dictionaries

A dictionary is just a table that maps a key to a value. You can use them to associate a name with an object like an integer or array. Use the def operator to map a value (a name literal) with a value:

```
/Version 1.0 def
/foo [1 2 3] def
```

Works with procedures too:
```
/average {add 2 div}
def
```

To get an element of an array use array index get.
The proper way to think about the above instructions is:

*push literal symbol name*
*push procedure object*
*execute dictionary operator* `def`

To look up a symbol, just say its name: `Version`, which would push `1.0` on the stack, for example.

Dictionaries are like scopes. There is a stack of dictionaries. First the system dictionary is pushed then the user dictionary. Symbols are looked up moving from top of stack down to the system dictionary. So, if you want another scope, just push another dictionary. These are like global, function, local nested scopes in C. To do locals in a function, do this:

```
/f {
  5 dict          % make space for 5 locals
  begin           % push dict on dict stack
    ...           % def values in local dict
  end             % pop dict, back to previous "scope"
} def
```

### Stacks

PostScript has four stacks:

1. operand stack. Where operands and results are pushed/popped.
1. dictionary stack. Like the nested scopes of a conventional language.
1. execution stack. Holds procedure invocation stack frames.
1. graphics state stack. Used to save/restore the graphics state before/after chunks of postscript that you want to only temporarily modify the graphics state.

### GhostScript in interactive mode

When exploring PostScript, it is useful to use the [GhostScript](http://www.ghostscript.com/) interpreter, which you can install with `brew install ghostscript` on mac. (If you get an error that it cannot find an image/lib, see the [fix](http://stackoverflow.com/questions/24690800/imagemagick-ghostscript-dyld-library-not-loaded-usr-local-lib-libjbig2dec)). On the other hand, I found that it wasn't working for me, possibly because of an X11 issue.  I installed [Richard Koch's GhostScript package](http://pages.uoregon.edu/koch/) and it worked. You will find the following commands useful:

* `=`: pop something off the stack and print a text representation.
* `==`: pop something off the stack and print a text representation, but print data structures and elements in PS syntax.
* `stack`: show the entire stack contents using `=`.
* `pstack`: show the entire stack contents using `==`.

### Text

To display text in PostScript, you must set a font and then use the show command on a text operand (assuming the current cursor is set to some x, y coordinate):

```
%!PS-Adobe-2.0
/Helvetica findfont
12 scalefont setfont
300 300 moveto
(hi Alex) show
showpage
%%EOF
```

### Graphics

Coordinate 0, 0 is the lower left of the page. here is a simple file that says hi Alex in a box:

```
%!PS-Adobe-3.0 EPSF-3.0
%%BoundingBox: 10 10 100 30
/Helvetica findfont
24 scalefont setfont
10 10 moveto
(hi Alex) show
10 10 moveto
10 30 lineto
100 30 lineto
100 10 lineto
10 10 lineto
stroke
showpage
%%EOF
```

The encapsulated PS image looks like:
