# PostScript interpreter

In this project, you will build an interpreter for a simplified version of PostScript, but one that can actually draw pictures and text. Your goal is to get the interpreter functional enough to display the following:

![nozzle](images/nozzle.png)

and enough to get the following recursive factorial function to operate properly:

```
/fact {
    1 dict begin                % enter local scope
        /n exch def             % save arg as n
        n 0 eq                  % if n==0
        {1}                     % return 1 by leaving on the stack
        {n n 1 sub fact mul}    % n * fact(n-1)
        ifelse
    end                         % exit local scope
} def
```

Part of the goal for this project is to learn to read code written by others and modify it.  I have largely provided the interpreter for you, but you must complete some of the critical pieces dealing with the language itself. Your first task will be to read through the code and try to understand what it does.

## The overall structure

A main program, `PS.java`, creates a `PSInterpreter` object that creates a visible `JFrame` (a window) that serves as our "printed page." The interpreter is a virtual machine that emulates the architecture of a PS machine. There is an operand stack, a dictionary stack, and a graphics state stack.

The `EPS.g4` grammar parses PS input and triggers calls to Java code that executes instructions. We would call this a syntax directed interpreter--A bit of syntax triggers the appropriate simulation.

All objects in the PS machine are (in)direct subclasses of `PSObject`, with two organizational subclasses: `PSValue` and `PSOperator`. All objects know-how to *execute*:

```java
public abstract class PSObject {
	public abstract void execute(PSInterpreter interpreter);
}
```

Values push themselves onto the stack and operators perform an operation such as drawing or manipulating the stack. Here is the class hierarchy:

![class hierarchy](images/ps-hier.png)

## Tasks

Your starter kit is provided [here](https://github.com/USF-CS652-starterkits/parrt-ps), but I will create repositories for each of you with the content.
 
You must implement the following:

* `PSInterpreter.push()`
* `PSInterpreter.pop()`
* `PSInterpreter.lookup()`
* `PSInterpreter.define()`
* `PSInterpreter.popAndPrint()`; same as `==` operator in interactive GS shell.
* `def` class; pop y then x elements off the stack and map x to y in the current (top of stack) dictionary. This delegates to `PSInterpreter.define()`.
* `PSInt` class; a PSValue that implements an integer. See `PSReal`.
* `begin` class; pop the dictionary object off of the operand stack and pushed onto the dictionary stack.
* `end` class; throw away the top of the dictionary stack.
* `lineto` class (Look in `GraphicsState` for useful methods like `lineto`)
* Fill in the missing grammar action in the second alternative of the loop in rule `exec` from `EPS.g4`:
```
/** Immediate execution mode; e.g., "40 60 add 2 div" executes right away. */
exec returns [int n = 0]
    :   (   instruction {interpreter.execute($instruction.o); $n++;}
        |   '=='        {...}
        )*
    ;
```
* Fill in the missing grammar action in the first alternative:
```
value returns [PSObject v]
    :   INT             {...}
   ...
```

## Building and testing

You can run use `bild.py` to run ANTLR on the grammar, compile everything, and run the factorial test:

```bash
$ ./bild.py test
```

which is just invoking the following after building:

```
$ java -cp out:antlr-4.5-complete.jar cs601.ps.PS tests/fact.ps
```

Look in the `bild.log` file for complete output after a run or failed run of the build script.
 
Here is the [complete output for fact.ps with -trace on](ps-fact-output.txt).
