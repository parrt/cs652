# Java Native Interface

Java provides a standard programming interface for interacting with functions from external native libraries. Although the naming conventions and internals differ for dynamic libraries across platforms, each conforming Java virtual machine (JVM) implementation supports JNI: [Java native interface](http://en.wikipedia.org/wiki/Java_Native_Interface).

The basic idea is that the Java code specifies the Java interface part without a body by using the `native` keyword. Then, the `javah` tool is used to create an include file with all of the appropriate definitions for some C code to define the appropriate native method.

## JNI support files location

See the include directory:

```
<install-directory>/include/
<install-directory>/include/<platform-directory>/
```

Principal files:

```
<install-directory>/include/jni.h
<install-directory>/include/<platform-directory>/jni_md.h
```

This header files account for the platform dependencies with C and C++. Each Java distribution provides an include directory with the header files that map Java data types to JNI data types to C data types. This mapping is handled in `jni.h`:

```c
...
typedef unsigned char   jboolean;
typedef unsigned short  jchar;
typedef short           jshort;
typedef float           jfloat;
typedef double          jdouble;
...
```

Platform dependent data types are handled in a separate header file, `jni_md.h`:

```c
#define JNIEXPORT     __attribute__((visibility("default")))
#define JNIIMPORT     __attribute__((visibility("default")))
#define JNICALL

typedef int jint;
#ifdef _LP64 /* 64-bit */
typedef long jlong;
#else
typedef long long jlong;
#endif

typedef signed char jbyte;
```

The `jni.h` header file contains various convenience definitions that are useful in describing method/function signatures, field types, and so on.

```c
typedef union jvalue {
    jboolean z;
    jbyte    b;
    jchar    c;
    jshort   s;
    jint     i;
    jlong    j;
    jfloat   f;
    jdouble  d;
    jobject  l;
} jvalue;
```

## Hello world from Java to C

```java
public class HelloJNI {
	static {
		// Load native library dynamically
		System.loadLibrary("hello"); 
	}

	private native void sayHello();

	public static void main(String[] args) {
		HelloJNI hello = new HelloJNI();
		hello.sayHello();
	}
}
```

According to this [blog entry](https://blogs.oracle.com/moonocean/entry/a_simple_example_of_jni), Linux uses prefix `lib` and suffix `.so`:

```bash
$ gcc -shared -fpic -o libfoo.so -I/usr/java/include -I/usr/java/include/linux foo.c
```

On Windows OS, it is prefixed by nothing and suffixed with '.dll'. It could be compiled with Visual Studio automatically and create a file named as foo.dll. 

```c
#include <jni.h>
#include <stdio.h>
#include "hello.h"

// Implementation of native method 'void sayHello()' from HelloJNI
JNIEXPORT void JNICALL Java_HelloJNI_sayHello(JNIEnv *env, jobject thisObj) {
   printf("Hello World!\n");
   return;
}
```


```bash
$ javac HelloJNI.java
$ javah HelloJNI
$ ls
HelloJNI.class   HelloJNI.java    hello.h
HelloJNI.h       hello.c          libhello.jnilib*
$ cc -dynamiclib -o libhello.jnilib \
  -I/Library/Java/JavaVirtualMachines/jdk1.8.0.jdk/Contents/Home/include \
  -I/Library/Java/JavaVirtualMachines/jdk1.8.0.jdk/Contents/Home/include/darwin \
  hello.c
$ java HelloJNI
Hello World!
```

## Return a C string to Java

```java
public class GetTime {
	static {
		// Load native library dynamically
		System.loadLibrary("gettime"); 
	}

	public native String getTime();

	public static void main(String[] args) {
		System.out.println(new GetTime().getTime());
	}
}
```

```c
#include <jni.h>
#include <stdio.h>
#include <time.h>
#include "GetTime.h"

#define TIME_STR_LEN 200

JNIEXPORT jstring JNICALL Java_GetTime_getTime(JNIEnv *env, jobject obj)
{
    static char timeStr[TIME_STR_LEN];
    static jstring jtime;
    time_t current;
    struct tm *t;

    current = time((time_t *) NULL);
    t = localtime(&current);
    sprintf(timeStr, "The current time is: %02d:%02d:%02d.\n",
    t->tm_hour, t->tm_min, t->tm_sec);
    jtime = (*env)->NewStringUTF(env, timeStr);
    return jtime;
}
```

```bash
cc -dynamiclib -o libgettime.jnilib -I/Library/Java/JavaVirtualMachines/jdk1.8.0.jdk/Contents/Home/include -I/Library/Java/JavaVirtualMachines/jdk1.8.0.jdk/Contents/Home/include/darwin gettime.c
$ java GetTime
The current time is: 11:26:52.
```

