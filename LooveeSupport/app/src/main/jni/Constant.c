#include <stdio.h>
#include "com_loovee_common_jni_ConstantJNI.h"

JNIEXPORT jstring JNICALL Java_com_loovee_common_jni_ConstantJNI_getEncryptKeyFromJNI
  (JNIEnv *env, jobject obj){

	return (*env)->NewStringUTF(env, "l*c%@)c5");
}
