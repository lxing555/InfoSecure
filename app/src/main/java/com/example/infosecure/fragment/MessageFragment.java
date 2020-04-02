package com.example.infosecure.fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.infosecure.activity.AddSecretLogActivity;
import com.example.infosecure.activity.SecretLogActivity;
import com.example.infosecure.adapter.MesRecycleAdapter;
import com.example.infosecure.R;
import com.example.infosecure.entity.Infos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MessageFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    public static MesRecycleAdapter mesRecycleAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view =inflater.inflate(R.layout.fragment_message,null);
        initRecyclerView();
        return view;
    }


    private void initRecyclerView(){
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerV_message);
        mesRecycleAdapter=new MesRecycleAdapter(getActivity(), Infos.messageList);
        recyclerView.setAdapter(mesRecycleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
    }

}
