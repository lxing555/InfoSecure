package com.example.infosecure.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.infosecure.AesUtil;
import com.example.infosecure.R;
import com.example.infosecure.adapter.MyDataBaseHelper;
import com.example.infosecure.entity.Infos;
import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;


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
        SQLiteDatabase.loadLibs(this);
        myDataBaseHelper =new MyDataBaseHelper(this, MyDataBaseHelper.DB_NAME,null,1);
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
                Cursor cursor=db.query(MyDataBaseHelper.TABLE_USER,new String[]{"password"},"name=?",new String[]{"admin"},null,null,null);
                if(cursor.moveToFirst()){
                    String pw=cursor.getString(cursor.getColumnIndex("password"));
                    if(AesUtil.getDeString(pw,Infos.key).equals(eTextPWord.getText().toString())){
                        Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else{
                        Toast.makeText(LoginActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
                    }
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
                    Toast.makeText(LoginActivity.this,"已初始化，禁止重复",Toast.LENGTH_LONG).show();
                }
                cursor.close();
                db.close();
            }
        });
    }
}
