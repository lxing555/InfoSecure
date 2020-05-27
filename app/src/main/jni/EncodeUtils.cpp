#include <stdint.h>
#include <string>
#include "EncodeUtils.h"

#ifdef __cplusplus
extern "C" {
#endif
#include "aes.h"
void AES128_ECB_encrypt(uint8_t* input, const uint8_t* key, uint8_t *output);
void AES128_ECB_decrypt(uint8_t* input, const uint8_t* key, uint8_t *output);
#ifdef __cplusplus
}
#endif

using namespace std;


static void initIv(uint8_t* pIv){
    uint8_t iv[]  = { 49, 50, 51, 52, 49, 50, 51, 52, 49, 50, 51, 52, 49, 50, 51, 52 }; // {1,2,3,4,1,2,3,4,1,2,3,4,1,2,3,4}
    for (int i = 0; i < KEY_LEN; i++){
        pIv[i] = iv[i];
    }
}

jbyteArray EncodeUtils::encryptData(JNIEnv *env,jbyteArray& data,uint8_t* key,jint type){   //type为0即用ECB加密，为1即用CBC加密
    uint8_t iv[KEY_LEN];
    initIv(iv);
    uint8_t *key1=(uint8_t *)keyfaiz;
    int lenOri = env->GetArrayLength(data); //源数据长度
    jbyte* jData = env->GetByteArrayElements(data, 0);

    uint8_t padding = KEY_LEN - lenOri % KEY_LEN;
    int lenOffset = lenOri + padding;  //用来加密的数据长度必须是16的倍数,不够时补齐
    int cont=lenOffset/16;

    uint8_t *srcData=(uint8_t *)malloc(sizeof(uint8_t)*lenOffset);
    memcpy(srcData, jData, lenOri);

    for (int i = 0; i < padding; i++) { // PKCS5Padding/PKCS7Padding 填充
        srcData[lenOri + i] = padding;
    }

    uint8_t *result=(uint8_t *)malloc(sizeof(uint8_t)*lenOffset);

    if(type==0){
        for(int i=0;i<cont;i++){
            AES128_ECB_encrypt(srcData+i*16,key,result+i*16);
        }
    }else{
        AES128_CBC_encrypt_buffer(result, srcData,lenOri, key, iv);
    }

    jbyte *by = (jbyte*)result;
    jbyteArray jarray = env->NewByteArray(lenOffset);
    env->SetByteArrayRegion(jarray, 0, lenOffset, by);

    return jarray;
}

jbyteArray EncodeUtils::decryptData(JNIEnv *env, jbyteArray& data,uint8_t* key,jint type) {     //type为0即用ECB解密，为1即用CBC解密

    uint8_t iv[KEY_LEN];
    initIv(iv);

    int lenOri = env->GetArrayLength(data); //源数据长度
    int cont=lenOri/16;

    jbyte* jData = env->GetByteArrayElements(data, 0);

    uint8_t *srcData=(uint8_t *)malloc(sizeof(uint8_t)*lenOri);
    memcpy(srcData, jData, lenOri);

    uint8_t *result=(uint8_t *)malloc(sizeof(uint8_t)*lenOri);
    if(type==0){
        for(int i=0;i<cont;i++){
            AES128_ECB_decrypt(srcData+i*16,key,result+i*16);
        }
    }else{
        AES128_CBC_decrypt_buffer(result, srcData,lenOri, key, iv);
    }

    int padding = result[lenOri-1];
    int resultSize = lenOri - padding;

    jbyte *by = (jbyte*)result;
    jbyteArray jarray = env->NewByteArray(resultSize);
    env->SetByteArrayRegion(jarray, 0, resultSize, by);

    return jarray;
}
