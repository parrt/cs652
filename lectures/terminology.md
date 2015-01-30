# Computer language terminology, technology

Define terms compiler, interp/VM, translation, LLVM, bytecode, GC, closures, language, grammar, Syntax, semantics, scope, concurrency, pre/postfix, recursion, object, idioms, libraries, tools, closures, continuations etc...

## Programming language abstraction categories

Paraphrasing [Conception, evolution, and application of functional programming languages](http://portal.acm.org/citation.cfm?id=72554) by Paul Hudak with a few thoughts of my own:

* *imperative programming languages*: one focuses on altering state, i.e. side effects, by executing commands. there is a notion of sequence. there is a tight relationship between the CPUs program counter and flow through the program. most languages in use today are imperative. here is the quintessential imperative statement:
`x = y;`
it is a statement with side effects*. Control assumed to flow to the statement following it.
* *declarative languages: have no implicit state. you don't worry so much about how something is computed, focusing instead on terms and expressions that encode rules are computations you would like to express. looping is done with recursion rather than loops. PROLOG is a good example of a declarative language; basic model is a set of relations. Sort of the data flow model. Variables of expressions are filled in when they are available. A dataflow computer is a set of functional units with data dependencies. SQL is another example of a declarative language.
* *functional programming languages*: all about evaluating expressions and are a kind of declarative language. LISP is the most famous and has been in use for 50 years. LISP says what not how. Instead of the assignment or branch statement like in an imperative language, the key model of computation is based upon the function. The result of a computation is the value of the program; you do not look in a memory location for the result like you would with an imperative language. Functions are treated as first-class objects, so you can pass them around like any other object. so-called "higher-order functions." higher order functions provide greater separation because they can serve as loop to tie to existing modules together. this is sort of like passing a code snippet to the sort mechanism so that it knows how to sort any kind of element without having to modify that element with an equals() method. Functional languages also typically have very powerful pattern matching mechanisms and lazy evaluation. pattern matching is like method overloading except based upon the structure of the incoming data element rather than simply its type. Without side effects, threading and parallel processing is trivial.

Imagine taking Java to LISP (imperative to functional): remove assignments and any other side effect inducing operations. As Hudak points out, however, unless that language is Python or Ruby the language is not particularly useful because most imperative languages lack any sort of nice functional programming elements.

**Immutable**==**pure functional** and mutable objects.  Pure functional languages have purely immutable objects.

## C compilation stuff

```c
#include <stdio.h>
void f(int x) { printf("%d\n",x); }
void g(int *x) { printf("%d\n",*x); }
void foo() {
       int x = 99;
       f(x);
       g(&x);
}
```

You can run

```bash
cc -E t.c
cc -O -S t.c
```

on ARM chip and it makes it clear what's going on.

```c
#include <stdio.h>
int main(int argc, char **argv) {
	printf("Hello\n");
}
```

$ file hello.c
hello.c: ASCII c program text

cc -E

file /usr/lib/libc.dylib 
/usr/lib/libc.dylib: Mach-O universal binary with 2 architectures
/usr/lib/libc.dylib (for architecture x86_64):	Mach-O 64-bit dynamically linked shared library x86_64
/usr/lib/libc.dylib (for architecture i386):	Mach-O dynamically linked shared library i386

$ gnm /usr/lib/libc.dylib 

i386:x86-64:
0000000000001ac3 T R8289209$_close
0000000000001ac8 T R8289209$_fork
0000000000001acd T R8289209$_fsync
0000000000001ad2 T R8289209$_getattrlist
0000000000001ad7 T R8289209$_getrlimit
0000000000001adc T R8289209$_getxattr
...

cc -c hello.c
gives hello.o

ELF files are Executable Linkable Format

$ file a.out
a.out: Mach-O 64-bit executable x86_64
maniac:master:~/github/cs652/lectures/code $ file hello.o
hello.o: Mach-O 64-bit object x86_64

cc hello.c

gives a.out.

```
$ gobjdump -d hello.o

hello.o:     file format mach-o-x86-64


Disassembly of section .text:

0000000000000000 <_main>:
   0:	55                   	push   %rbp
   1:	48 89 e5             	mov    %rsp,%rbp
   4:	48 83 ec 20          	sub    $0x20,%rsp
   8:	48 8d 05 00 00 00 00 	lea    0x0(%rip),%rax        # f <_main+0xf>
   f:	89 7d fc             	mov    %edi,-0x4(%rbp)
  12:	48 89 75 f0          	mov    %rsi,-0x10(%rbp)
  16:	48 89 c7             	mov    %rax,%rdi
  19:	b0 00                	mov    $0x0,%al
  1b:	e8 00 00 00 00       	callq  20 <_main+0x20>
  20:	b9 00 00 00 00       	mov    $0x0,%ecx
  25:	89 45 ec             	mov    %eax,-0x14(%rbp)
  28:	89 c8                	mov    %ecx,%eax
  2a:	48 83 c4 20          	add    $0x20,%rsp
  2e:	5d                   	pop    %rbp
  2f:	c3                   	retq 
```

versus

```
$ cc -O -c hello.c
maniac:master:~/github/cs652/lectures/code $ gobjdump -d hello.o

hello.o:     file format mach-o-x86-64


Disassembly of section .text:

0000000000000000 <_main>:
   0:	55                   	push   %rbp
   1:	48 89 e5             	mov    %rsp,%rbp
   4:	48 8d 3d 00 00 00 00 	lea    0x0(%rip),%rdi        # b <_main+0xb>
   b:	e8 00 00 00 00       	callq  10 <_main+0x10>
  10:	31 c0                	xor    %eax,%eax
  12:	5d                   	pop    %rbp
  13:	c3                   	retq   
```

https://sourceware.org/binutils/

```bash
$ nm hello.o
0000000000000058 s EH_frame0
0000000000000030 s L_.str
0000000000000000 T _main
0000000000000070 S _main.eh
                 U _printf

$ brew install binutils
```

```
$ gdb a.out
(gdb) disass main
Dump of assembler code for function main:
   0x0000000100000f60 <+0>:	push   %rbp
   0x0000000100000f61 <+1>:	mov    %rsp,%rbp
   0x0000000100000f64 <+4>:	lea    0x2b(%rip),%rdi        # 0x100000f96
   0x0000000100000f6b <+11>:	callq  0x100000f74
   0x0000000100000f70 <+16>:	xor    %eax,%eax
   0x0000000100000f72 <+18>:	pop    %rbp
   0x0000000100000f73 <+19>:	retq   
End of assembler dump.
```

```
$ otool -L a.out # show shared libs
a.out:
	/usr/lib/libSystem.B.dylib (compatibility version 1.0.0, current version 1197.1.1)
```
this works:
$ ld -arch x86_64 -macosx_version_min 10.9.0 -o a.out hello.o -lSystem

C phases:
http://www.cs.swarthmore.edu/~newhall/unixhelp/compilecycle.html

# Execution strategies

 * bytecode interpreters; UCSD and Berkeley Pascal, JavaScript, Java ≤1.2, Python, Ruby 1.9, Smalltalk-80, Visual Basic ≤4
 * tree-based interpreters; Perl ≤5, Ruby ≤1.8
 * Native code compilers; static C, C++, Visual Basic 5 and 6
 * Bytecode+dynamic compilation; Java ≥1.3, C#, Self, Smalltalk, Visual Basic .NET
 * Translate one language to another; `cfront`

# Java

```bash
$ javac Hello.java
$ javap Hello.class
Compiled from "Hello.java"
class Hello {
  Hello();
  public static void main(java.lang.String[]);
}
```

```bash
$ javap -c Hello.class
Compiled from "Hello.java"
class Hello {
  Hello();
    Code:
       0: aload_0       
       1: invokespecial #1                  // Method java/lang/Object."<init>":()V
       4: return        

  public static void main(java.lang.String[]);
    Code:
       0: getstatic     #2                  // Field java/lang/System.out:Ljava/io/PrintStream;
       3: ldc           #3                  // String Hello World!
       5: invokevirtual #4                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
       8: return        
}
```

other interps: dalvik, llvm, [lua vm](http://www.jucs.org/jucs_11_7/the_implementation_of_lua/jucs_11_7_1159_1176_defigueiredo.pdf)

# back to terms

**Recursion**

A function that eventually calls itself. Requires the recursive leap of faith. To implement a requires a stack because we need to save the return address. Fortran originally did not have this; Fortran 90 and beyond have it.

Here is the typical pattern

```
void f(args) {
   ... // a check for termination condition
   f(args'); //recursive invocation with tweaked args
}
```

Is very useful for self similar data structures such as binary trees, graphs.

*tail recursion*


# Scoping stuff

[Scoping](http://en.wikipedia.org/wiki/Scope_%28computer_science%29)

**Scope** Strictly speaking, this is the part of a program in which a symbol of visible but we typically say "x is *in scope*." More typically, a scope is a chunk of a program like a class, function, or block that can define new symbols.

See chapter 6 in LIP (Language implementation patterns)

In practice, *in scope* means visible and *scope* means lexical chunk of stuff that surround symbol definitions.

There is *static/lexical scoping* and *dynamic scoping*. Static scoping just means defined at static / non-runtime but dynamic scoping means a set of visible symbols can change at run time.

**Symbols** are just identifiers of program entities like variables, functions, and class names.

dynamic scoping: early LISP, perl; template engines. For example you might want to see class name or filename within a deeply nested template that you inject to emit a function call or something. In other words, the chain of functions or templates that call you provide a set of symbols that is different according to the call stack.

We **resolve** symbols by more or less just looking backwards but it is complicated by: closures, inheritance, forward references.

**enclosing or nested scopes**

```java
class T {
	void f(int i) {
		...
		if (...) {
			int i; // can hide stuff in outer scopes
		}
	}
}
```
basically we have a stack of scopes.

In scope just means symbol is visible in the current context or environment.

**Hiding**. Nested or enclosing scope can hide symbols

History: LISP in 1960 had dynamic scoping but later versions and Scheme used static scoping. Just about all of the languages use static scoping, which was first introduced in Algol 60. Is generally considered a bad idea to have dynamic scoping because it's hard to figure out what's going on. On the other hand, I find it very useful for templates.

## Closures

First,

[Higher-order functions](http://en.wikipedia.org/wiki/Higher-order_function) Functions that take other functions as parameters or return them.

```python
>>> def f():
...     def g(): print "hi"
...     return g
...
>>> f()()
hi
```

This requires that functions be **first-class functions** or **first-class objects** in that we can treat them like entities and pass them around in variables.

[Closures](http://en.wikipedia.org/wiki/Closure_%28computer_programming%29), see [Neal Gafter on closures](http://gafter.blogspot.com/2007/01/definition-of-closures.html).

Introduced in the 1960s and typically used by functional languages and is not available in typical imperative languages like Algol, C, pascal, Java &lt; 8.

Just a code block we can pass around, which could be a function or just a little code block. The key is that *it keeps its context or environment* so that he can still reference any symbols that are visible at its definition point. The term itself comes from the fact that it captures

A simple function pointer like in C does not carry its context around.

```
names do: [n | Transcript show: n].
```

```java
time({...});
time() {...}; // same
```

It's important to note that the set of visible symbols does not change at run time; it is still statically scoped. We can pass that closure around anywhere we want and it still can't see anything new nor can it lose anything.

Here's a Python example:

```python
>>> def f():
...     x = 3
...     def g(): print x # accesses x even though we call this function from outside
...     return g
...
>>> f()()
3
>>> g = f()
>>> g()
3
```

You might hear the term **free variable**; that's just a variable that's not a parameter or local variable of that functional block; They are bound globals or fields or something enclosing the functional block. You can think of free variable as *nonlocal reference*.

The term **lambda expression** is often used synonymously with closure since lambda expressions are anonymous function objects that also can access all variables in scope regardless of where you pass that lambda expression / lambda function.

```python
>>> dub = lambda x : 2*x
>>> dub(3)
6
>>> x = 3
>>> dub = lambda : 2*x # capture x location
>>> dub()
6
```

```java
// assume map takes a list and a lambda expression
X = map(points, p -> p.getX()); // get all X coordinates from list points
```

## Continuations

Wikipedia defines [continuations](http://en.wikipedia.org/wiki/Continuation) as:

<blockquote>
The continuation is a data structure that represents the computational process at a given point in the process's execution; the created data structure can be accessed by the programming language, instead of being hidden in the runtime environment.
</blockquote>

It's a way to stop a program, record where it sat, and make it available as an object. That way, we can restart the program from where it left off. C did this with `setjmp()`/`longjmp()`.  It would record the current stack, registers, program counter, complete with arguments and local variables and you could use it to restart/restore later with `longjmp()`. Of course it did not make a copy of the entire state, such as the global variables or the disk.

Continuations are often used to create [generators](http://en.wikipedia.org/wiki/Generator_%28computer_programming%29).

## typing

The following terms and definitions are pretty good ones I think, but there seems to be no consensus on their usage. Stick with the following.

* *static typing*. Pascal, C, C++, Java. The type of every variable must be specified/known at compile time. Helps reduce errors and often helps in efficiency, is pretty annoying. Often leads to lots of typecasts. *Type Inference* helps a lot here.  Just because a language is statically typed doesn't mean you need to specify every type manually; that would be *explicit typing*.
* *dynamic typing*. Python, Ruby, LISP. Typically you do not specify types of variables; run-time checks verify that the appropriate types are used. For example, in a dynamically typed language you can say x.close() even if you know that x is an integer; at run time it will throw an exception. "Duck typing" is really just dynamic typing.
* *weak typing*. Most languages have a few innocuous cases of weak typing such as char, int, and sometimes float being somewhat interchangeable. Some languages are pretty loose with types such as Perl and BASIC. You can, for example, add to strings that have numbers in them and store into an integer.
* *strong typing*. Most commonly-used languages are strongly typed I would say. Basically means "type safety" or data elements of unknown type tend not to float automatically to other types; i.e., very little implicit conversion of types or least very few ways to cheat the system. See http://en.wikipedia.org/wiki/Strong_typing.
* *untyped*: assembly language. explicit conversion is required and there is no type checking at compile time or runtime.

[strong versus weak typing](http://en.wikipedia.org/wiki/Strong_and_weak_typing). Assembly is untyped. perl is weak. Most languages are strongly typed.  Strong is typically f(3) when f takes a string argument.  Note that it is not considered weak typing if you have **type promotion** where `2+"3"` is 23. However, if you have `2+"3"` giving 5, is weakly typed. Perl and PHP would do this.

## Memory systems

* *static, global data*. Fortran and COBOL
* *locals and recursion*. Waited for introduction of hardware stack with the exception of LISP as far as I know. Note that CDC6000 machine on which I programmed had no hardware stack. You made a function call by storing a jump instruction to the return address at the first word(s) of the function you are calling. To return was to jump to the first instruction of the function. Weird.
* *dynamic memory allocation*
* *garbage collection* The notion of automatic garbage collection just means that you don't have to worry about freeing memory that you no longer need. Of course even in languages with automatic garbage collection one needs to make sure that variables either go out of scope or that we set pointers to null. Here's how it works: At some frequency:
  1. distinguish live from dead objects (tracing) and reclaim dead ones.
  1. Live objects are reachable from roots, which are globals, locals, registers of active methods etc... quick example **heap** before and after collection.

* [lazy evaluation](http://en.wikipedia.org/wiki/Lazy_evaluation) means you don't have to care about the order in which subexpressions are evaluated.
* *software transactional memories*: like transactions for a database. You can unroll a whole series of memory changes. Can be used to allow optimistic parallel processing and then roll back upon race or collision. As computers get faster and bigger and more parallel, we can literally record every single change made by a program.

## Misc

Hardware support for programming languages. Alan Kay: "it took years to get even rudimentary stack mechanisms into CPUs. Most machines still have no support for dynamic allocation and garbage collection and so forth."

You should know about Xerox PARC(Palo Alto research Center) whence: laser printer, ethernet, GUI windowing systems ("borrowed" by Jobs for Mac. Gates "borrowed" from Jobs to make windows. Gates joked about Jobs saying Gates had stolen the idea of Windows. Gates said that it was like accusing him of going into a house to steal the television only to find that Jobs in the process of stealing it), smalltalk. cedar/mesa project that I believe introduced exceptions and also the concept of a monitor from which Java's threat mechanism was derived.  PARC was first to really use the mouse ([Doug Engelbart's mouse patent diagram](http://upload.wikimedia.org/wikipedia/commons/e/ed/Mouse-patents-englebart-rid.png)).  Perhaps most famous guy is [Alan Kay](http://en.wikipedia.org/wiki/Alan_Kay) (smalltalk guy). Chuck Geschke, the USF trustee, was that PARC. He  cofounded Adobe.  He set next to Bob Metcalfe, who started 3Com and coinvented ethernet.