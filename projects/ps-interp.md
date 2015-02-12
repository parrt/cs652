# PostScript interpreter

In this project, you will build an interpreter for a simplified version of PostScript, but one that can actually draw pictures and text. Your goal is to get the interpreter functional enough to display the following:

![nozzle](images/nozzle.png)

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

You must implement the following methods:

* `PSInterpreter.lookup()`

