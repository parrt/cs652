CS652 -- Programming Languages
=====

This is the home page for Computer Science 652, Graduate Programming Languages, at the University of San Francisco.

# Abstract

Because programming languages are at the core of how we communicate with machines, programmers should have a thorough understanding of how languages are designed, implemented, and manipulated. This course concerns itself specifically with the implementation and translation of computer languages, leaving an in-depth study of language design to another course. Students will learn the formalisms behind computer languages, but the focus will be on developing the ability to build languages and their translators.

This class can be challenging conceptually. Some of the language formalisms take a while to sink in. Well, actually you have one major hurdle to get over and then it's easy--abstraction in the sense of recursion, meta-language, programs that generate other programs (or even themselves), etc... If you get a headache when you try to figure out how the first C compiler could have been written in C, you might invest in a big bottle of aspirin.

Students are required to build projects in Java unless specified otherwise. You should have a good understanding of data structures, algorithms, and recursion. Prior experience with language implementation is helpful, but not required. You will be expected to write a lot of code this semester, culminating in a complete programming language implementation.

Two graduate classes is considered full-time at USF and, hence, you can expect this class to require about 20 hours/week of class time and homework/development time. You should start early on every project. Note that there is no such thing as a late project. Late projects get a 0 grade as I will be handing out the solutions the day projects are due.

# Administrivia

**ROOM.** Lo Schiavo Science 307.

**TIME.** MWF 2:15pm - 3:20pm,  January 26 (Mon) - May 13 (Wed).

**EXAMS.** There will be 2 exams but no final exam.

### Instruction Format

Class periods of 1:05min each 3 times per week for 15 weeks. Instructor-student interaction during lecture is encouraged. All programming will be done in the Java programming language, unless otherwise specified.

### Books

