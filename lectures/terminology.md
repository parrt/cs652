Computer language terminology

Define terms compiler, interp/VM, translation, LLVM, bytecode, GC, closures, language, grammar, Syntax, semantics, scope, concurrency, pre/postfix, recursion, object, idioms, libraries, tools, closures, continuations etc...

[see written notes]

Imagine you had not seen language before and you didn't know what this means

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
cc -O -S t.c
```

on ARM chip and it makes it clear what's going on, some you know its assembly code.

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