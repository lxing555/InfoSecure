package com.example.infosecure.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.infosecure.AesUtil;
import com.example.infosecure.R;
import com.example.infosecure.entity.Photos;
import com.example.infosecure.entity.Infos;
import com.example.infosecure.entity.SecretLog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddSecretLogActivity extends AppCompatActivity {
    private GridView gridViewImage;
    private final int IMAGE_OPEN = 1;
    private String pathImage;                //选择图片路径
    private List<Map<String,Object>> dataList;
    private SimpleAdapter simpleAdapter;
    private Bitmap bitmapAdd;
    private EditText eTextTitle,eTextContent;
    private LinearLayout linearLayout;
    private SecretLog secretLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_secret_log);
        initView();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_ADJUST_PAN);
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }
        //锁定屏幕
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        dataList=new ArrayList<Map<String,Object>>();
        bitmapAdd= BitmapFactory.decodeResource(getResources(),R.drawable.add_image);
        HashMap<String,Object>map=new HashMap<String, Object>();
        map.put("itemImage",bitmapAdd);
        dataList.add(map);
        simpleAdapter=new SimpleAdapter(this,dataList,R.layout.item_grid_image,new String[]{"itemImage"},new int[]{R.id.img_item});
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder(){
            @Override
            public boolean setViewValue(View view,Object data,String textRepresentation){
                if(view instanceof ImageView && data instanceof Bitmap){
                    ImageView i = (ImageView)view;
                    i.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }
        });
        gridViewImage.setAdapter(simpleAdapter);
        gridViewImage.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                if(dataList.size() == 10) { //第一张为默认图片
                    Toast.makeText(AddSecretLogActivity.this, "图片数9张已满", Toast.LENGTH_SHORT).show();
                }
                else if(position == 0) { //点击图片位置为+ 0对应0张图片
                    Toast.makeText(AddSecretLogActivity.this, "添加图片", Toast.LENGTH_SHORT).show();
                    //选择图片
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, IMAGE_OPEN);
                    //通过onResume()刷新数据
                }
                else {
                    dialog(position);
                    //Toast.makeText(MainActivity.this, "点击第"+(position + 1)+" 号图片",
                    //Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void initView(){
        gridViewImage=(GridView)findViewById(R.id.gridView_add_image);
        eTextTitle=(EditText)findViewById(R.id.et_add_title);
        eTextContent=(EditText)findViewById(R.id.et_add_content);
        linearLayout=(LinearLayout)findViewById(R.id.ll_save);
        secretLog=new SecretLog();
        linearLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                secretLog.setContent(eTextContent.getText().toString());
                secretLog.setTitle(eTextTitle.getText().toString());

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyyMMddHHmmss");
                //获取当前时间
                Date date = new Date(System.currentTimeMillis());
                Date dateId=new Date(System.currentTimeMillis());
                secretLog.setLogId(simpleDateFormat2.format(dateId));
                //Log.d("logId",simpleDateFormat2.format(dateId));
                secretLog.setTime(simpleDateFormat.format(date));
                Infos.secretLogList.add(secretLog);
                Infos.insert_secretLog(secretLog);
                finish();
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //打开图片
        if(resultCode==RESULT_OK && requestCode==IMAGE_OPEN) {
            Uri uri = data.getData();
            if (!TextUtils.isEmpty(uri.getAuthority())) {
                //查询选择图片
                Cursor cursor = getContentResolver().query(
                        uri,
                        new String[] { MediaStore.Images.Media.DATA },
                        null,
                        null,
                        null);
                //返回 没找到选择图片
                if (null == cursor) {
                    return;
                }
                //光标移动至开头 获取图片路径
                cursor.moveToFirst();
                pathImage = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));
            }
        }  //end if 打开图片
    }


    //刷新图片
    @Override
    protected void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(pathImage)){
            Bitmap addbmp=BitmapFactory.decodeFile(pathImage);
            {
                String base= Photos.imageToBase64(addbmp);
                Log.d("Base64",base);
                String enbase= AesUtil.getEnString(base, Infos.key);
                Log.d("加密后Base64",enbase);
                secretLog.images_base64.add(base);
                secretLog.images_encrypt.add(enbase);
            }
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("itemImage", addbmp);
            dataList.add(map);
            simpleAdapter = new SimpleAdapter(this,
                    dataList, R.layout.item_grid_image,
                    new String[] {"itemImage"}, new int[] { R.id.img_item});
            simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data,
                                            String textRepresentation) {
                    // TODO Auto-generated method stub
                    if(view instanceof ImageView && data instanceof Bitmap){
                        ImageView i = (ImageView)view;
                        i.setImageBitmap((Bitmap) data);
                        return true;
                    }
                    return false;
                }
            });
            gridViewImage.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
            //刷新后释放防止手机休眠后自动添加
            pathImage = null;
        }
    }
    /*
     * Dialog对话框提示用户删除操作
     * position为删除图片位置
     */
    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddSecretLogActivity.this);
        builder.setMessage("确认移除已添加图片吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dataList.remove(position);
                simpleAdapter.notifyDataSetChanged();
                secretLog.images_base64.remove(position);
                secretLog.images_encrypt.remove(position);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
