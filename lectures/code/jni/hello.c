#include <jni.h>
#include <stdio.h>
#include "HelloJNI.h"
 
// Implementation of native method 'void sayHello()' from HelloJNI
JNIEXPORT void JNICALL Java_HelloJNI_sayHello(JNIEnv *env, jobject thisObj) {
   printf("Hello World!\n");
   return;
}
