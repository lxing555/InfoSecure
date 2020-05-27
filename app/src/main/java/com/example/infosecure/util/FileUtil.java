package com.example.infosecure.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import static com.example.infosecure.entity.Infos.AES_KEY;

public class FileUtil {
    private static final String faiz="0123456789ABCDEF";
    public static void cipherencrypt(String filePath,String outPath) throws IOException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException {
        // Here you read the cleartext.
        FileInputStream fis = new FileInputStream(filePath);
        // This stream write the encrypted text. This stream will be wrapped by
        // another stream.
        FileOutputStream fos = new FileOutputStream(outPath);
        // Length is 16 byte
        SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(),
                "AES");
        // Create cipher
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, sks);
        // Wrap the output stream
        CipherOutputStream cos = new CipherOutputStream(fos, cipher);
        // Write bytes
        int b;
        byte[] d = new byte[8];
        while ((b = fis.read(d)) != -1) {
            cos.write(d, 0, b);
        }
        // Flush and close streams.
        cos.flush();
        cos.close();
        fis.close();
    }

    public static void cipherdecrypt(String inPath,String outPath) throws IOException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException {
        FileInputStream fis = new FileInputStream(inPath);
        FileOutputStream fos = new FileOutputStream(outPath);
        SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(),
                "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, sks);
        CipherInputStream cis = new CipherInputStream(fis, cipher);
        int b;
        byte[] d = new byte[8];
        while ((b = cis.read(d)) != -1) {
            fos.write(d, 0, b);
        }
        fos.flush();
        fos.close();
        cis.close();
    }
    /**
     * 根据byte数组，生成文件
     * @param bfile 文件数组
     * @param filePath 文件存放路径
     * @param fileName 文件名称
     */
    public static void byte2File(byte[] bfile,String filePath,String fileName){
        BufferedOutputStream bos=null;
        FileOutputStream fos=null;
        File file=null;
        try{
            File dir=new File(filePath);
            if(!dir.exists() && !dir.isDirectory()){//判断文件目录是否存在
                dir.mkdirs();
            }
            file=new File(filePath+fileName);
            fos=new FileOutputStream(file);
            bos=new BufferedOutputStream(fos);
            bos.write(bfile);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        finally{
            try{
                if(bos != null){
                    bos.close();
                }
                if(fos != null){
                    fos.close();
                }
            }
            catch(Exception e){
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }
    /**
     * 获得指定文件的byte数组
     * @param filePath 文件绝对路径
     * @return
     */
    public static byte[] file2Byte(String filePath){
        ByteArrayOutputStream bos=null;
        BufferedInputStream in=null;
        try{
            File file=new File(filePath);
            if(!file.exists()){
                throw new FileNotFoundException("file not exists");
            }
            bos=new ByteArrayOutputStream((int)file.length());
            in=new BufferedInputStream(new FileInputStream(file));
            int buf_size=1024;
            byte[] buffer=new byte[buf_size];
            int len=0;
            while(-1 != (len=in.read(buffer,0,buf_size))){
                bos.write(buffer,0,len);
            }
            return bos.toByteArray();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
        finally{
            try{
                if(in!=null){
                    in.close();
                }
                if(bos!=null){
                    bos.close();
                }
            }
            catch(Exception e){
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }
    /**
     * @param raw
     * @param clear 待加密数据
     * @return      加密后的字节数组
     * @throws Exception
     */
    private static byte[] javaencrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    /**
     * @param raw
     * @param encrypted   加密过的数据
     * @return            解密后的数据
     * @throws Exception
     */
    private static byte[] javadecrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    /** Java加密文件
     * @param inPath 待加密文件路径+文件名
     * @param outPath 加密后文件输出路径
     * @param fileName 加密后文件名
     * @throws Exception
     */
    public static void fileEncryptJava(String inPath,String outPath,String fileName) throws Exception {
        byte[] clear=file2Byte(inPath);
        byte[] encrypt= javaencrypt(faiz.getBytes(),clear);
        byte2File(encrypt,outPath,fileName);
    }

    /** Java解密文件
     * @param inPath 待解密文件路径+文件名
     * @param outPath 解密文件输出路径
     * @param fileName 解密后文件名
     * @throws Exception
     */
    public static void fileDecryptJava(String inPath,String outPath,String fileName) throws Exception {
        byte[] encrypt=file2Byte(inPath);
        byte[] clear= javadecrypt(faiz.getBytes(),encrypt);
        byte2File(clear,outPath,fileName);
    }

    /** NDK加密文件
     * @param inPath 待加密文件路径+文件名
     * @param outPath 加密后文件输出路径
     * @param fileName 加密后文件名
     * @param method   加密方式
     */
    public static void fileEncryptNDK(String inPath, String outPath, String fileName,int method){
        byte[] clear=file2Byte(inPath);
        byte[] encrypt=SecureUtil.encryptData(clear,AES_KEY,method);
        byte2File(encrypt,outPath,fileName);
    }

    /** NDK解密文件
     * @param inPath 待解密文件路径+文件名
     * @param outPath 解密文件输出路径
     * @param fileName 解密后文件名
     */
    public static void fileDecryptNDK(String inPath, String outPath, String fileName,int method){
        byte[] encrypt=file2Byte(inPath);
        byte[] clear=SecureUtil.decryptData(encrypt,AES_KEY,method);
        byte2File(clear,outPath,fileName);
    }

    //字符串转换成数组
    public static byte[] toByteArray(String hexString) {
        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() / 2];
        int k = 0;
        for (int i = 0; i < byteArray.length; i++) {// 因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先
            byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
            byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
            byteArray[i] = (byte) (high << 4 | low);
            k += 2;
        }
        return byteArray;
    }

}
