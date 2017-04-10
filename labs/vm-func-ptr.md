# Adding function pointers to a VM

 The goal of this lab is to add function pointers to our [virtual machine with function metadata](https://github.com/parrt/simple-virtual-machine/tree/func-meta-info).
 
In one of the test files, you will see the following sample program:
 
```java
    static int[] f = {
    //                              ADDRESS
    //.def main() { print f(10); }
        ICONST, 10,                 // 0
        FUNCIDX, 0,                 // 2
        CALLIDX,                    // 4
        PRINT,                      // 5
        HALT,                       // 6
    //.def f(x): ARGS=1, LOCALS=0
    // return 2*x
        LOAD, 0,                    // 7
        ICONST, 2,
        IMUL,
        RET
    };
    static FuncMetaData[] f_metadata = {
        //.def f(x): ARGS=1, LOCALS=0   ADDRESS
        new FuncMetaData("f", 1, 0, 7)
    };
```

As you can see, there are two new instructions:
 
* `FUNCIDX` *func-index*. This pushes the metadata index of a specific function onto the operand stack.
* `CALLIDX`. This instruction pops the metadata index from the operand stack, rather than pulling it from the code memory as `CALL` does.

The `CALLIDX` instruction expects the metadata index on the top of the stack, above the arguments to the function. That is why you see the ICONST push before the FUNCIDX instruction in the code above.

Here is a more complicated example derived from the following high-level code.

```
main:
	print f(&g,10)
	halt

f(p, x):
	return (*p)(x) # indirect call through argument

g(x):
	return 2*x
```

```java
static int[] g = {
//                              ADDRESS
//.def main() { print f(&g,10); }
    FUNCIDX, 1,                 // 0    (push index of g)
    ICONST, 10,                 // 2
    CALL, 0,                    // 4
    PRINT,                      // 6
    HALT,                       // 7
//.def f(p,x): ARGS=1, LOCALS=0
// return (*p)(x)
    LOAD, 1,                    // 8    (push arg x)
    LOAD, 0,                    // 10   (push func ptr/index p)
    CALLIDX,                    // 12
    RET,                        // 13
//.def g(x): ARGS=1, LOCALS=0
// return 2*x
    LOAD, 0,                    // 14
    ICONST, 2,
    IMUL,
    RET
};
static FuncMetaData[] g_metadata = {
    //.def f(p,x): ARGS=2, LOCALS=0 ADDRESS
    new FuncMetaData("f", 2, 0, 8),
    //.def g(x): ARGS=1, LOCALS=0   ADDRESS
    new FuncMetaData("g", 1, 0, 14)
};
```

The trace I get is the following:

```
0000:	funcidx    1                 stack=[ 1 ]
0002:	iconst     10                stack=[ 1 10 ]
0004:	call       f                 stack=[ ]
0008:	load       1                 stack=[ 10 ]
0010:	load       0                 stack=[ 10 1 ]
0012:	callidx                      stack=[ ]
0014:	load       0                 stack=[ 10 ]
0016:	iconst     2                 stack=[ 10 2 ]
0018:	imul                         stack=[ 20 ]
0019:	ret                          stack=[ 20 ]
0013:	ret                          stack=[ 20 ]
0006:	print                        stack=[ ]
20
0007:	halt                         stack=[ ]
```

## Tasks

The first step is to add the bytecode instructions to `Bytecode.java`.

Next, add two more cases to the "decode" `switch` to handle the instructions. You will note that the functionality to perform `CALL` vs `CALLIDX` are identical except for where the instructions obtain the function metadata index. You should probably factor that code out into a function and call it from both cases.