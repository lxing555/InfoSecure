package com.example.infosecure.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;

import net.sqlcipher.database.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.infosecure.AesUtil;
import com.example.infosecure.R;
import com.example.infosecure.adapter.MyDataBaseHelper;
import com.example.infosecure.entity.Infos;

public class InitPwActivity extends AppCompatActivity {
    private EditText eTextPW,eTextRePW;
    private LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_pw);
        initView();
    }
    private void initView(){
        eTextPW=(EditText)findViewById(R.id.et_init_pwd);
        eTextRePW=(EditText)findViewById(R.id.et_init_pwconfirm);
        linearLayout=(LinearLayout)findViewById(R.id.ll_initpw_confirm);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db=LoginActivity.myDataBaseHelper.getWritableDatabase(MyDataBaseHelper.DB_PASSWORD);
                String newOne=eTextPW.getText().toString();
                String newTwo=eTextRePW.getText().toString();
                if(newOne.equals("")){
                    Toast.makeText(InitPwActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(newTwo.equals("")){
                    Toast.makeText(InitPwActivity.this,"请确认密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!newOne.equals(newTwo)){
                    Toast.makeText(InitPwActivity.this,"密码不一致！",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(newOne.length()<6||newTwo.length()<6){
                    Toast.makeText(InitPwActivity.this,"密码长度过短",Toast.LENGTH_SHORT).show();
                    return;
                }
                ContentValues values=new ContentValues();
                values.put("name","admin");
                values.put("password", AesUtil.getEnString(newOne,Infos.key));
                db.insert(MyDataBaseHelper.TABLE_USER,null,values);
                Toast.makeText(InitPwActivity.this,"初始化成功",Toast.LENGTH_SHORT).show();
                db.close();
                finish();
            }
        });
    }

}
