package com.example.infosecure.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.infosecure.R;
import com.example.infosecure.activity.AddSecretLogActivity;
import com.example.infosecure.activity.FileEncryptActivity;
import com.example.infosecure.activity.SecretLogActivity;
import com.example.infosecure.activity.ShowLogActivity;
import com.example.infosecure.adapter.MyDataBaseHelper;
import com.example.infosecure.adapter.SecretLogRecycleAdapter;
import com.example.infosecure.entity.Infos;

import net.sqlcipher.database.SQLiteDatabase;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.infosecure.activity.LoginActivity.myDataBaseHelper;

public class MainFragment extends Fragment {
    private RecyclerView recyclerView;
    private SecretLogRecycleAdapter secretLogRecycleAdapter;
    private Context context;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View FragMain =inflater.inflate(R.layout.fragment_main,null);
        setHasOptionsMenu(true);
        context=getContext();
        initView(FragMain);
        return FragMain;
    }
    private void initView(View view){
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerV_secretLog);
        secretLogRecycleAdapter=new SecretLogRecycleAdapter(context, Infos.secretLogList);
        recyclerView.setAdapter(secretLogRecycleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false));
        //recyclerView.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));
        secretLogRecycleAdapter.setOnItemClickListener(new SecretLogRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                Toast.makeText(getContext(), "You clicked item " + position, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getContext(), ShowLogActivity.class);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });
        secretLogRecycleAdapter.setmOnItemLongClickListener(new SecretLogRecycleAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClicked(View v, int position) {
                Toast.makeText(getContext(), "You long long clicked item " + position, Toast.LENGTH_SHORT).show();
                showDialog(getActivity(),position);
            }
        });
    }
    private void showDialog(Context context, final int position){
        AlertDialog.Builder dialog=new AlertDialog.Builder(context);
        dialog.setMessage("确认要删除所选内容吗？");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SQLiteDatabase db= myDataBaseHelper.getWritableDatabase(MyDataBaseHelper.DB_PASSWORD);
                db.delete(MyDataBaseHelper.TABLE_SECURELOG,"logId=?",new String[]{Infos.secretLogList.get(position).getLogId()});
                db.delete(MyDataBaseHelper.TABLE_IMAGE,"logId=?",new String[]{Infos.secretLogList.get(position).getLogId()});
                Infos.secretLogList.remove(position);
                secretLogRecycleAdapter.notifyDataSetChanged();
                db.close();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_secret_log, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_secret_log_add:{
                Intent intent=new Intent(getContext(), AddSecretLogActivity.class);
                startActivity(intent);
            }break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();
        secretLogRecycleAdapter.notifyDataSetChanged();
    }
}
