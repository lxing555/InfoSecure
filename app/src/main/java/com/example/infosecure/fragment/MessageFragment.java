package com.example.infosecure.fragment;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.infosecure.activity.AddSecretLogActivity;
import com.example.infosecure.activity.SecretLogActivity;
import com.example.infosecure.activity.ShowMesActivity;
import com.example.infosecure.adapter.MesRecycleAdapter;
import com.example.infosecure.R;
import com.example.infosecure.adapter.MyDataBaseHelper;
import com.example.infosecure.entity.Infos;
import com.example.infosecure.entity.Message;

import net.sqlcipher.database.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.infosecure.activity.LoginActivity.myDataBaseHelper;
import static com.example.infosecure.adapter.MyDataBaseHelper.MESSAGE_ID;
import static com.example.infosecure.adapter.MyDataBaseHelper.TABLE_MESSAGE;

public class MessageFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    public static MesRecycleAdapter mesRecycleAdapter;
    public static final String MES_POSITION="position";
    public static final String SMS_URI_ALL = "content://sms/";
    public static final String SMS_URI_INBOX = "content://sms/inbox";
    public static final String SMS_URI_SEND = "content://sms/sent";
    public static final String SMS_URI_DRAFT = "content://sms/draft";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view =inflater.inflate(R.layout.fragment_message,null);
        setHasOptionsMenu(true);
        initRecyclerView();
        return view;
    }

    private void initRecyclerView(){
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerV_message);
        mesRecycleAdapter=new MesRecycleAdapter(getActivity(), Infos.messageList);
        recyclerView.setAdapter(mesRecycleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        mesRecycleAdapter.setOnItemClickListener(new MesRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent=new Intent(getActivity(), ShowMesActivity.class);
                intent.putExtra(MES_POSITION,position);
                startActivity(intent);
            }
        });
        mesRecycleAdapter.setmOnItemLongClickListener(new MesRecycleAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClicked(View v, int position) {
                showDialog(getContext(),position);
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
                db.delete(TABLE_MESSAGE,MESSAGE_ID+"=?",new String[]{Infos.messageList.get(position).getMesId()});
                Infos.messageList.remove(position);
                mesRecycleAdapter.notifyDataSetChanged();
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
        inflater.inflate(R.menu.menu_message, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_message_getinfo:{
                getMessageList();
            }break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void getMessageList(){
        ContentResolver cr = getActivity().getContentResolver();       //打开ContentResolver查询本地短信
        String[] projection = new String[]{"address","body", "date", "type"};
        Uri uri=Uri.parse(SMS_URI_ALL);
        Cursor cursor = cr.query(uri,projection,null,null,null);
        if(cursor.moveToFirst()){
            do{
                String phone=cursor.getString(cursor.getColumnIndex("address"));//获取联系人姓名
                int type=cursor.getInt(cursor.getColumnIndex("type"));
                String date=timeStamp2Date(cursor.getString(cursor.getColumnIndex("date")));
                String content=cursor.getString(cursor.getColumnIndex("body"));
                Log.d("Message","手机号"+phone+"类型"+type+"日期"+date+"内容"+content);
                Message mes=new Message(phone,date,content,type);
                Infos.insert_Mess(mes);
                mesRecycleAdapter.notifyDataSetChanged();
            }while (cursor.moveToNext());
        }
    }
    public static String timeStamp2Date(String seconds) {
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }
        String format = "yyyy-MM-dd HH:mm:ss";

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds)));
    }

}
