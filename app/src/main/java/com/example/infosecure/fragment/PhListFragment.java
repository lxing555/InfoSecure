package com.example.infosecure.fragment;

import android.Manifest;
import android.content.ContentResolver;
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
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_CONTACTS},201);
        }
        initView();
        return view;
    }
    private void initView(){
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerV_conPerson);
        adapter=new ConPerRecycleAdapter(getActivity(), Infos.conPersonList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
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
                SQLiteDatabase db= myDataBaseHelper.getWritableDatabase(MyDataBaseHelper.DB_PASSWORD);
                db.delete(MyDataBaseHelper.TABLE_CONPERSON,"name=?",new String[]{Infos.conPersonList.get(position).getName()});
                Infos.conPersonList.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
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

    //获取所有联系人
    public void getPhoneList(){
        ContentResolver cr = getActivity().getContentResolver();
        Cursor cursor = cr.query(phoneUri,new String[]{NUM,NAME},null,null,null);
        if(cursor.moveToFirst()){
            do{

                ConPerson con = new ConPerson(cursor.getString(cursor.getColumnIndex(NAME)),cursor.getString(cursor.getColumnIndex(NUM)),"");
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
