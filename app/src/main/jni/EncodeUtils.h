#include <jni.h>
#include <cstdint>

#ifndef WINANDROID_ENCODEUTILS_H
#define WINANDROID_ENCODEUTILS_H
#define keyfaiz "0123456789ABCDEF"
#define KEY_LEN 16
class EncodeUtils {

public:
    static jbyteArray encryptData(JNIEnv *env, jbyteArray& data,uint8_t* key,jint type);
    static jbyteArray decryptData(JNIEnv *env, jbyteArray& data,uint8_t* key,jint type);
    //static jstring geneSign(JNIEnv *env, jstring& data);
};


#endif //WINANDROID_ENCODEUTILS_H
