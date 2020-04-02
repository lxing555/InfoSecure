package com.example.infosecure.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;

import net.sqlcipher.database.SQLiteDatabase;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.infosecure.R;
import com.example.infosecure.adapter.MyDataBaseHelper;
import com.example.infosecure.adapter.SecretLogRecycleAdapter;
import com.example.infosecure.entity.Infos;

import static com.example.infosecure.activity.LoginActivity.myDataBaseHelper;

public class SecretLogActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SecretLogRecycleAdapter secretLogRecycleAdapter;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secret_log);
        context=this;
        initRecyclerView();
    }
    private void initRecyclerView(){
        recyclerView=(RecyclerView)findViewById(R.id.recyclerV_secretLog);
        secretLogRecycleAdapter=new SecretLogRecycleAdapter(context,Infos.secretLogList);
        recyclerView.setAdapter(secretLogRecycleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));
        secretLogRecycleAdapter.setOnItemClickListener(new SecretLogRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                Toast.makeText(SecretLogActivity.this, "You clicked item " + position, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(SecretLogActivity.this,ShowLogActivity.class);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });
        secretLogRecycleAdapter.setmOnItemLongClickListener(new SecretLogRecycleAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClicked(View v, int position) {
                Toast.makeText(SecretLogActivity.this, "You long long clicked item " + position, Toast.LENGTH_SHORT).show();
                SQLiteDatabase db= myDataBaseHelper.getWritableDatabase(MyDataBaseHelper.DB_PASSWORD);
                db.delete(MyDataBaseHelper.TABLE_SECURELOG,"logId=?",new String[]{Infos.secretLogList.get(position).getLogId()});
                Log.d("LogId",Infos.secretLogList.get(position).getLogId());
                db.delete(MyDataBaseHelper.TABLE_IMAGE,"logId=?",new String[]{Infos.secretLogList.get(position).getLogId()});
                Infos.secretLogList.remove(position);
                secretLogRecycleAdapter.notifyDataSetChanged();
                db.close();
            }
        });
    }


    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        MenuInflater menuInflater=new MenuInflater(this);
        menuInflater.inflate(R.menu.menu_secret_log,menu);
        return super.onCreatePanelMenu(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_secret_log_add:{
                Intent intent=new Intent(SecretLogActivity.this, AddSecretLogActivity.class);
                startActivity(intent);
            }break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume(){
        super.onResume();
        secretLogRecycleAdapter.notifyDataSetChanged();
    }
}
