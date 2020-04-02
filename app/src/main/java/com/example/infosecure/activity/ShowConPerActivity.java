package com.example.infosecure.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.infosecure.R;
import com.example.infosecure.entity.Infos;
import com.example.infosecure.fragment.PhListFragment;

public class ShowConPerActivity extends AppCompatActivity {
    private TextView textName,textPhone1,textPhone2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_con_per);
        initView();
    }
    private void initView(){
        textName=(TextView)findViewById(R.id.text_conper_name);
        textPhone1=(TextView)findViewById(R.id.text_conper_phone1);
        textPhone2=(TextView)findViewById(R.id.text_conper_phone2);
        Intent intent=getIntent();
        int pos=intent.getIntExtra(PhListFragment.CONPER_POSITION,0);
        textName.setText(Infos.conPersonList.get(pos).getName());
        textPhone1.setText(Infos.conPersonList.get(pos).getPhone1());
        textPhone2.setText(Infos.conPersonList.get(pos).getPhone2());
    }
}
