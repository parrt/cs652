#include <jni.h>
#include <stdio.h>
#include <time.h>
#include "GetTime.h"

#define TIME_STR_LEN 200

JNIEXPORT jstring JNICALL Java_GetTime_getTime(JNIEnv *env, jobject obj)
{
    static char timeStr[TIME_STR_LEN]; // make non-local space
    static jstring jtime;              // alloc local struct space
    time_t current;
    struct tm *t;

    current = time((time_t *) NULL);   // call OS
    t = localtime(&current);
    sprintf(timeStr, "The current time is: %02d:%02d:%02d.\n",
            t->tm_hour, t->tm_min, t->tm_sec);
    jtime = (*env)->NewStringUTF(env, timeStr); // C->Java string
    return jtime;                      // return copy of local struct
}
