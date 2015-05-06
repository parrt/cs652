#include <jni.h>
#include <stdio.h>
#include "CallStatic.h"
 
// instance method version:
//JNIEXPORT void JNICALL Java_HelloJNI_sayHello(JNIEnv *env, jobject thisObj) { ... }

// static method
JNIEXPORT void JNICALL Java_CallStatic_sayHello(JNIEnv *env, jclass thisClass) {
   printf("Hello World!\n");
   return;
}
