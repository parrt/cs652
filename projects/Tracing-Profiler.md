Tracing profiler
====

A tracing profiler injects code the start of stop of every method that tracks how long each method invocation takes. It also counts how many times the method is called. Do not try to record an entry for every method invocation. That would be too slow and cost too much memory. Instead, compute the amount of time and add that to a record for each pkg.class.method. Also count the number of invocations for each method. Then, again with a shutdown hook, you can collate the information and generate a nice little report.

Note that you should use the most accurate timer Java has:

long startTime = System.nanoTime();
...
long estimatedTime = System.nanoTime() - startTime;
In order to do the bytecode transformations, you need to read ASM transformations. Also you should use ASMifier so you don't have to figure out all the library method calls. Still, I found getting this thing to work kind of tricky. ASM's use of visitors to visit and to generate I found to be profoundly unintuitive.

To ensure that your "method exit" code actually executes, you must enclose the method in a try-finally:

try {
   ... original method code ...
}
finally {
    long estimatedTime = System.nanoTime() - startTime;
    ...
}
The Java code could execute a return instruction anywhere within the method. This will force your code to execute.

As part of your tracing profiler, you must also track the number of objects of each type that are created. In this case, it makes sense to instrument java.* classes so that you pick up things like String. That means, for each new X(...) expression in any method, you should add code to increment a count for that particular object. This will be tricky because you must make sure that the code you insert does not mess up the expression surrounding the new. For example, if you see new X().go(), your instrumentation code will either have to go in front of the expression or after the new finishes but before the go method call. You could also do the count after go(), but it would change the order in which you record things since that method call could create other objects.

You will need a map from pkg.class -> count of objects.

You should be careful that you don't slow down the host program too much by injecting really expensive code into the methods. Do as much processing at shutdown, rather than on the fly, as you can.

Your report should be include the package / class name with the invocation count as with the sampling profiler. But, you should also include the total amount of time spent in that method in milliseconds. Sort by the amount of time spent in the method in descending order (do not sort by the invocation count):

pkg.bar 1932ms 29
pkg.foo 988ms 32
another.pkg.main 20ms 8
...
Follow that report with another right afterwards that shows the number of objects created of each type:

org.jdom.Attribute 523416
char[] 356059
...
java.lang.String 23009
...
Java agents
All of our work will be done using agents so that our profilers are as unobtrusive as possible. Agents make it possible to instrument or observe programs during execution without having to modify those programs and without having source code for them. For example, here is how I launched my sampling profiler on class SomeTestProgram:

$ java -javaagent:sampler.jar SomeTestProgram
That sampler.jar has to have a manifest file that indicates what the agent class is. Since we are using premain() method, we need to specify Premain-Class:

$ cat META-INF/MANIFEST.MF 
Premain-Class: tracer.TestAgent
When you jar up your code, make sure that you include the META-INF directory and the manifest file underneath.

Deliverables
Please put your code in package sampler and tracer.

sampler.jar. For consistency, please call your agent class SamplerAgent.
tracer.jar. Please call your agent TracerAgent.
Here is precisely how we will run your software:

java -javaagent:sampler.jar SomeTestProgram
java -javaagent:tracer.jar SomeTestProgram
where SomeTestProgram must have a main method. The agent from the jars, identified by the Premain-Class property in the manifest file, will execute before the SomeTestProgram main method runs. This gives your agent an opportunity to rewrite the bytecodes or do whatever else you need to initialize your system.

Submission
Be careful not to create extra subdirectories that get mapped to subversion. I will pull your files exactly as shown below. Failure to put the files in the right directory means 10% off. We will be using a robot to pull and test your source code. Put everything into the two packages sampler and tracer. Please do not use subdirectories such as src.

https://www.cs.usfca.edu/svn/YOUR_CS_STUDENT_LOGIN/cs345/profiler/sampler.jar
https://www.cs.usfca.edu/svn/YOUR_CS_STUDENT_LOGIN/cs345/profiler/sampler/*.java
https://www.cs.usfca.edu/svn/YOUR_CS_STUDENT_LOGIN/cs345/profiler/tracer.jar
https://www.cs.usfca.edu/svn/YOUR_CS_STUDENT_LOGIN/cs345/profiler/tracer/*.java
You can use the svn account for development of the software too if you would like, but I will only be looking at your sampler.jar, tracer.jar file in the build directory.
