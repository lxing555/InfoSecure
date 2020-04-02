package com.example.infosecure.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.infosecure.R;
import com.example.infosecure.activity.ChangePWActivity;
import com.example.infosecure.activity.LoginActivity;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class UserCenterFragment extends Fragment {
    private RelativeLayout rlChangePW,rlLogOut;
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view =inflater.inflate(R.layout.fragment_usercenter,null);
        initView();
        return view;
    }
    private void initView(){
        rlChangePW=(RelativeLayout)view.findViewById(R.id.rl_change_pw);
        rlLogOut=(RelativeLayout)view.findViewById(R.id.rl_log_out);

        rlChangePW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ChangePWActivity.class);
                startActivity(intent);
            }
        });
        rlLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
}