The ANTLR 4 reference book is [online](http://0-proquest.safaribooksonline.com.ignacio.usfca.edu/book/programming/9781941222621) for free via USF’s “safari online” as part of your tuition. Log in via the USF library site.

Unfortunately the other book, [Language Implementation Patterns](http://amzn.com/193435645X), is not available for free (not sure why). It’s not required for CS652, but I will closely follow much of that book in class and it’s cheap at $24 in printed form. (A reminder that using an unpaid-for copy of the electronic version is uncool and violates our academic honesty policy and is illegal.)

### Grading

| Artifact | Grade Weight | Due date |
|--------|--------|--------|
|[Java REPL](https://github.com/parrt/cs652/blob/master/projects/Java-REPL.md)| 5%| Feb 4 |
|[PostScript Interpreter](https://github.com/parrt/cs652/blob/master/projects/ps-interp.md) | 2% | Feb 20 |
|[Java->C for vtable](https://github.com/parrt/cs652/blob/master/projects/Java-vtable.md) |10%| Wed, Mar 25 |
|[smalltalk compiler/VM](https://github.com/parrt/cs652/blob/master/projects/smalltalk.md)|	20%| Wed, May 13 |
|Quizzes | 3%| sporadic |
|Exam 1| 30%| Mon, March 30 |
|Exam 2| 30%| Wed, May 13 |

*No final exam, Smalltalk project counts as the final.*

*I expect to see proper git commit messages and github usage so I can track your development.*

I consider an "A" grade to be above and beyond what most students have achieved. A "B" grade is an average grade for a graduate student or what you could call "competence" in a business setting. A "C" grade means that you either did not or could not put forth the effort to achieve competence. An "F" grade implies you did very little work or had great difficulty with the class compared to other students.

Projects that do not run exactly as specified will lose 10% of the total points. Make sure that you do not have hardcoded files/directories in your code, remember that UNIX is case-sensitive as is Java, file names and class names must be correct, specified method signatures must be correct, etc...

Code quality counts. Even if you have perfect functionality, I will deduct points for poor and sloppy coding.

*I will be very strict and set a high standard in my grading*, but I will work hard to help you if you are having trouble. You some of you may not get the grade you were hoping for in this class, but I will do everything I can to make sure you learn a lot and have a satisfying educational experience!

Unless you are sick or have a family emergency, I will not change deadlines for projects nor exam times. For example, I will not give you a special final exam just because you want to fly home early. Consult the university academic calendar before making travel plans.

**ABOUT ME.** My name is Terence Parr and I’m a professor in the computer science department.  Please call me Terence (the use of “Terry” is a capital offense). For more information on me, see http://parrt.cs.usfca.edu.

**TARDINESS.** Please be on time for class. It is a big distraction if you come in late.

**ACADEMIC HONESTY.** You must abide by the copyright laws of the United States and academic honesty policies of USF. You may not copy code from other current or previous students. All suspicious activity will be investigated and, if warranted, passed to the Dean of Sciences for action.  Copying answers or code from other students or sources during a quiz, exam, or for a project is a violation of the university’s honor code and will be treated as such. Plagiarism consists of copying material from any source and passing off that material as your own original work. Plagiarism is plagiarism: it does not matter if the source being copied is on the Internet, from a book or textbook, or from quizzes or problem sets written up by other students.

The golden rule: **You must never represent another person’s work as your own.**

My policy is as follows:

 1. The first observed incident of cheating will result in a zero on the quiz or the assignment (for example). It will be reported to both the CS chair and the CS program assistant for tracking. 
 1. The second observed incident of cheating after the initial incident will result in a failing grade for the course.

If you ever have questions about what constitutes plagiarism, cheating, or academic dishonesty in my course, please feel free to ask me. I’m happy to discuss the issue in a forthright manner.

Official text from USF: *As a Jesuit institution committed to cura personalis—the care and education of the whole person—USF has an obligation to embody and foster the values of honesty and integrity. USF upholds the standards of honesty and integrity from all members of the academic community. All students are expected to know and adhere to the University's Honor Code. You can find the full text of the code [online](http://www.usfca.edu/catalog/policies/honor).*

**ON DISABILITIES.** If you are a student with a disability or disabling condition, or if you think you may have a disability, please contact USF Student Disability Services (SDS) at 415/422-2613 within the first week of class, or immediately upon onset of the disability, to speak with a disability specialist. If you are determined eligible for reasonable accommodations, please meet with your disability specialist so they can arrange to have your accommodation letter sent to me, and we will discuss your needs for this course. For more information, please visit http://www.usfca.edu/sds/ or call 415/422-2613.

# Syllabus

## Programming language concepts

[Language Terminology](https://github.com/parrt/cs652/blob/master/lectures/terminology.md)

Some key lang innovations in common use by programmers	the representation of data in binary
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

[Functional programming and other fun stuff in Python](https://github.com/parrt/cs652/blob/master/lectures/functional-python.md)

**LAB**: [Fun with Python](https://github.com/parrt/cs652/blob/master/labs/functional-python.md)

[The PostScript (PDF) language](https://github.com/parrt/cs652/blob/master/lectures/postscript.md)

### Formal grammars

[grammars](https://github.com/parrt/cs652/blob/master/lectures/grammars.pdf)

Chapter 5 in ANTLR 4 reference

**LAB**: [Grammars](https://github.com/parrt/cs652/blob/master/labs/grammars.md)

## Parsing

[The Quest for the One True Parser](http://www.infoq.com/presentations/parsing-history?utm_source=infoq&utm_medium=QCon_EarlyAccessVideos&utm_campaign=QConSanFrancisco2014)

Chapter 2 in [Language Implementation Patterns](http://www.amazon.com/Language-Implementation-Patterns-Domain-Specific-Programming/dp/193435645X).

Student should read pattern number 2 on LL(1) lexical analysis in chapter 2

**LAB**: [LL(1) Parsing, Lexing](https://github.com/parrt/cs652/blob/master/labs/LL1.md)

Lookahead computation in Pattern 3, chap 2. Then more formally [First and Follow computations](http://www.cs.virginia.edu/~cs415/reading/FirstFollowLL.pdf)

* parsing I
   *	top-down
   *	LL(1), LL(k), lookahead computation

* parsing II
   *	mapping grammars to recursive descent

## Parse Trees

Section 4, 4.1, 4.2, and Pattern 8 in [Language Implementation Patterns](http://www.amazon.com/Language-Implementation-Patterns-Domain-Specific-Programming/dp/193435645X)

Section 5.1, 5.4, and Pattern 13 in [Language Implementation Patterns](http://www.amazon.com/Language-Implementation-Patterns-Domain-Specific-Programming/dp/193435645X)

**LAB**: [Parse Tree construction in an LL(1) Parser](https://github.com/parrt/cs652/blob/master/labs/LL1-trees.md)

Section 2.5 Parse-Tree Listeners and Visitors

Sections 7.2-7.4 Decoupling Grammars from Application-Specific Code in ANTLR 4 reference on Parse tree listeners/visitors.

See grey box titled "Adding fields to nodes via rule arguments and return values" (rougly page 122).

Read chapter 8 on building real line which applications as a contains examples of how to use listeners and visitors.


## Symbol tables

Chapter 6 in [Language Implementation Patterns](http://www.amazon.com/Language-Implementation-Patterns-Domain-Specific-Programming/dp/193435645X)

   * what we track
   * scopes (lexical, dynamic)
   * resolving symbols
   * monolithic scope
   * nested scopes

Section 8.4 has a subsection called a "Crash course in symbol tables" that could also be a useful supplement to Chapter 6 in LIP book.

Chapter 7 in [Language Implementation Patterns](http://www.amazon.com/Language-Implementation-Patterns-Domain-Specific-Programming/dp/193435645X)

   * structs
   * classes

## Model-driven translation

In Language implementation patterns:

* Generally Chapter 11, but specifically 11.3 *Model-driven translation*.
* Then, 11.4 *Constructing a nested output model*.
* Figure 11.8 *Assembling a C file model using Java AST visitor* is particularly illuminating for your vtable project.
* Pattern 31: *Target-specific generator classes* for simple SQL table creation.

[Generating SQL sample code](https://github.com/parrt/cs652/tree/master/lectures/code/genclasses/src).

See Figure 35 -- Assembling a C file model using Java AST visitor in LIP book. check out the associated overall C template.

polymorphic method calls: [vehicle.c](https://github.com/parrt/cs652/blob/master/lectures/code/vtable/vehicle.c).

## Virtual machines

Watch [How to build a virtual machine (video)](https://www.youtube.com/watch?v=OjaAToVkoTw&feature=youtu.be)

Read Chapter 10 in Language implementation patterns. Study the difference between a stack and register machine. Particularly relevant to the Smalltalk project, you want to look at the section on *Storing large constants in the constant pool*. We do something similar with the `push_literal` bytecode.

Study [Simple Virtual Machine (Java)](https://github.com/parrt/simple-virtual-machine):
* [master](https://github.com/parrt/simple-virtual-machine). Basic instructions only (no function calls).
* [add-functions](https://github.com/parrt/simple-virtual-machine/tree/add-functions). Includes CALL/RET instructions, runs factorial test function.
* [split-stack](https://github.com/parrt/simple-virtual-machine/tree/split-stack). Split into operand stack and function call stack. The virtual machines in LIP book have split stacks.
* [func-meta-info](https://github.com/parrt/simple-virtual-machine/tree/func-meta-info).  CALL bytecode instruction takes an index into a metadata table for functions rather than an address and the number of arguments. This makes it much easier for bytecode compiler to generate code because it doesn't need to worry about forward references. This branch also properly allocates space for local variables.

Next, study [Simple Virtual Machine (C)](https://github.com/parrt/simple-virtual-machine-C). Specifically, look at the master branch, which is a C port of [split-stack branch (Java)](https://github.com/parrt/simple-virtual-machine/tree/split-stack). Then, look at the [computed-goto branch (C)](https://github.com/parrt/simple-virtual-machine-C/tree/computed-goto).

Your [Smalltalk compiler and interpreter project](https://github.com/parrt/cs652/blob/master/projects/smalltalk.md) also provides details about how to build a Smalltalk VM.

## Week 9

* Polymorphism
   * via symbol table
   * via vtable

* Translation I
   * syntax directed
   * tree-rewriting

* Translation II
   * rule-based
   * Token stream rewriting

## Week 10

* Translation III
   * model-driven

* Translation IV
   * model-driven continued

* StringTemplate

## Week 11

* Interpreters I
   * syntax-directed

* Interpreters II
   * bytecode
   * stack

* Interpreters III
   * register

## Week 12

* Java VM case study

* LLVM; write some by hand for experimenting.

* Generating bytecode

## Week 13

* smalltalk language

* smalltalk closures in detail I

* smalltalk closures in detail II

## Week 14

* GC I

* GC II

## Finite automata (Optional)

*	NFA
*	DFA
*	grammar -> NFA

* lexing
   *	with DFA
   *	show recursive descent version


## Optional typing

* static typing I
   * dynamic vs static typing
   * computing types

* static typing II
   * type promotion
   * checking type safety

* static typing III
   * polymorphic type safety
