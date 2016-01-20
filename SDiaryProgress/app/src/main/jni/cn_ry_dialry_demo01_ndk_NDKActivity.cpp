#include "cn_ry_dialry_demo01_ndk_NDKActivity.h"
#include "jni.h"
/*
 * Class:     cn_ry_dialry_demo01_ndk_NDKActivity
 * Method:    getStringFromC
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_cn_ry_dialry_demo01_ndk_NDKActivity_getStringFromC
  (JNIEnv * env, jobject obj) {
    return env->NewStringUTF("Hello from JNI !  Compiled with ABI ");
}
