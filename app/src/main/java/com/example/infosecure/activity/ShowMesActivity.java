package com.example.infosecure.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.infosecure.R;
import com.example.infosecure.entity.Infos;

import static com.example.infosecure.fragment.MessageFragment.MES_POSITION;

public class ShowMesActivity extends AppCompatActivity {
    private TextView textName,textPhone,textContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_mes);
        initView();
    }
    private void initView(){
        textName=(TextView)findViewById(R.id.text_mes_name);
        textPhone=(TextView)findViewById(R.id.text_mes_phone);
        textContent=(TextView)findViewById(R.id.text_mes_content);
        Intent intent=getIntent();
        int pos=intent.getIntExtra(MES_POSITION,0);
        textName.setText(Infos.messageList.get(pos).getName());
        textPhone.setText(Infos.messageList.get(pos).getPhone());
        //Log.d("测试电话",Infos.messageList.get(pos).getPhone());
        textContent.setText(Infos.messageList.get(pos).getContent());
    }
}
