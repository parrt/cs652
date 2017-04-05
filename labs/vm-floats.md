# Adding floats to a virtual machine

In this lab, you are going to augment an existing [simple stack-based virtual machine](https://github.com/parrt/simple-virtual-machine) to support floating-point numbers instead of just integers.

## Getting started

First download the source from the **master** branch of the `simple-virtual-machine` repo. There are three files in `src/vm`

* Bytecode.java The bytecode definitions are in this file
* VM.java The virtual machine itself that performs fetch-decode-execute
* Test.java  A test rig with two small bytecode programs

## Representing floats as ints in Java

The first problem we have to solve relates to the fact that all of our memory is 32-bit word-addressable arrays:
 
```java
int[] code;         // word-addressable code memory but still bytecodes.
int[] globals;      // global variable space
int[] stack;		// Operand stack, grows upwards
```

How can we store floating-point numbers in an `int[]`? We need a trick that you probably didn't know about in Java. Test the following program:

```java
float x = 3.14159f;
int xi = (int)x;
System.out.printf("%f as int is %d\n", x, xi);
int xbits = Float.floatToIntBits(x);
System.out.printf("%f as int is %d (0x%x)\n", x, xbits, xbits);
x = Float.intBitsToFloat(xbits);
System.out.printf("Bits 0x%x as float is %f\n", xbits, x);
```

You should see the following output:

```
3.141590 as int is 3
3.141590 as int is 1078530000 (0x40490fd0)
Bits 0x40490fd0 as float is 3.141590
```

The `Float.floatToIntBits(x)` function is really just a cast because `x` is already represented as a 32-bit floating-point word. We can't cast with `(int)` because that converts the number to an `int`. All we want to do is store floating-point numbers as ints. Then we can use the reciprocal `Float.intBitsToFloat` to get it back as a floating-point number when we need to do arithmetic.

## Adding floating-point instructions

In `Bytecode.java`, add the following definitions:
 
```java
public static final short FADD = 16;     // float add
public static final short FSUB = 17;
public static final short FMUL = 18;
public static final short FLT  = 19;     // float less than
public static final short FEQ  = 20;     // float equal

public static final short HALT = 21;
```

Also update the `Instruction[] instructions` list. Note that the byte code values must be contiguous 1..21.

Now add cases to the decode `switch` of `VM.java`:
