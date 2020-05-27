package com.example.infosecure.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import net.sqlcipher.database.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.infosecure.R;
import com.example.infosecure.adapter.MyDataBaseHelper;

import static com.example.infosecure.adapter.MyDataBaseHelper.USER_AESKEY;
import static com.example.infosecure.adapter.MyDataBaseHelper.USER_ENCRYPT_METHOD;
import static com.example.infosecure.adapter.MyDataBaseHelper.USER_NAME;
import static com.example.infosecure.adapter.MyDataBaseHelper.USER_PASSWORD;

public class InitPwActivity extends AppCompatActivity {
    private EditText eTextPW,eTextRePW,eTextAesKey,eTextReAesKey;
    private LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_pw);
        //getSupportActionBar().hide();
        initView();
    }
    private void initView(){
        eTextPW=(EditText)findViewById(R.id.et_init_pwd);
        eTextRePW=(EditText)findViewById(R.id.et_init_pwconfirm);
        eTextAesKey=(EditText)findViewById(R.id.et_init_aeskey);
        eTextReAesKey=(EditText)findViewById(R.id.et_init_aeskeyconfirm);
        linearLayout=(LinearLayout)findViewById(R.id.ll_initpw_confirm);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db=LoginActivity.myDataBaseHelper.getWritableDatabase(MyDataBaseHelper.DB_PASSWORD);
                String newOne=eTextPW.getText().toString();
                String newTwo=eTextRePW.getText().toString();
                String aesKey=eTextAesKey.getText().toString();
                String aeskeyTwo=eTextReAesKey.getText().toString();
                if(newOne.equals("")){
                    Toast.makeText(InitPwActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(newTwo.equals("")){
                    Toast.makeText(InitPwActivity.this,"请确认密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(aesKey.equals("")){
                    Toast.makeText(InitPwActivity.this,"请输入密钥",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!newOne.equals(newTwo)){
                    Toast.makeText(InitPwActivity.this,"密码不一致！",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!aesKey.equals(aeskeyTwo)){
                    Toast.makeText(InitPwActivity.this,"密钥不一致！",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(newOne.length()<6){
                    Toast.makeText(InitPwActivity.this,"密码长度过短",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(aesKey.length()!=16&&aesKey.length()!=24&&aesKey.length()!=32){
                    Toast.makeText(InitPwActivity.this,"请确保密钥为16/24/32位",Toast.LENGTH_SHORT).show();
                    return;
                }
                ContentValues values=new ContentValues();
                values.put(USER_NAME,"admin");
                values.put(USER_PASSWORD, newOne);
                values.put(USER_AESKEY,aesKey);
                values.put(USER_ENCRYPT_METHOD,0);
                db.insert(MyDataBaseHelper.TABLE_USER,null,values);
                Toast.makeText(InitPwActivity.this,"初始化成功",Toast.LENGTH_SHORT).show();
                db.close();
                finish();
            }
        });
    }

}
