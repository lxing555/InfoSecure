package com.example.infosecure.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.infosecure.R;
import com.example.infosecure.activity.FileEncryptActivity;
import com.example.infosecure.activity.SecretLogActivity;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MainFragment extends Fragment {
    private Button btEncrypt,btDecode,btSecretLog,btPower;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View FragMain =inflater.inflate(R.layout.fragment_main,null);
        initView(FragMain);
        return FragMain;
    }
    private void initView(View view){
        btDecode=(Button)view.findViewById(R.id.bt_decode);
        btEncrypt=(Button)view.findViewById(R.id.bt_encrypt);
        btSecretLog=(Button)view.findViewById(R.id.bt_secret);
        btPower=(Button)view.findViewById(R.id.bt_power);

        btEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), FileEncryptActivity.class);
                startActivity(intent);
            }
        });

        btDecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btSecretLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SecretLogActivity.class);
                startActivity(intent);
            }
        });
    }

}
