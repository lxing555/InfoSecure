package com.example.infosecure.entity;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Photos {
    private String path;
    private Bitmap bitmap;
    private String base64;
    /**
     * 将bitmap转换成byte,该过程时间较长,建议子线程运行,但是这里我为了setText，就放主线程了
     * @param bitmap
     * @return
     */
    public static String imageToBase64(Bitmap bitmap) {
        //以防解析错误之后bitmap为null
        if (bitmap == null)
            return "解析异常";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //此步骤为将bitmap进行压缩，选择了格式jepg，第二个参数为压缩质量，第三个参数传入outputstream去写入压缩后的数据
        bitmap.compress(Bitmap.CompressFormat.JPEG,80, outputStream);
        try {
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //将获取到的outputstream转换成byte数组
        byte[] bytes = outputStream.toByteArray();
        Log.d("图片压缩测试80%压缩",""+bytes.length);
        //android.util包下有Base64工具类，直接调用，格式选择Base64.DEFAULT即可
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        //打印数据，下面计算用
        Log.i("MyLog", "imageToBase64: " + str.length());
        return str;
    }
    public static Bitmap base64ToImage(String text) {
        //同样的，用base64.decode解析编码，格式跟上面一致
        byte[] bytes = Base64.decode(text, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }




}
