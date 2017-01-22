# Translating C declarations to English


## Goal

In this project, you will implement a small translator that recognizes a subset of C declarations and prints out the equivalent English meaning. For example, given input

```C
int *a[];
```

your translator must emit:

```
a is a array of pointer to int
```

You must fill in [Tool.java](https://github.com/USF-CS652-starterkits/parrt-cdecl/blob/master/src/cs652/cdecl/Tool.java) and the parse tree visitor [EnglishGenerator.java](https://github.com/USF-CS652-starterkits/parrt-cdecl/blob/master/src/cs652/cdecl/EnglishGenerator.java) in the starter kit.

Before you panic, note that my solution to the translator itself has 9 lines of Java code that I had to write myself. ANTLR and Intellij wrote the rest.  My solution for `Tool.java` has just 10 lines I had to write manually.

## Discussion

Please study [How To Read C Declarations](http://blog.parr.us/2014/12/29/how-to-read-c-declarations/) carefully. The rules for understanding C declarations are actually quite simple.

To implement this translator, we'll use ANTLR to parse a subset of C declaration syntax and then build a parse tree visitor that computes an appropriate English subphrase for each parse subtree. I have provided the grammar for you in [CDecl.g4](https://github.com/USF-CS652-starterkits/parrt-cdecl/blob/master/grammars/cs652/cdecl/CDecl.g4). The critical rule is:

```
declarator
    :	declarator '[' ']'		# Array		// right operators have highest precedence
    |	declarator '(' ')'		# Func
    |	'*' declarator			# Pointer
    |	'(' declarator ')'		# Grouping
    |	ID						# Var
    ;
```

The parse trees resulting from this rule identify the various constructs and the order in which they should be applied. For example `*a[]` indicates we should apply the square bracket array operator first and then the pointer operator. We override that precedence with grouping parentheses if we want: `(*a)[]`.

I suggest using the ANTLR 4 plug-in for Intellij to get familiar with the grammar. Tell it to test starting with rule `declaration`.  The following parse tree corresponds to `*a[]`:

<img src=images/array-ptrs.png width=250>

and this one corresponds to `(*a)[]`:

<img src=images/ptr-array.png width=250>

Notice how, in the first tree, the `[]` binds tighter to the `a`. In other words, it is closest to it in the tree with respect to tree node depth. The deepest operations are done first, followed by the second deepest, up the tree to the root. That is why the second parse tree means "pointer to an array", because `*` is the deepest operator.

To implement your translator, you will create a visitor with methods for each of the `declarator` rule alternatives as well as a few others. The tree structure affects the order in which these methods are called. In other words, the parser will drive your translator according to what it finds in the input. 

Your individual visitor methods must augment and return the text computed for its child(ren). For example, in the visitor method that handles `*`, you just have to visit the `declarator` child and add string `pointer to` to its return value. See the ANTLR 4 reference for more on how the visitors work.

As an exercise to learn more about ANTLR's handling of operator precedence, try moving the array and function alternatives last in rule `declarator`. The same input will give you a different tree and results in an incorrect translation to English. For example, `int *a[]` would get the following incorrect parse tree.

<img src=images/array-ptrs-bad.png width=250>

In this case, it shows doing the pointer operator first rather than the array operator.

## Getting started

I have provided a [starter kit](https://github.com/USF-CS652-starterkits/parrt-cdecl) that you can pull into your repository. From the command line, clone your project repo and then pull in my starter kit:

```bash
$ git clone git@github.com:USF-CS652-S16/USERID-cdecl.git
$ cd USERID-cdecl/ # jump into dir containing YOUR empty repo
$ git pull git@github.com:USF-CS652-starterkits/parrt-cdecl.git master
```

You can get any updates to the starter kit by simply `git pull`ing again from `USF-CS652-starterkits/parrt-cdecl.git`.

## Building and testing

The build assumes Java 8.

I suggest that you use Intellij, which knows how to deal with maven (`mvn`) builds. You can run the unit tests with a simple click. It should work on UNIX (including Mac) and Windows.

From the command line, you can build and store a jar with all of your software as follows on UNIX:

```bash
$ mvn install
```

That stores the jar in the following location:

```
~/.m2/repository/edu/usfca/cs652/cdecl/1.0/cdecl-1.0.jar
```

The main program is executed as follows from the Mac commandline for my `parrt` user:

```bash
$ java -cp /Users/parrt/.m2/repository/edu/usfca/cs652/cdecl/1.0/cdecl-1.0.jar:$CLASSPATH \
  cs652.cdecl.Tool "int i;"
i is a int
$ java -cp /Users/parrt/.m2/repository/edu/usfca/cs652/cdecl/1.0/cdecl-1.0.jar:$CLASSPATH \
  cs652.cdecl.Tool "int *a[];"
a is a array of pointer to int
```

Or, using maven as follows:

```bash
$ mvn test
...
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running cs652.cdecl.TestCDecl
Tests run: 15, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.154 sec

Results :

Tests run: 15, Failures: 0, Errors: 0, Skipped: 0

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 2.914 s
[INFO] Finished at: 2016-01-05T09:12:26-08:00
[INFO] Final Memory: 23M/310M
[INFO] ------------------------------------------------------------------------
```

Every time you commit to your repository, your software will automatically be downloaded and tested on the Travis continuous integration server using maven. Here is what a successful result looks like:

<img src=images/travis-cdecl.png width=500>

Check out [https://travis-ci.com/USF-CS652-S16/USERID-cdecl](https://travis-ci.com/USF-CS652-S16/USERID-cdecl) where USERID is your github user id. Mine is parrt, for example. You will not be able to see the repositories of other students.

## Deliverables

You must fill in `Tool.java` and `EnglishGenerator.java` to get a working solution that passes all [15 unit tests](https://github.com/USF-CS652-starterkits/parrt-cdecl/blob/master/test/cs652/cdecl/TestCDecl.java).

## Submission

You must submit your project via github using your account and the repository I've created for you in organization [USF-CS652-S16](https://github.com/USF-CS652-S16).

You are required to show git commits to your repository that are consistent with developing the software for this project.
