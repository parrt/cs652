# Smalltalk (*subset*) Compiler and Interpreter

## Goal

This project is to build a full compiler and interpreter / virtual machine (VM) for a subset of Smalltalk.



## Discussion

### Smalltalk language definition

Here is the [formal ANTLR grammar](https://github.com/USF-CS652-starterkits/parrt-smalltalk/blob/master/src/smalltalk/compiler/Smalltalk.g4).

no class variables but allows class methods
we disallow globals. x:=expr will generate code for expr but not the store if x is not a valid argument, local variable, or field. no ';' extended msg send notation, no `#(1 2 3)` array literal notation, but with dynamic array notation `{1. 2. 3}`. Much of the implementation is not exposed to the programmer, such as method invocation contexts.

Paraphrasing the [Pharo cheat sheet](http://files.pharo.org/media/flyer-cheat-sheet.pdf):

#### Literals and keywords
| Syntax | Semantics |
|--------:|--------|
|  `nil` | undefined object |
|`true,false`|boolean liberals|
|`self`|receiver of current message (method call)|
|`super`|receiver of current message but with superclass scope|
|`class`|start of a class definition or class method|
|`"..."`|comment (allowed anywhere)|
|`'abc'`|string literal|
|`$a`|character literal `a`|
|`123`|integer literal|
|`1.23`|floating-point literal (single precision), no scientific notation|

#### Expression syntax

| Syntax | Semantics |
|---------:|--------|
|`.`|expression separator (not terminator)|
|`x := *expr*`|assignment to local or field (there are no global variables)|
|`^`*expr*|return expression from method, even when nested in a `[...]`block|
|`|x y|`|define two local variables or fields|
|`{a. 1+2. aList size}`|dynamic array constructed from three expressions separated by periods|
|`[:x | 2*x]`|code block taking one parameter and evaluating to twice that parameter; in common usage, of these are called lambdas or closures.|
|`[:x :y | x*y]`|code block taking two parameters|
|`[99] value` |use `value` method to evaluate a block with 99|
|`[:x | 2*x] value: 10` |use `value:` method to evaluate a block with parameter 10|

#### Message send expressions

Smalltalk uses the concept of message sending, which is really the same thing as method calling in Java. There are three kinds of messages:

1. **Unary messages**.  These are messages with no arguments. For example, Smalltalk `myList size` is just `myList.size()` in Java. The most common unary message is probably `new`, which creates a new object. For example, `Array new`, which is more or less `new Array()` in Java.
1. **Binary operator messages**.  These messages take one argument and take the form of one or more special symbols. For example, `1+2` is the usual addition and `x->y` creates an association object used by `Dictionary`.
```java
/** "A binary message selector is composed of one or
     two nonalphanumeric characters. The only restriction is
     that the second character cannot be a minus sign."
     BlueBlook p49 in pdf.
 */
bop : (opchar|'-') opchar? ;
opchar
	:	'+'
	|	'/' | '\\'
	|	'*' | '~'
	|	'<' | '>'
	|	'=' | '@' | '%' | '|' | '&' | '?' | ','
	;
```
3. **Keyword messages**. These messages take one or more arguments, but unlike Java, the argument names are used in the call. For example, here's how to execute code block over the values from 1 to 5: `1 to: 5 do: [:i | ...]`.  Method `to:do:` passes the iteration number as an argument to the code block when it evaluates it. Here's how to take the conjunction of two booleans: `true and: false`.

**Precedence**.  Unary operators have higher precedence than binary operators, which have higher precedence than keyword operators.  Operators within the same type (unary, binary, keyword) group left to right. That means that `1+2*3` is `(1+2)*3` not `1+(2*3)` as we use in arithmetic and most other programming languages. Parentheses override the default precedence as usual. Here's an example that uses all three operators:

```
1+2 to: myList size do: [:i | ...]
```

The keyword message `to:do:` has lowest precedence and so `myList size` is evaluated and passed as the `to:` parameter.  Similarly, `1+2` evaluates to 3 and is the receiver of the `to:do:` message. When it's unclear to the reader what the president says, use parentheses.

#### Class, method syntax

Smalltalk has no file format or syntax for class definitions because it was all done in a code browser. In our case, we need a file format and some very simple syntax will suffice. The following class definition for `Array` demonstrates all bits of the syntax:

```
class Array : Collection [
   "An object that represents a Smalltalk array of objects backed by Java class STArray"
   class new [ ^self new: 10 ]
   class new: size <primitive:#Array_Class_NEW>

   size <primitive:#Array_SIZE>
   at: i <primitive:#Array_AT>
   at: i put: v <primitive:#Array_AT_PUT>
   do: blk [
       1 to: self size do: [:i | blk value: (self at: i)].
   ]
]
```

Classes are defined using the `class` keyword followed by the name of the class. If there is a superclass, use a colon followed by the superclass name (`Collection`, in this case). The body of the class is wrapped in square brackets.

Class methods are proceeded with the `class` keyword but are otherwise the same as other methods. Both of the `new` methods are class methods and the second one, `new:` is a primitive that has no Smalltalk implementation. Instead, the VM will look for a `Primitive` called `Array_Class_NEW` and execute the associated `perform` method. The non-primitive class method `new` invokes the primitive `new:` with a parameter of 10.

Method `size` takes no parameters and is primitive. Method `at:` takes one parameter and is primitive.  Method `at:put` takes two parameters and is primitive.  Method `do:` takes one parameter, a code block, and has a Smalltalk implementation.

### Virtual machine

```java
public class VirtualMachine {
	/** The dictionary of global objects including class meta objects */
	public final SystemDictionary systemDict; // singleton

	/** "This is the active context itself. It is either a MethodContext
	 *  or a BlockContext." BlueBook p 605 in pdf.
	 */
	public MethodContext ctx;
```

<center>
<img src="images/smalltalk-notation.png" width=600>
</center>

<img src="images/smalltalk-rules.png" width=800>
</center>

## Tasks
