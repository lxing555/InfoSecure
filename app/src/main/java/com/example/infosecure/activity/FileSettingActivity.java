package com.example.infosecure.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.infosecure.R;
import com.example.infosecure.adapter.MyDataBaseHelper;

import net.sqlcipher.database.SQLiteDatabase;

import static com.example.infosecure.activity.FileEncryptActivity.FILE_ENCRYPT_METHOD;
import static com.example.infosecure.activity.FileEncryptActivity.FILE_MANAGE_MODE;
import static com.example.infosecure.activity.LoginActivity.myDataBaseHelper;
import static com.example.infosecure.adapter.MyDataBaseHelper.USER_AESKEY;
import static com.example.infosecure.adapter.MyDataBaseHelper.USER_ENCRYPT_METHOD;
import static com.example.infosecure.adapter.MyDataBaseHelper.USER_NAME;
import static javax.crypto.Cipher.ENCRYPT_MODE;

public class FileSettingActivity extends AppCompatActivity {
    private RelativeLayout rlChangeMode,rlChangeMethod;
    private TextView textMode,textMethod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_setting);
        initView();
    }
    private void initView(){
        rlChangeMode=(RelativeLayout)findViewById(R.id.rl_change_encrypt_mode);
        rlChangeMethod=(RelativeLayout)findViewById(R.id.rl_change_encrypt_method);
        textMode=(TextView)findViewById(R.id.text_encrypt_mode);
        textMethod=(TextView)findViewById(R.id.text_encrypt_mehod);
        if(FILE_MANAGE_MODE==1){
            textMode.setText("解密");
        }
        if(FILE_ENCRYPT_METHOD==1){
            textMethod.setText("CBC");
        }
        rlChangeMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleChoiceDialog1();
            }
        });

        rlChangeMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleChoiceDialog2();
            }
        });
    }
    private void showSingleChoiceDialog1(){
        final String[] items = { "加密","解密" };
        AlertDialog.Builder singleChoiceDialog = new AlertDialog.Builder(FileSettingActivity.this);
        singleChoiceDialog.setTitle("选择加解密模式");

        singleChoiceDialog.setSingleChoiceItems(items,FILE_MANAGE_MODE,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FILE_MANAGE_MODE = which;
                    }
                });
        singleChoiceDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (FILE_MANAGE_MODE != -1) {
                            Toast.makeText(FileSettingActivity.this,
                                    "你选择了" + items[FILE_MANAGE_MODE],
                                    Toast.LENGTH_SHORT).show();
                            textMode.setText(items[FILE_MANAGE_MODE]);
                        }
                    }
                });
        singleChoiceDialog.show();
    }
    private void showSingleChoiceDialog2(){
        final String[] items = { "ECB","CBC" };
        AlertDialog.Builder singleChoiceDialog = new AlertDialog.Builder(FileSettingActivity.this);
        singleChoiceDialog.setTitle("选择加解密方法");

        singleChoiceDialog.setSingleChoiceItems(items,FILE_ENCRYPT_METHOD,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FILE_ENCRYPT_METHOD = which;
                    }
                });
        singleChoiceDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (FILE_ENCRYPT_METHOD != -1) {
                            Toast.makeText(FileSettingActivity.this,
                                    "你选择了" + items[FILE_ENCRYPT_METHOD],
                                    Toast.LENGTH_SHORT).show();
                            textMethod.setText(items[FILE_ENCRYPT_METHOD]);
                            SQLiteDatabase db=myDataBaseHelper.getWritableDatabase(MyDataBaseHelper.DB_PASSWORD);
                            ContentValues values=new ContentValues();
                            values.put(USER_ENCRYPT_METHOD,FILE_ENCRYPT_METHOD);
                            db.update(MyDataBaseHelper.TABLE_USER,values,USER_NAME+"=?",new String[]{"admin"});
                            db.close();
                        }
                    }
                });
        singleChoiceDialog.show();
    }
}
