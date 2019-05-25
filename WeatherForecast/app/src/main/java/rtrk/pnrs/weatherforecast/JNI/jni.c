#include "jni.h"

JNIEXPORT jdouble JNICALL Java_rtrk_pnrs_weatherforecast_MyLittleHelpers_Conversion_conversion
  (JNIEnv *env, jobject obj, jdouble temperature, jint x) {

    if (x) {
        // x == 1
        // celsius to fahrenheit
        return temperature * 1.8 + 32;
    } else {
        // x == 0
        // fahrenheit to celsius
        return (temperature - 32) / 1.8;
    }

  }

