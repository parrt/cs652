Introduction
I'm using Pharo. It's a bit buggier than Squeak, but it has a nicer interface.

No files; everything is stored within the development environment repository. The legacy of this still seeing in the eclipse development environment, which came from IBM's Smalltalk IDE.
The class browser is like a file system browser. Note the similarity to Mac OS X's finder browser. It's amazing how influential the early days at Xerox Parc continue to be. Using the browser, you can find classes and methods and generally poke around.
Adding classes or methods is a matter of clicking in the right spot in the browser. Class definitions are done with message sent as is just about everything else. Note that our Smalltalk implementation we use a slightly different syntax for class definitions. Instance variables are strings sent via the subclass: method.
There is no main method. We use workspace to "do it", "print it", or "inspect it".
The transcript is like a log. Transcript show: 'hi'..
Syntax
Literals. From ``Squeak by Example,'' by Andrew P. Black, Stéphane Ducasse, Oscar Nierstrasz, Damien Pollet with Damien Cassou and Marcus Denker. Printed page 52 on Smalltalk syntax.

keywords. self, super, nil, true, false (yes, that's it).

local declarations. |x y|

assignments. x := 1.

blocks. [:x | n := n + x.]. These are lexical closures in that they capture all variables visible to them and can access them even if the code block is passed to another function. This is in contrast to just about every other language you've probably looked at. But, it's essentially required to implement Smalltalk. see the discussion below on control structures.

messages

unary. 1 factorial
binary. 1+2
keyword. list at: i put: 'hi'.
precedence. left to right but unary messages have highest precedence then binary then keyword.
1 factorial + 2 factorial.
1 + 2 * 3
list at: 2 factorial put: 3 factorial.
return values from methods. ^expr. or the final expression value in a code block.
Simple example
Create parrt category at the top level of the browser. Then create a sample Test class and put a method inside to count the characters within a string:

countChar: s
    | n |
    n := 0.
    s do: [:c | n := n + 1].
    ^n
Execute by saying following the workspace window.

|t|
t := Test new.
t countChar: 'abc'
Highlight it and say "print it". It will emit 3 to the workspace window.

Creating objects
By convention, Smalltalk class objects receive new messages to create new instances. Also by convention, the basic implementation of new in Object will send message initialize to the newly created object. Here is what the new class method does in my definition of Object:

Object [
  class new [
    "Return a new, initialized object. Without ^, #new returns self,
     which is a Class object. Not what we want. If you override, use ^
     as in '^super new'. Don't call initialize in subclasses otherwise
     it calls initialize twice, once in subclass and once here in
     Object>>new.  You must, however, have initialize call
     super initialize to init all fields up inheritance chain."
    ^self basicNew initialize
  ]
  ...
  class basicNew <primitive:#Object_Class_BASICNEW>
  initialize [ ]
]
Notice that initialize is an instance method the others are class methods.

Blocks and Control structures
Blocks are chunks of code with parameters and return values like anonymous functions. Unlike functions, however, they can see the variables in the surrounding context. In the example from the previous section, notice that the block can access local variable n.

To evaluate a block, we use message value, value:, value:value:, etc... For example,

[Transcript show: 'foo'] value.
[:x | 2*x] value: 10.
[:x :y | x*y] value: 10 value: 20.
conditional statements
boolean-value ifTrue: [what to do if true]

boolean-value ifTrue: [what to do if true] ifFalse: [do if not true]

From "Squeak by example" page 59:

(17 * 13 > 220)
ifTrue: [ 'bigger' ]
ifFalse: [ 'smaller' ]  -->  'bigger'
loops
[condition] whileTrue: [loop code]

From "Squeak by example" page 59:

n := 1.
[ n < 1000 ] whileTrue: [ n := n*2 ].
n --> 1024
There is also whileFalse.

n timesRepeat \[_loop code_]
n to: m do: \[_loop code_]
Most collections implement do: so that we can iterate across their elements. From "Squeak by example" page 59-60: "The most important messages for iterating over collections include do:, collect:, select:, reject:, detect: and inject:into:." and then:

"collect: builds a new collection of the same size, transforming each element.

(1 to: 10) collect: [:each | each*each] --> #(1 4 9 16 25 36 49 64 81 100)
select: and reject: build new collections, each containing a subset of the elements satisfying (or not) the boolean block condition. detect: returns the first element satisfying the condition. Don’t forget that strings are also collections, so you can iterate over all the characters.

'hello there' select: [ :char | char isVowel ]  -->  'eoee'
'hello there' reject: [ :char | char isVowel ]  -->  'hll thr'
'hello there' detect: [ :char | char isVowel ]  -->  $e
Finally, you should be aware that collections also support a functional- style fold operator in the inject:into: method. This lets you generate a cumula- tive result using an expression that starts with a seed value and injects each element of the collection. Sums and products are typical examples.

(1 to: 10) inject: 0 into: [:sum :each | sum + each] --> 55
This is equivalent to 0+1+2+3+4+5+6+7+8+9+10."

Sample linked list
Using the syntax we'll use for class projects:

Collection superClass: #Object [
    size [
        | n |
        n := 0.
        self do: [:each | n := n + 1].
        ^n
    ]
    asString [
        | s |
        s := self className, '('.
        self do: [:v | s := s, ' ', v asString].
        s := s, ' )'.
        ^s
    ]
]
Link superClass: #Object [
    | value link |
    class withValue: v [
        "Call Object>>new, then set value to arg v. same as '^self new' here
         since we don't define new in Link."
        ^super new value: v.
    ]
 
    nextLink [ ^link ]
    nextLink: aLink [ link := aLink ]
    value: v [ value := v. ]
    value [^value]
    asString [^value asString]
]
LinkedList superClass: #Collection [
    | head tail |
    first [^head]
    last [^tail]
    isEmpty [ ^head == nil ]
    add: v [ self addLast: v ]
    addFirst: v []
    addLast: v [
        self isEmpty
            ifTrue:  [ head := Link withValue: v. tail := head ]
            ifFalse: [ tail nextLink: (Link withValue: v). tail := tail nextLink ].
    ]
    removeAllSuchThat: blk [
        self
        do: [:v | (blk value: v) ifTrue: [self remove: v]]
    ]
    do: blk [
        |p|
        p:=head.
        [p~~nil] whileTrue: [ blk value: (p value). p := p nextLink ]
    ]
]
