CS652 -- Programming Languages
=====

This is the home page for Computer Science 652, Graduate Programming Languages, at the University of San Francisco. [Catalog entry](https://www.usfca.edu/catalog/course/programming-languages)

# Abstract

Because programming languages are at the core of how we communicate with machines, programmers should have a thorough understanding of how languages are designed, implemented, and manipulated. This course concerns itself specifically with the implementation and translation of computer languages, leaving an in-depth study of language design to another course. Students will learn the formalisms behind computer languages, but the focus will be on developing the ability to build languages and their translators.

This class can be challenging conceptually. Some of the language formalisms take a while to sink in. Well, actually you have one major hurdle to get over and then it's easy--abstraction in the sense of recursion, meta-language, programs that generate other programs (or even themselves), etc... If you get a headache when you try to figure out how the first C compiler could have been written in C, you might invest in a big bottle of aspirin.

Students are required to build projects in Java unless specified otherwise. You should have a good understanding of data structures, algorithms, and recursion. Prior experience with language implementation is helpful, but not required. You will be expected to write a lot of code this semester, culminating in a complete programming language implementation.

Two graduate classes is considered full-time at USF and, hence, you can expect this class to require about 20 hours/week of class time and homework/development time. You should start early on every project. Note that there is no such thing as a late project. Late projects get a 0 grade as I will be handing out the solutions the day projects are due.

# Administrivia

**ROOM.** Lo Schiavo Science 307.

**TIME.** MWF 2:15pm - 3:20pm,  January 25 (Mon) - May 11 (Wed).

**EXAMS.** There will be 2 exams and 1 final exam.

## Student evaluation

| Artifact | Grade Weight | Due date |
|--------|--------|--------|
|[Java REPL](https://github.com/parrt/cs652/blob/master/projects/Java-REPL.md)| 3%| Friday, Feb 5, 2016 |
|[C declaration to English translator](https://github.com/parrt/cs345/blob/master/projects/cdecl.md) (borrowed from CS345)|2%| Fri, Feb 12|
|[Java->C for vtable](https://github.com/parrt/cs652/blob/master/projects/vtable.md) |20%| 3 parts|
|&nbsp;&nbsp;&nbsp;&nbsp;[vtable -- Java subset grammar](https://github.com/parrt/cs652/blob/master/projects/vtable-grammar.md) |5%| Fri, Feb 19|
|&nbsp;&nbsp;&nbsp;&nbsp;[vtable -- Semantic analysis](https://github.com/parrt/cs652/blob/master/projects/vtable-symtab.md) |7%| Fri, Mar 4|
|&nbsp;&nbsp;&nbsp;&nbsp;[vtable -- Code generation](https://github.com/parrt/cs652/blob/master/projects/vtable.md) |8%| Fri, Mar 25|
|[smalltalk compiler/VM](https://github.com/parrt/cs652/blob/master/projects/smalltalk.md)| 20%| Wed, Apr 20 |
|Garbage collector| 5% | Fri, May 6|
|Exam 1| 15%| Mon, Feb 29 |
|Exam 2| 15%| Fri, Apr 22 |
|Final| 20%| 3pm Monday, May 16|

## Instruction Format

Class periods of 1:05min each 3 times per week for 15 weeks. Instructor-student interaction during lecture is encouraged. All programming will be done in the Java programming language, unless otherwise specified.

### Books

We will make heavy use of [Language Implementation Patterns](http://amzn.com/193435645X) and, to a lessor extent, [The ANTLR 4 reference book](http://0-proquest.safaribooksonline.com.ignacio.usfca.edu/book/programming/9781941222621) (free via USF’s “safari online” as part of your tuition. Log in via the USF library site.)

Language Implementation Patterns is cheap at $24 in printed form. (A reminder that using an unpaid-for copy of the electronic version is uncool and violates our academic honesty policy and is illegal.)

### Submitting Projects

All projects are due precisely at the start of class on the due date. You do not need, say, two weeks and three minutes. My grading script will pull down the state of your repository at the time class starts.

For repl, vtable, smalltalk projects, I provide you with the complete set of tests your implementation must pass. Passing all tests as specified (i.e., don't modify them) gets you 90%. You get the other 10% after I sign off on your code. If there are changes to be made, I will submit a github *pull request* with comments inline in your code. Merge the pull request and then respond to each comment by fixing your code. Please remove my comments as you make changes.  Please try to submit all of your changes in a single commit beyond the last commit you did for original submission of the project.  Once you have fixed the code or if I did not require any changes, I will mark you down as 100%. Please email me when you have submitted your changes.  **In order for me to get email indicating you've finished,** please use `@parrt` in your commit message or, if you've already committed your finished update, add a comment. For example, `finished responding to @parrt review.`  Be advised that I might require you to make multiple passes over your code to fix things until I'm satisfied.  **Any required changes must be done prior to submission of your next project or the last day of class, whichever comes first.**  I will not do code reviews on incomplete projects so the maximum would be < 90%.

You must submit each project via github using your account and the repository I've created for you in organization [USF-CS652-S16](https://github.com/USF-CS652-S16). The repositories are always named `userid-projectname`. For example, my ps project would be `parrt-ps`.

*I expect to see proper git commit messages and github usage so I can track your development.* Commits must be consistent with developing the software for each project. For example, a single large commit of the entire project at once could be considered circumstantial evidence of academic dishonesty.

Unless you are sick or have a family emergency, I will not change deadlines for projects or exam times. For example, I will not give you a special final exam just because you want to fly home early. Consult the university academic calendar before making travel plans.

### Grading standards

I consider an "A" grade to be above and beyond what most students have achieved. A "B" grade is an average grade for a student or what you could call "competence" in a business setting. A "C" grade means that you either did not or could not put forth the effort to achieve competence. An "F" grade implies you did very little work or had great difficulty with the class compared to other students.

Projects must run exactly as specified.  To make things easier on you, each Java project has a maven build file and each C project has a CMake built file that automates compilation and testing of projects.  Make sure that you do not have hardcoded files/directories in your code, remember that UNIX is case-sensitive as is Java, file names and class names must be correct, specified method signatures must be correct, etc...

Code quality and correctness counts.  I've seen projects that had no actual functionality but, instead, tested for the input and simply printed out the right answer. Such projects get a 0.

The maximum score for perfect functionality is 90%. To get the remaining 10%, you must pass my code review per above. Github will notify you of a *pull request* if I want you to clean your code up.

Projects passing less than all unit tests at the deadline will get a fraction of 90% that represents my estimate of the completed functionality and code quality. No code reviews are done on incomplete projects so the maximum would be < 90%.

*I will be very strict and set a high standard in my grading*, but I will work hard to help you if you are having trouble. You some of you may not get the grade you were hoping for in this class, but I will do everything I can to make sure you learn a lot and have a satisfying educational experience!

**ABOUT ME.** My name is Terence Parr and I’m a professor in the computer science department.  Please call me Terence or Professor (the use of “Terry” is a capital offense). For more information on me, see [http://parrt.cs.usfca.edu](http://parrt.cs.usfca.edu).

**TARDINESS.** Please be on time for class. It is a big distraction if you come in late.

**ACADEMIC HONESTY.** You must abide by the copyright laws of the United States and academic honesty policies of USF. You may not copy code from other current or previous students. All suspicious activity will be investigated and, if warranted, passed to the Dean of Sciences for action.  Copying answers or code from other students or sources during a quiz, exam, or for a project is a violation of the university’s honor code and will be treated as such. Plagiarism consists of copying material from any source and passing off that material as your own original work. Plagiarism is plagiarism: it does not matter if the source being copied is on the Internet, from a book or textbook, or from quizzes or problem sets written up by other students. Giving code or showing code to another student is also considered a violation.

The golden rule: **You must never represent another person’s work as your own.**

My policy is as follows:

 1. The first observed incident of cheating will result in a zero on the quiz or the assignment (for example). It will be reported to both the CS chair and the CS program assistant for tracking. 
 1. The second observed incident of cheating after the initial incident will result in a failing grade for the course.

If you ever have questions about what constitutes plagiarism, cheating, or academic dishonesty in my course, please feel free to ask me. I’m happy to discuss the issue in a forthright manner.

**Note:** Leaving your laptop unattended is a common means for another student to take your work. It is your responsibility to guard your work. Do not leave your printouts laying around or in the trash. *All persons with common code are likely to be considered at fault.*

**Official text from USF**: *As a Jesuit institution committed to cura personalis—the care and education of the whole person—USF has an obligation to embody and foster the values of honesty and integrity. USF upholds the standards of honesty and integrity from all members of the academic community. All students are expected to know and adhere to the University's Honor Code. You can find the full text of the code [online](http://www.usfca.edu/catalog/policies/honor).*

**ON DISABILITIES.** If you are a student with a disability or disabling condition, or if you think you may have a disability, please contact USF Student Disability Services (SDS) at 415/422-2613 within the first week of class, or immediately upon onset of the disability, to speak with a disability specialist. If you are determined eligible for reasonable accommodations, please meet with your disability specialist so they can arrange to have your accommodation letter sent to me, and we will discuss your needs for this course. For more information, please visit http://www.usfca.edu/sds/ or call 415/422-2613.

# Syllabus

## Overview

Describe projects, discuss syllabus

Define key terms (raw notes: [Language Terminology](https://github.com/parrt/cs652/blob/master/lectures/terminology.md))

Chapter 1 from Language Implementation Patterns (LIP)

Chapter 2 in ANTLR 4 reference

## Part I -- Recognition and Tree Construction

Chapter 2 on recursive-descent parsers in [Language Implementation Patterns](http://www.amazon.com/Language-Implementation-Patterns-Domain-Specific-Programming/dp/193435645X).

Chapter 5 in ANTLR 4 reference on grammar patterns

Watch [The Quest for the One True Parser](http://www.infoq.com/presentations/parsing-history?utm_source=infoq&utm_medium=QCon_EarlyAccessVideos&utm_campaign=QConSanFrancisco2014)

Parse Trees & ASTs. Section 4, 4.1, 4.2, and Pattern 8 in [Language Implementation Patterns](http://www.amazon.com/Language-Implementation-Patterns-Domain-Specific-Programming/dp/193435645X)

Tree walkers, order. Section 5.1, 5.4, and Pattern 13 in [Language Implementation Patterns](http://www.amazon.com/Language-Implementation-Patterns-Domain-Specific-Programming/dp/193435645X)

Section 2.5 in ANTLR 4 reference on Parse-Tree Listeners and Visitors

Sections 7.2-7.4 Decoupling Grammars from Application-Specific Code in ANTLR 4 reference on Parse tree listeners/visitors.

See grey box titled "Adding fields to nodes via rule arguments and return values" (rougly page 122).

Read chapter 8 on building real language applications as it contains examples of how to use listeners and visitors.

**LAB**: [Parse Tree construction in an LL(1) Parser](https://github.com/parrt/cs652/blob/master/labs/LL1-trees.md)

**LAB**: [LL(1) Parsing, Lexing](https://github.com/parrt/cs652/blob/master/labs/LL1.md)

**LAB**: [Grammars](https://github.com/parrt/cs652/blob/master/labs/grammars.md)

## Part II -- Semantic Analysis

Chapter 6 in Language Implementation Patterns (LIP)

(ANTLR 4 ref Section 8.4 has a subsection called a "Crash course in symbol tables" that could also be a useful supplement to Chapter 6 in LIP book.)

See [Declarative Name Binding and Scope Rules](http://swerl.tudelft.nl/twiki/pub/Main/TechnicalReports/TUD-SERG-2012-015.pdf) for another description of symbol table management.

Chapter 7 in LIP: structs, classes

Chapter 8 in LIP: type computation, promotion, type checking

## Part III -- Model-driven translation

In Language implementation patterns:

* Generally Chapter 11, but specifically 11.3 *Model-driven translation*.
* Then, 11.4 *Constructing a nested output model*.
* Figure 11.8 *Assembling a C file model using Java AST visitor* is particularly illuminating for your vtable project.
* Pattern 31: *Target-specific generator classes* for simple SQL table creation.

Sample code: [Generating SQL](https://github.com/parrt/cs652/tree/master/lectures/code/genclasses/src).

See Figure 11.8 in LIP book -- Assembling a C file model using Java AST visitor. check out the associated overall C template.

Vtables, polymorphic method calls: [vehicle.c](https://github.com/parrt/cs652/blob/master/lectures/code/vtable/vehicle.c).

## Part IV -- Virtual machines

[The PostScript (PDF) language](https://github.com/parrt/cs652/blob/master/lectures/postscript.md)

Watch [How to build a virtual machine (video)](https://www.youtube.com/watch?v=OjaAToVkoTw&feature=youtu.be)

Read Chapter 10 in Language implementation patterns. Study the difference between a stack and register machine. Particularly relevant to the Smalltalk project, you want to look at the section on *Storing large constants in the constant pool*. We do something similar with the `push_literal` bytecode.

Study [Simple Virtual Machine (Java)](https://github.com/parrt/simple-virtual-machine):

* [master](https://github.com/parrt/simple-virtual-machine). Basic instructions only (no function calls).
* [add-functions](https://github.com/parrt/simple-virtual-machine/tree/add-functions). Includes CALL/RET instructions, runs factorial test function.
* [split-stack](https://github.com/parrt/simple-virtual-machine/tree/split-stack). Split into operand stack and function call stack. The virtual machines in LIP book have split stacks.
* [func-meta-info](https://github.com/parrt/simple-virtual-machine/tree/func-meta-info).  CALL bytecode instruction takes an index into a metadata table for functions rather than an address and the number of arguments. This makes it much easier for bytecode compiler to generate code because it doesn't need to worry about forward references. This branch also properly allocates space for local variables.

Next, study [Simple Virtual Machine (C)](https://github.com/parrt/simple-virtual-machine-C). Specifically, look at the master branch, which is a C port of [split-stack branch (Java)](https://github.com/parrt/simple-virtual-machine/tree/split-stack). Then, look at the [computed-goto branch (C)](https://github.com/parrt/simple-virtual-machine-C/tree/computed-goto).

Your [Smalltalk compiler and interpreter project](https://github.com/parrt/cs652/blob/master/projects/smalltalk.md) also provides details about how to build a Smalltalk VM.

## Part V -- Memory management

malloc/free

automatic garbage collection: mark-and-sweep, mark-and-compact, mark-and-copy (scavenging), generational collectors.

Notes:

* [Allocation](https://github.com/parrt/cs652/blob/master/lectures/alloc.md)
* [Garbage collection](https://github.com/parrt/cs652/blob/master/lectures/gc.md)
* https://courses.engr.illinois.edu/cs241/sp2011/lectures/21-MemoryAlloc.pdf (re: **Malloc/free**)
* http://g.oswego.edu/dl/html/malloc.html
* http://www.inf.udec.cl/~leo/Malloc_tutorial.pdf
* http://danluu.com/malloc-tutorial/
* http://goog-perftools.sourceforge.net/doc/tcmalloc.html

## Misc

[JNI](https://github.com/parrt/cs652/blob/master/lectures/jni.md); bridging languages

[Functional programming and other fun stuff in Python](https://github.com/parrt/cs652/blob/master/lectures/functional-python.md)

**LAB**: [Fun with Python](https://github.com/parrt/cs652/blob/master/labs/functional-python.md)
