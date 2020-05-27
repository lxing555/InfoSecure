package com.example.infosecure.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.infosecure.R;
import com.example.infosecure.adapter.MyDataBaseHelper;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import static com.example.infosecure.activity.FileEncryptActivity.verifyStoragePermissions;
import static com.example.infosecure.adapter.MyDataBaseHelper.DB_NAME;
import static com.example.infosecure.adapter.MyDataBaseHelper.DB_VERSION;
import static com.example.infosecure.adapter.MyDataBaseHelper.USER_NAME;
import static com.example.infosecure.adapter.MyDataBaseHelper.USER_PASSWORD;


public class LoginActivity extends AppCompatActivity {
    private TextView textInitPw,textForgetPw;
    private EditText eTextPWord;
    private LinearLayout btLogin;
    public static MyDataBaseHelper myDataBaseHelper;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {///密码为555555
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        SQLiteDatabase.loadLibs(this);
        myDataBaseHelper =new MyDataBaseHelper(this,DB_NAME,null,DB_VERSION);
        requestPermission();
        initView();
    }
    private void initView(){
        eTextPWord =(EditText)findViewById(R.id.et_login_pwd);
        btLogin =(LinearLayout)findViewById(R.id.btn_login);
        textInitPw=(TextView)findViewById(R.id.text_initpw);
        textForgetPw=(TextView)findViewById(R.id.text_fogetpw);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db= myDataBaseHelper.getWritableDatabase(MyDataBaseHelper.DB_PASSWORD);
                Cursor cursor=db.query(MyDataBaseHelper.TABLE_USER,new String[]{USER_PASSWORD},USER_NAME+"=?",new String[]{"admin"},null,null,null);
                if(cursor.moveToFirst()){
                    String pw=cursor.getString(cursor.getColumnIndex(USER_PASSWORD));
                    if(pw.equals(eTextPWord.getText().toString())){
                        Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else{
                        Toast.makeText(LoginActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this,"未初始化",Toast.LENGTH_SHORT).show();
                }
                cursor.close();
                db.close();
            }
        });

        textInitPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db= myDataBaseHelper.getWritableDatabase(MyDataBaseHelper.DB_PASSWORD);
                Cursor cursor = db.rawQuery("select count(*) from user",null);
                cursor.moveToFirst();
                int cont = cursor.getInt(0);
                cursor.close();
                if(cont==0){
                    Intent intent=new Intent(LoginActivity.this,InitPwActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this,"已初始化，禁止重复初始化",Toast.LENGTH_LONG).show();
                }
                cursor.close();
                db.close();
            }
        });
    }
    private void requestPermission(){
        if(ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED||ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_SMS)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(LoginActivity.this,new String[]{Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS},1);
        }
        verifyStoragePermissions(this);
        if(ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(LoginActivity.this,new String[]{Manifest.permission.READ_CONTACTS},201);
        }
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
    }

}
