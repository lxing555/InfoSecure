package com.example.infosecure.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.example.infosecure.R;
import com.example.infosecure.entity.ConPerson;
import com.example.infosecure.entity.Infos;
import com.example.infosecure.entity.SecretLog;

public class AddConPerActivity extends AppCompatActivity {
    private EditText eTextName,eTextPhone1,eTextPhone2;
    private LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_conper);
        initView();
    }
    private void initView(){
        eTextName=(EditText)findViewById(R.id.et_add_name);
        eTextPhone1=(EditText)findViewById(R.id.et_add_phone1);
        eTextPhone2=(EditText)findViewById(R.id.et_add_phone2);
        linearLayout=(LinearLayout)findViewById(R.id.ll_conper_add);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=eTextName.getText().toString();
                String phone1=eTextPhone1.getText().toString();
                String phone2=eTextPhone2.getText().toString();
                if(phone2==null)phone2="";
                ConPerson conPerson=new ConPerson(name,phone1,phone2);
                Infos.insert_conperson(conPerson);
                Infos.conPersonList.add(conPerson);
                finish();
            }
        });

    }
}
