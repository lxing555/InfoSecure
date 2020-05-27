package com.example.infosecure.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.infosecure.R;
import com.example.infosecure.adapter.MyDataBaseHelper;

public class ChangePWActivity extends AppCompatActivity {
    private EditText eTextOldPW,eTextNewPW,eTextNewPWRe;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pw);
        //getSupportActionBar().hide();
        initView();
    }
    private void initView(){
        eTextOldPW=(EditText)findViewById(R.id.et_old_pwd);
        eTextNewPW=(EditText)findViewById(R.id.et_new_pwd1);
        eTextNewPWRe=(EditText)findViewById(R.id.et_new_pwd2);
        linearLayout=(LinearLayout)findViewById(R.id.ll_changepw_confirm);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db=LoginActivity.myDataBaseHelper.getWritableDatabase(MyDataBaseHelper.DB_PASSWORD);
                String old=eTextOldPW.getText().toString();
                String newOne=eTextNewPW.getText().toString();
                String newTwo=eTextNewPWRe.getText().toString();
                Cursor cursor=db.query(MyDataBaseHelper.TABLE_USER,new String[]{"password"},"name=?",new String[]{"admin"},null,null,null,null);
                if(old.equals("")){
                    Toast.makeText(ChangePWActivity.this,"请输入原密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(cursor.moveToFirst()){
                    String oldpw=cursor.getString(cursor.getColumnIndex("password"));
                    if(!oldpw.equals(old)){
                        Toast.makeText(ChangePWActivity.this,"原密码错误",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if(newOne.equals("")){
                    Toast.makeText(ChangePWActivity.this,"请输入新密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(newTwo.equals("")){
                    Toast.makeText(ChangePWActivity.this,"请确认新密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!newOne.equals(newTwo)){
                    Toast.makeText(ChangePWActivity.this,"新密码不一致！",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(newOne.length()<6){
                    Toast.makeText(ChangePWActivity.this,"密码长度过短",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(newOne.equals(old)){
                    Toast.makeText(ChangePWActivity.this,"新密码和原密码一样！",Toast.LENGTH_SHORT).show();
                    return;
                }
                ContentValues values=new ContentValues();
                values.put("password", newOne);
                db.update(MyDataBaseHelper.TABLE_USER,values,"name=?",new String[]{"admin"});
                Toast.makeText(ChangePWActivity.this,"密码更新成功",Toast.LENGTH_SHORT).show();
                cursor.close();
                db.close();
                finish();
            }
        });
    }
}
