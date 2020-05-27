#include <jni.h>
#include <string>
#include "JavaClassesDef.h"
#include "EncodeUtils.h"
#include "aes.h"
#include <sys/ptrace.h>
#include <cstdint>

static const char *className = "com/example/infosecure/util/SecureUtil";
static jbyteArray encrypt(JNIEnv *env,jobject, jbyteArray data,jstring aesKey,jint type) {
    const char* s= (env)->GetStringUTFChars(aesKey,0);             //类型转换很重要
    uint8_t *key=(uint8_t*)s;
    return EncodeUtils::encryptData(env,data,key,type);
}

static jbyteArray decrypt(JNIEnv *env,jobject, jbyteArray data,jstring aesKey,jint type) {
    const char* s= (env)->GetStringUTFChars(aesKey,0);
    uint8_t *key=(uint8_t*)s;
    return EncodeUtils::decryptData(env,data,key,type);
}


static JNINativeMethod nMethods[] = {
        { SecureUtil_encryptData_Method, SecureUtil_encryptData_Param, (void*)encrypt},
        { SecureUtil_decryptData_Method, SecureUtil_decryptData_Param, (void*)decrypt}
};

static int jniRegisterNativeMethods(JNIEnv* env, const char* className,
                                    const JNINativeMethod* gMethods, int numMethods)
{
    jclass clazz;

    clazz = (env)->FindClass( className);
    if (clazz == NULL) {
        return -1;
    }

    int result = 0;
    if ((env)->RegisterNatives(clazz, nMethods, numMethods) < 0) {
        result = -1;
    }

    (env)->DeleteLocalRef(clazz);
    return result;
}

jint JNI_OnLoad(JavaVM* vm, void* reserved){
    //LOGI("JNI", "enter jni_onload");

    JNIEnv* env = NULL;
    jint result = -1;

    if (vm->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK) {
        return result;
    }

    jniRegisterNativeMethods(env, className, nMethods, sizeof(nMethods) / sizeof(JNINativeMethod));

    return JNI_VERSION_1_4;
}
