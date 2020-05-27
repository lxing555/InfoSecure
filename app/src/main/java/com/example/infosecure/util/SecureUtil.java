package com.example.infosecure.util;

public class SecureUtil {

    static {
        System.loadLibrary("native-lib");
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    native public static byte[] encryptData(byte[] data,String AES_KEY,int type);
    native public static byte[] decryptData(byte[] data,String AES_KEY,int type);

}


//    public static byte[] encryptData(byte[] data) {
//        return encryptData(data);
//    }
//
//    public static byte[] decryptData(byte[] data) {
//        return decryptData(data);
//    }