CS652 -- Programming Languages
=====

This is the home page for Computer Science 652, Graduate Programming Languages, at the University of San Francisco.

# Abstract

Because programming languages are at the core of how we communicate with machines, programmers should have a thorough understanding of how languages are designed, implemented, and manipulated. This course concerns itself specifically with the implementation and translation of computer languages, leaving an in-depth study of language design to another course. Students will learn the formalisms behind computer languages, but the focus will be on developing the ability to build languages and their translators.

This class can be challenging conceptually. Some of the language formalisms take a while to sink in. Well, actually you have one major hurdle to get over and then it's easy--abstraction in the sense of recursion, meta-language, programs that generate other programs (or even themselves), etc... If you get a headache when you try to figure out how the first C compiler could have been written in C, you might invest in a big bottle of aspirin.

Students are required to build projects in Java unless specified otherwise. You should have a good understanding of data structures, algorithms, and recursion. Prior experience with language implementation is helpful, but not required. You will be expected to write a lot of code this semester, culminating in a complete programming language implementation.

Two graduate classes is considered full-time at USF and, hence, you can expect this class to require about 20 hours/week of class time and homework/development time. You should start early on every project. Note that there is no such thing as a late project. Late projects get a 0 grade as I will be handing out the solutions the day projects are due.

# Administrivia

| Artifact | Grade Weight |
|--------|--------|
|Java REPL| 5%|
|PostScript Interpreter | 2% |
|modify java bytecodes |5%|
|Java->C for vtable |8%|
|smalltalk compiler|	10%|
|smalltalk VM |		15%|
|Exam 1| 20%|
|Exam 2| 20%|
|Exam 3| 20%|

*No final exam, Smalltalk project counts as the final.*

# Syllabus

## Week 1

Intro 3 days. Define terms compiler, interp/VM, translation, LLVM, bytecode, GC, closures, language, grammar, Syntax, semantics, scope, concurrency, pre/postfix, recursion, object, idioms, libraries, tools, closures, continuations etc...

Quick survey of languages to show the extent/quirks/concepts:
 *	LISP
 *	Fortran
 *	Prolog
 *	SQL
 *	Forth
 *	Smalltalk
 *	C
 *	C++
 *	Obj-C
 *	Swift
 *	F#
 *	Python
 *	Erlang
 *	PostScript
 *	AppleScript

## Week 2

### formal grammars; 3 days

 *	notation: CFG, yacc, EBNF
 *	hierarchy
 *	derivations
 *	L(G)
 *	parse trees
 *	grammars -> ATN

### grammar properties
 *	ambig
 *	left-recur elim

## Week 3

Some key lang innovations in common use by programmers 	the representation of data in binary
 *	assembly language
 *	Hi-level lang
 *	strong typing, static vs dyn typing
 *	modules
 *	symbol scopes
 *	procedures
 *	recursion
 *	heap vars
 *	type inference
 *	user-defined datatypes
 *	exception handling
 *	dyn mem alloc
 *	closures
 *	continuations
 *	GC
 *	OO, polymorphism/ dynamic dispatch, subtyping
 *	concurrency
 *	BNF
 *	Macros
 *	Operator overloading
 *	Pass by name/value

## Week 4

* Finite automata
   *	NFA
   *	DFA
   *	grammar -> NFA

* lexing
   *	with DFA
   *	show recursive descent version

* parsing I
   *	top-down
   *	LL(1), LL(k), lookahead computation

* parsing II
   *	mapping grammars to recursive descent

## Week 5

* parsing III
   *	bottom-up parsing
   *	predicated parsing
   *	scannerless parsing

* symbol tables I
   *	what we track
   *	scopes (lexical, dynamic)
   *	resolving symbols

* parse tree listeners/visitors

## Week 6

* symbol tables II
   * monolithic scope
   * nested scopes

* symbol tables III
   * smalltalk and closures

* symbol tables IV
   * structs
   * classes

## Week 7

* static typing I
   * dynamic vs static typing
   * computing types

* static typing II
   * type promotion
   * checking type safety

* static typing III
   * polymorphic type safety

## Week 8

* Polymorphism
   * via symbol table
   * via vtable

* Translation I
   * syntax directed
   * tree-rewriting

* Translation II
   * rule-based
   * Token stream rewriting

## Week 9

* Translation III
   * model-driven

* Translation IV
   * model-driven continued

* StringTemplate

## Week 10

* Interpreters I
   * syntax-directed

* Interpreters II
   * bytecode
   * stack

* Interpreters III
   * register

## Week 11

* Java VM case study

* LLVM; write some by hand for experimenting.

* Generating bytecode

## Week 12

* smalltalk language

* smalltalk closures in detail I

* smalltalk closures in detail II

## Week 13

* GC I

* GC II
