# Smalltalk (*subset*) Bytecode Compiler

## Goal

This project is to build a full compiler for a subset of Smalltalk.

## Smalltalk language definition

Here is the [formal ANTLR grammar](https://github.com/USF-CS652-starterkits/parrt-smalltalk/blob/master/src/smalltalk/compiler/Smalltalk.g4).

Our version of Smalltalk differs from the standard in a few ways:

* no class variables but allows class methods
* subclasses can see the fields of their parent classes; in ST-80 these are private
* we disallow definition of globals variables. x:=expr will generate code for expr but not the store if x is not a valid argument, local variable, or field. There is a `push_global` instruction but it is for looking up class names and other globally-visible symbols (only one so far is `Transcript` object).
* We allow forward refs (refs to classes not yet defined)
* no `#(1 2 3)` array literal notation, but with dynamic array notation `{1. 2. 3}`.
* no ';' extended msg send notation
* Much of the implementation is not exposed to the programmer, such as method invocation contexts.

A useful project with implementation information is [SOM Smalltalk](http://som-st.github.io/).

Paraphrasing the [Pharo cheat sheet](http://files.pharo.org/media/flyer-cheat-sheet.pdf):

### Literals and keywords
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

### Expression syntax

| Syntax | Semantics |
|---------:|--------|
|`.`|expression separator (not terminator)|
|`x :=` *expr*|assignment to local or field (there are no global variables)|
|`^`*expr*|return expression from method, even when nested in a `[...]`block|
|`|x y|`|define two local variables or fields|
|`{a. 1+2. aList size}`|dynamic array constructed from three expressions separated by periods|
|`[:x | 2*x]`|code block taking one parameter and evaluating to twice that parameter; in common usage, these are called lambdas or closures.|
|`[:x :y | x*y]`|code block taking two parameters|
|`[99] value` |use `value` method to evaluate a block with 99|
|`[:x | 2*x] value: 10` |use `value:` method to evaluate a block with parameter 10|

### Message send expressions

Smalltalk uses the concept of message sending, which is really the same thing as method calling in Java. There are three kinds of messages:

1. **Unary messages**.  These are messages with no arguments. For example, Smalltalk `myList size` is just `myList.size()` in Java. The most common unary message is probably `new`, which creates a new object. For example, `Array new`, which is more or less `new Array()` in Java.
2. **Binary operator messages**.  These messages take one argument and take the form of one or more special symbols. For example, `1+2` is the usual addition and `x->y` creates an association object used by `Dictionary`.
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
	
3. **Keyword messages**. These messages take one or more arguments, but unlike Java, the argument names are used in the call. For example, here's how to execute a code block over the values from 1 to 5: `1 to: 5 do: [:i | ...]`.  Method `to:do:` passes the iteration number as an argument to the code block when it evaluates it. Here's how to take the conjunction of two booleans: `true and: false`.

**Precedence**.  Unary operators have higher precedence than binary operators, which have higher precedence than keyword operators.  Operators within the same type (unary, binary, keyword) group left to right. That means that `1+2*3` is `(1+2)*3` not `1+(2*3)` as we use in arithmetic and most other programming languages. Parentheses override the default precedence as usual. Here's an example that uses all three operators:

```
1+2 to: myList size do: [:i | ...]
```

The keyword message `to:do:` has lowest precedence and so `myList size` is evaluated and passed as the `to:` parameter.  Similarly, `1+2` evaluates to 3 and is the receiver of the `to:do:` message. When it's unclear to the reader what the precedence is, use parentheses.

### Class, method syntax

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

Method `size` takes no parameters and is primitive. Method `at:` takes one parameter and is primitive.  Method `at:put:` takes two parameters and is primitive.  Method `do:` takes one parameter, a code block, and has a Smalltalk implementation.

## Bytecode operational semantics

<img src="images/smalltalk-notation.png" width="600" align=middle>

<img src="images/smalltalk-rules.png" width="800" align=middle>

In [Bytecode.java](https://github.com/USF-CS652-starterkits/parrt-smalltalk/blob/master/src/smalltalk/vm/Bytecode.java), you will see the definitions of the various bytecodes. Each instruction above gets its own unique integer "op code". There is also a definition of how many operands and the operand sizes so that we can disassemble code. For example, here is a class with a simple method:

```
class T [
	|x|
	foo [|y| x := y.]
]
```

and the bytecode generated for method `foo`:
```
0000:  push_local     0, 0
0005:  store_field    0
0008:  pop
0009:  self             
0010:  return           
```

The numbers on the left are the byte addresses of the instructions. The first instruction takes five bytes because there is one byte for the [`push_local`](https://github.com/USF-CS652-starterkits/parrt-smalltalk/blob/master/src/smalltalk/vm/Bytecode.java#L66) instruction and [two operands](https://github.com/USF-CS652-starterkits/parrt-smalltalk/blob/master/src/smalltalk/vm/Bytecode.java#L96) that are each two bytes long.

### Primitive methods

Primitive methods  are like `native` methods in java and SmallTalk code can invoke them like regular methods. Although we have a great deal of our Smalltalk implementation in Smalltalk, to bootstrap we ultimately need to execute some Java. Primitive methods are the interface between Smalltalk and the underlying implementation. For example, in `image.st` you'll see a number of primitive methods:

```
class Integer : Number [
   "this object has no fields visible from smalltalk code and is backed by Java class STInteger"
   + y <primitive:#Integer_ADD>
   - y <primitive:#Integer_SUB>
   * y <primitive:#Integer_MULT>
   / y <primitive:#Integer_DIV>
   < y <primitive:#Integer_LT>
   > y <primitive:#Integer_GT>
   <= y <primitive:#Integer_LE>
   >= y <primitive:#Integer_GE>
   = y <primitive:#Integer_EQ>
   to: n do: blk [
       self <= n ifTrue: [blk value: self. self+1 to: n do: blk]
   ]
   hash [ ^self ]
   mod: n <primitive:#Integer_MOD>
]
```

As we know, there is a `STCompiledBlock` for each Smalltalk-based method, such as `hash`, but there is also one for primitive methods. The difference is that `STCompiledBlock` objects for primitive methods have the following field non-null:

primitiveName

### Class methods

Smalltalk has class methods just like Java does and we use the `class` keyword on methods in our Smalltalk to indicate which methods are class methods. As with regular methods, class methods can also have primitive implementations:
 
```
class Object [
    class basicNew <primitive:#Object_Class_BASICNEW>
    class new [ ^self basicNew initialize ]
    initialize ["do nothing by default" ^self]
	...
]
```

Class methods are treated no differently than instance methods except that we turn on `isClassMethod` in `STMethod` and then `STCompiledBlock`.  Class methods only work on Smalltalk `Class` (Java `STMetaClassInfo`) objects but we rely on the programmer to avoid sending class messages to instances (see [testClassMessageOnInstanceError](https://github.com/USF-CS652-starterkits/parrt-smalltalk/blob/master/test/smalltalk/test/TestCore.java#L1022)) and vice versa (see [testInstanceMessageOnClassError](https://github.com/USF-CS652-starterkits/parrt-smalltalk/blob/master/test/smalltalk/test/TestCore.java#L1059)). For example, `Array new` makes sense because `new` is a class method but `x new` for some instance `x` does not. Similarly, `names size` makes sense but `Array size` does not.

Please note that `self` in a class method refers to the class definition object and not an instance of the class.

## Compilation

[Compiler starter kit](https://github.com/USF-CS652-starterkits/parrt-smalltalk/blob/master/src/smalltalk/compiler).

For the constructs as shown below in the compilation rules, use visitor methods to compute the `Code` result for particular construct. As a side effect, you will be tracking literals within each block/method. Further, you will be setting the `compiledBlock` field of each block/method. In a sense, the result of compilation is the decorated scope tree and is represented by the collection of `compiledBlock`s.  But, for clarity, the result of compilation is a list of `STMetaClassObject` objects, one for each class defined in the a symbol table. These will be installed in the `SystemDictionary` of a VM prior to execution:
```java
	/** This method returns the final result of compilation, a list of meta objects.
	 *  Each meta object, {@link STMetaClassObject}, has a list of compiled
	 *  methods which, in turn, contain the list of compiled blocks for nested
	 *  blocks of that method.
	 *
	 *  Convert a symbol table with classes, methods, and compiled code
	 *  (as computed by the compiler) into a list of
	 *  meta-objects ().
	 *
	 *  This method assumes that the compiler has annotated the symbol table
	 *  symbols such as {@link STBlock} with pointers to the
	 *  {@link smalltalk.vm.primitive.STCompiledBlock}s.
	 */
	public static List<STMetaClassObject> getMetaObjects(STSymbolTable symtab) {
		List<STMetaClassObject> metas = new ArrayList<>();
		for (Symbol s : symtab.GLOBALS.getSymbols()) {
			if ( s instanceof STClass) {
				metas.add(new STMetaClassObject(null, (STClass)s));
			}
		}
		return metas;
	}
```

<img src="images/smalltalk-blocks.png" width="700" align=middle>

<img src="images/smalltalk-expr.png" width="700" align=middle>

<img src="images/smalltalk-msgs.png" width="700" align=middle>

### DBG instructions

The DBG instructions informed the VM where in the original Smalltalk source code the following bytecode instruction(s) comes from. The debug information is extremely useful when writing Smalltalk code, although it can be useful when debugging the VM itself. You need to insert DBG instructions in the following locations:

1. `visitMain`. At the end of the body, before pop, self, return.
2. `visitSmalltalkMethodBlock`.  After visiting the children, before the pop, self, ...
3. `visitAssign`

		//At the end
		if (compiler.genDbg) {
		    code = dbg(ctx.start).join(code);
		}
		//before return
4. `visitKeywordSend`

		//After getLiteralIndex()
		if(compiler.genDbg){
		    code = Code.join(code, dbg(ctx.KEYWORD(0).getSymbol()));
		}
		//Before you join code for Send
5. `visitBinaryExpression`

		//After you join code for visitUnaryExpression(1)
		if(compiler.genDbg){
		    code = Code.join(dbg(ctx.bop(i-1).getStart()), code);
		}
		//Before you join code for Send
6. `visitBlock`.

		//After you join code for visitChildren()
		if(compiler.genDbg){
		    code = Code.join(code, dbgAtEndBlock(ctx.stop));
		}
		//Before you join push_block_return
7. `visitEmptyBody`

		//At the end
		if(compiler.genDbg){
		    code = Code.join(code, dbgAtEndBlock(ctx.stop));
		}
		//Before you return
8. `visitReturn`

		//After visitChildren
		if (compiler.genDbg) {
		    e = Code.join(e, dbg(ctx.start)); // put dbg after expression as that is when it executes
		}
		//Before you join code for method_return()
9. `visitUnaryMsgSend`

		//At the end
		if (compiler.genDbg) {
		    code = Code.join(dbg(ctx.ID().getSymbol()), code);
		}
		//Before return

## Tasks

Most of the compiler is given to you, but you need to build the `CodeGenerator` parse tree **visitor**. This is the meat of the compiler.
