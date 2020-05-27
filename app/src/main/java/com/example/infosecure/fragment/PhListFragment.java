package com.example.infosecure.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;


import net.sqlcipher.database.SQLiteDatabase;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.infosecure.activity.AddConPerActivity;
import com.example.infosecure.R;
import com.example.infosecure.activity.ShowConPerActivity;
import com.example.infosecure.adapter.ConPerRecycleAdapter;
import com.example.infosecure.adapter.MyDataBaseHelper;
import com.example.infosecure.entity.ConPerson;
import com.example.infosecure.entity.Infos;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.infosecure.activity.LoginActivity.myDataBaseHelper;
import static com.example.infosecure.adapter.MyDataBaseHelper.MESSAGE_ID;
import static com.example.infosecure.adapter.MyDataBaseHelper.TABLE_MESSAGE;

public class PhListFragment extends Fragment {
    private RecyclerView recyclerView;
    private ConPerRecycleAdapter adapter;
    private View view;
    public static final String CONPER_POSITION="position";
    // 号码
    public final static String NUM = ContactsContract.CommonDataKinds.Phone.NUMBER;
    // 联系人姓名
    public final static String NAME = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
    //联系人提供者的uri
    private Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view =inflater.inflate(R.layout.fragment_phonelist,null);
        setHasOptionsMenu(true);
        initView();
        return view;
    }
    private void initView(){
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerV_conPerson);
        adapter=new ConPerRecycleAdapter(getActivity(), Infos.conPersonList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));

        adapter.setOnItemClickListener(new ConPerRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent=new Intent(getActivity(), ShowConPerActivity.class);
                intent.putExtra(CONPER_POSITION,position);
                startActivity(intent);
            }
        });
        adapter.setmOnItemLongClickListener(new ConPerRecycleAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClicked(View v, int position) {
                showDialog(getContext(),position);
            }
        });
    }

    private void showDialog(Context context, final int position){
        AlertDialog.Builder dialog=new AlertDialog.Builder(context);
        //dialog.setTitle("确认要删除所选内容吗？");
        dialog.setMessage("确认要删除所选内容吗？");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SQLiteDatabase db= myDataBaseHelper.getWritableDatabase(MyDataBaseHelper.DB_PASSWORD);
                db.delete(MyDataBaseHelper.TABLE_LINKMAN,"name=?",new String[]{Infos.conPersonList.get(position).getName()});
                Infos.conPersonList.remove(position);
                adapter.notifyDataSetChanged();
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
        inflater.inflate(R.menu.menu_con_person, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_conperson_add:{
                Intent intent=new Intent(getActivity(), AddConPerActivity.class);
                startActivity(intent);
            }break;
            case R.id.item_conperson_getinfo:{
                getPhoneList();
                adapter.notifyDataSetChanged();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //获取手机本地所有联系人
    private void getPhoneList(){
        ContentResolver cr = getActivity().getContentResolver();       //打开ContentResolver查询本地联系人
        Cursor cursor = cr.query(phoneUri,new String[]{NUM,NAME},null,null,null);
        if(cursor.moveToFirst()){
            do{
                String conName=cursor.getString(cursor.getColumnIndex(NAME));//获取联系人姓名
                String conPhone=cursor.getString(cursor.getColumnIndex(NUM));//获取联系人电话
                ConPerson con = new ConPerson(conName,conPhone);
                if(Infos.conPersonList.indexOf(con)==-1){
                    Infos.insert_conperson(con);
                    Infos.conPersonList.add(con);
                }
            }while (cursor.moveToNext());
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
