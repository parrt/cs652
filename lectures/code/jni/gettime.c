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
