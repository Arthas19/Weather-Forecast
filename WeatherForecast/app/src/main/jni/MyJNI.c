//
// Created by logic on 25-May-19.
//
#include "MyJNI.h"

JNIEXPORT jdouble JNICALL Java_rtrk_pnrs_weatherforecast_MyLittleHelpers_Conversion_conversion
  (JNIEnv *env, jobject obj, jdouble temp, jint x) {
    if (x) {
        // returns in fahrenheit
        return temp * 1.8 + 32;
    } else {
        // returns in celsius
        return (temp - 32) / 1.8;
    }
  }