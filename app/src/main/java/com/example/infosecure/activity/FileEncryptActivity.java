package com.example.infosecure.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.infosecure.util.FileUtil;
import com.example.infosecure.R;

import java.io.File;

public class FileEncryptActivity extends AppCompatActivity {
    private TextView textVideo,textDocument,textPhoto,textAudio;
    private RelativeLayout rlEncryptImage,rlEncryptDocument,rlEncryptAudio,rlEncryptVideo;
    private String inPath_Name;
    private String file_true_Path;
    private String fileName;
    public static int FILE_MANAGE_MODE=0;    //state为0时是加密模式，为1时是解密模式
    public static int FILE_ENCRYPT_METHOD=1;   //0为ECB加密，1为ECB解密
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_maneger);
        //getSupportActionBar().hide();
        //verifyStoragePermissions(this);
        initView();
    }
    public static void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void initView(){
        rlEncryptImage=(RelativeLayout)findViewById(R.id.rl_encrypt_image);
        rlEncryptDocument=(RelativeLayout)findViewById(R.id.rl_encrypt_document);
        rlEncryptAudio=(RelativeLayout)findViewById(R.id.rl_encrypt_audio);
        rlEncryptVideo=(RelativeLayout)findViewById(R.id.rl_encrypt_video);
        textPhoto=(TextView)findViewById(R.id.text_encrypt_image);
        textVideo=(TextView)findViewById(R.id.text_encrypt_video);
        textAudio=(TextView)findViewById(R.id.text_encrypt_audio);
        textDocument=(TextView)findViewById(R.id.text_encrypt_document);
        rlEncryptVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        });

        rlEncryptDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        });

        rlEncryptImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);

            }
        });

        rlEncryptAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if ("file".equalsIgnoreCase(uri.getScheme())) {//使用第三方应用打开
                inPath_Name = uri.getPath();
                Toast.makeText(this, inPath_Name + "11111", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                inPath_Name = getPath(this, uri);
                getFileName(inPath_Name);
                Toast.makeText(this, inPath_Name, Toast.LENGTH_SHORT).show();
                if(FILE_MANAGE_MODE==0){
                    try {
                        FileUtil.fileEncryptNDK(inPath_Name,file_true_Path,FILE_ENCRYPT_METHOD+"encrypt"+fileName,FILE_ENCRYPT_METHOD);
                        deleteSingleFile(inPath_Name);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else{
                    try {
                        int method=fileName.charAt(0)-48;
                        if(method==FILE_ENCRYPT_METHOD){
                            FileUtil.fileDecryptNDK(inPath_Name,file_true_Path,fileName.substring(8,fileName.length()),FILE_ENCRYPT_METHOD);
                            deleteSingleFile(inPath_Name);
                        }else{
                            Toast.makeText(FileEncryptActivity.this,"当前解密方式与该文件加密方式不符，请切换加解密方法",Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else {//4.4以下下系统调用方法
                inPath_Name = getRealPathFromURI(uri);
                Toast.makeText(FileEncryptActivity.this, inPath_Name + "222222", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        MenuInflater menuInflater=new MenuInflater(this);
        menuInflater.inflate(R.menu.menu_file,menu);
        return super.onCreatePanelMenu(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_file_settings:{
                Intent intent=new Intent(FileEncryptActivity.this,FileSettingActivity.class);
                startActivity(intent);
            }break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(FILE_MANAGE_MODE==1){
            textVideo.setText("视频解密");
            textPhoto.setText("照片解密");
            textDocument.setText("文档解密");
            textAudio.setText("音频解密");
        }else{
            textVideo.setText("视频加密");
            textPhoto.setText("照片加密");
            textDocument.setText("文档加密");
            textAudio.setText("音频加密");
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(null!=cursor&&cursor.moveToFirst()){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }
    /** 删除单个文件
     * @param filePath$Name 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    private boolean deleteSingleFile(String filePath$Name) {
        File file = new File(filePath$Name);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                Log.e("--Method--", "Copy_Delete.deleteSingleFile: 删除单个文件" + filePath$Name + "成功！");
                return true;
            } else {
                Toast.makeText(this, "删除单个文件" + filePath$Name + "失败！", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(this, "删除单个文件失败：" + filePath$Name + "不存在！", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void getFileName(String pathandname){
        int start=pathandname.lastIndexOf("/");
        int end=pathandname.length();
        if (start!=-1 && end!=-1) {
            file_true_Path=pathandname.substring(0,start+1);
            fileName=pathandname.substring(start+1, end);
        }
    }
    /**
          * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
          */
    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
    /**
          * Get the value of the data column for this Uri. This is useful for
          * MediaStore Uris, and other file-based ContentProviders.
          *
          * @param context       The context.
          * @param uri           The Uri to query.
          * @param selection     (Optional) Filter used in the query.
          * @param selectionArgs (Optional) Selection arguments used in the query.
          * @return The value of the _data column, which is typically a file path.
          */
    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
          * @param uri The Uri to check.
          * @return Whether the Uri authority is ExternalStorageProvider.
          */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    /**
          * @param uri The Uri to check.
          * @return Whether the Uri authority is DownloadsProvider.
          */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    /**
          * @param uri The Uri to check.
          * @return Whether the Uri authority is MediaProvider.
          */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
