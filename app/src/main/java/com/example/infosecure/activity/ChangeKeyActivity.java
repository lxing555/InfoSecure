package com.example.infosecure.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.infosecure.R;
import com.example.infosecure.adapter.MyDataBaseHelper;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import static com.example.infosecure.adapter.MyDataBaseHelper.USER_AESKEY;
import static com.example.infosecure.adapter.MyDataBaseHelper.USER_NAME;
import static com.example.infosecure.adapter.MyDataBaseHelper.USER_PASSWORD;

public class ChangeKeyActivity extends AppCompatActivity {
    private EditText eTextPW,eTextOldKey,eTextNewKey1,eTextNewKey2;
    private LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_key);
        //getSupportActionBar().hide();
        initView();
    }
    private void initView(){
        eTextPW=(EditText)findViewById(R.id.et_old_pwd_changekey);
        eTextOldKey=(EditText)findViewById(R.id.et_old_key_changekey);
        eTextNewKey1=(EditText)findViewById(R.id.et_new_key1);
        eTextNewKey2=(EditText)findViewById(R.id.et_new_key2);
        linearLayout=(LinearLayout)findViewById(R.id.ll_changekey_confirm);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db=LoginActivity.myDataBaseHelper.getWritableDatabase(MyDataBaseHelper.DB_PASSWORD);
                String password=eTextPW.getText().toString();
                String oldKey=eTextOldKey.getText().toString();
                String newOne=eTextNewKey1.getText().toString();
                String newTwo=eTextNewKey2.getText().toString();
                Cursor cursor=db.query(MyDataBaseHelper.TABLE_USER,new String[]{"password"},"name=?",new String[]{"admin"},null,null,null,null);
                if(password.equals("")){
                    Toast.makeText(ChangeKeyActivity.this,"请输入原密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(oldKey.equals("")){
                    Toast.makeText(ChangeKeyActivity.this,"请输入原密钥",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(cursor.moveToFirst()){
                    String oldpw=cursor.getString(cursor.getColumnIndex(USER_PASSWORD));
                    String oldkey=cursor.getString(cursor.getColumnIndex(USER_AESKEY));
                    if(!oldpw.equals(password)){
                        Toast.makeText(ChangeKeyActivity.this,"原密码错误",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(!oldkey.equals(oldKey)){
                        Toast.makeText(ChangeKeyActivity.this,"原密钥错误",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if(newOne.equals("")){
                    Toast.makeText(ChangeKeyActivity.this,"请输入新密钥",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(newTwo.equals("")){
                    Toast.makeText(ChangeKeyActivity.this,"请确认新密钥",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!newOne.equals(newTwo)){
                    Toast.makeText(ChangeKeyActivity.this,"新密钥不一致！",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(newOne.equals(oldKey)){
                    Toast.makeText(ChangeKeyActivity.this,"新密钥和原密钥一样！",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(newOne.length()!=16&&newOne.length()!=24&&newOne.length()!=32){
                    Toast.makeText(ChangeKeyActivity.this,"请确保密钥为16/24/32位",Toast.LENGTH_SHORT).show();
                    return;
                }
                ContentValues values=new ContentValues();
                values.put(USER_AESKEY, newOne);
                db.update(MyDataBaseHelper.TABLE_USER,values,USER_NAME+"=?",new String[]{"admin"});
                Toast.makeText(ChangeKeyActivity.this,"密钥更新成功",Toast.LENGTH_SHORT).show();
                cursor.close();
                db.close();
                finish();
            }
        });
    }
}
