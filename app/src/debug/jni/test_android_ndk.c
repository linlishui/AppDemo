//
// Created by lishui.lin on 21-2-23.
//

#include <jni.h>
#include <stdio.h>

void callJavaMethod(JNIEnv *,jobject);

jstring Java_lishui_demo_app_jni_NdkTest_getNdkStr
(JNIEnv *env, jobject thiz)
{
    printf("invoke getNdkStr in C\n");
    callJavaMethod(env,thiz);
    return (*env)->NewStringUTF(env, "Hello NDK with C");
}

void Java_lishui_demo_app_jni_NdkTest_setNdkStr
(JNIEnv *env, jobject thiz, jstring string)
{
    printf("invoke setNdkStr from C\n");
    char* str = (char*)(*env)->GetStringUTFChars(env, string, NULL);
    printf("%s\n", str);
    (*env)->ReleaseStringUTFChars(env, string, str);
}

void callJavaMethod(JNIEnv *env,jobject thiz)
{
    jclass clazz = (*env)->FindClass(env, "lishui/demo/app/jni/NdkTest");
    if(clazz == NULL){
        printf("find class JniTest error!");
        return;
    }
    jmethodID id = (*env)->GetStaticMethodID(env, clazz,"invokeByNDK","(Ljava/lang/String;)V");
    if(id == NULL){
       printf("find method invokeByJNI error!");
       return;
    }
    jstring msg = (*env)->NewStringUTF(env, "msg send by callJavaMethod in test_android_jni.c");
    (*env)->CallStaticVoidMethod(env, clazz, id, msg);
}