package com.example.infosecure.entity;

import android.content.ContentValues;
import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import android.util.Log;

import com.example.infosecure.adapter.MyDataBaseHelper;
import java.util.ArrayList;

import static com.example.infosecure.activity.FileEncryptActivity.FILE_ENCRYPT_METHOD;
import static com.example.infosecure.activity.LoginActivity.myDataBaseHelper;
import static com.example.infosecure.adapter.MyDataBaseHelper.IMAGE_BASE64;
import static com.example.infosecure.adapter.MyDataBaseHelper.IMAGE_ID;
import static com.example.infosecure.adapter.MyDataBaseHelper.IMAGE_LOGID;
import static com.example.infosecure.adapter.MyDataBaseHelper.LINKMAN_NAME;
import static com.example.infosecure.adapter.MyDataBaseHelper.LINKMAN_PHONE;
import static com.example.infosecure.adapter.MyDataBaseHelper.MESSAGE_CONNAME;
import static com.example.infosecure.adapter.MyDataBaseHelper.MESSAGE_CONTENT;
import static com.example.infosecure.adapter.MyDataBaseHelper.MESSAGE_ID;
import static com.example.infosecure.adapter.MyDataBaseHelper.MESSAGE_PHONE;
import static com.example.infosecure.adapter.MyDataBaseHelper.MESSAGE_TIME;
import static com.example.infosecure.adapter.MyDataBaseHelper.MESSAGE_TYPE;
import static com.example.infosecure.adapter.MyDataBaseHelper.SECURE_TIME;
import static com.example.infosecure.adapter.MyDataBaseHelper.USER_AESKEY;
import static com.example.infosecure.adapter.MyDataBaseHelper.USER_ENCRYPT_METHOD;

public class Infos{
    public static ArrayList<Message> messageList=new ArrayList<>();
    public static ArrayList<SecretLog> secretLogList =new ArrayList<>();
    public static ArrayList<ConPerson> conPersonList=new ArrayList<>();
    public static String AES_KEY="0123456789abcdef";
    /**
     * APP初始化时，获取数据库所有数据
     */
    public static void getInfos(){
        getMessageInfos();
        getLogInfos();
        getConPerson();
        getUserData();
        //insertData();
    }
    private void insertData(){
        insert_Mess(new Message("123","2020-04-27 10:18:15","有内鬼，终止交易！",1));
        insert_Mess(new Message("123456","2020-04-27 10:21:15","我的银行卡号1231252451223！",1));
        insert_Mess(new Message("123456789","2020-04-27 10:20:15","您的快递已到菜鸟驿站，请凭取件码1556在今天晚上8点之前来取件！",1));
        insert_Mess(new Message("123456788","2020-04-27 10:01:18","测试短信1",1));
        insert_Mess(new Message("234567","2020-04-27 10:02:15","测试短信2",1));
    }

    private static void getUserData(){
        SQLiteDatabase db=myDataBaseHelper.getReadableDatabase(MyDataBaseHelper.DB_PASSWORD);
        Cursor cursor=db.query(MyDataBaseHelper.TABLE_USER,null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            AES_KEY=cursor.getString(cursor.getColumnIndex(USER_AESKEY));
            FILE_ENCRYPT_METHOD=cursor.getInt(cursor.getColumnIndex(USER_ENCRYPT_METHOD));
        }
        cursor.close();
        db.close();
    }
    /**
     * 获取数据库表secureLog的数据
     */
    private static void getMessageInfos(){
        messageList.clear();
        SQLiteDatabase db= myDataBaseHelper.getReadableDatabase(MyDataBaseHelper.DB_PASSWORD);
        Cursor cursor=db.query(true, MyDataBaseHelper.TABLE_MESSAGE,null,null,null,null,null,MESSAGE_TIME,null);
        if(cursor.moveToFirst()){
            do{
                String name=cursor.getString(cursor.getColumnIndex(MESSAGE_CONNAME));
                String phone=cursor.getString(cursor.getColumnIndex(MESSAGE_PHONE));
                String time=cursor.getString(cursor.getColumnIndex(MESSAGE_TIME));
                String content=cursor.getString(cursor.getColumnIndex(MESSAGE_CONTENT));
                String id=cursor.getString(cursor.getColumnIndex(MESSAGE_ID));
                int type=cursor.getInt(cursor.getColumnIndex(MESSAGE_TYPE));
                Message m=new Message(name,phone,time,content,id,type);
                messageList.add(m);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    /**
     * 获取数据库表secureLog的数据
     */
    private static void getLogInfos(){
        secretLogList.clear();
        SQLiteDatabase db= myDataBaseHelper.getReadableDatabase(MyDataBaseHelper.DB_PASSWORD);
        Cursor cursor=db.query(true, MyDataBaseHelper.TABLE_SECURELOG,null,null,null,null,null,SECURE_TIME,null);
        if(cursor.moveToFirst()){
            do{
                String title=cursor.getString(cursor.getColumnIndex("title"));
                String time=cursor.getString(cursor.getColumnIndex("time"));
                String content=cursor.getString(cursor.getColumnIndex("content"));
                String logId=cursor.getString(cursor.getColumnIndex("logId"));
                SecretLog m=new SecretLog(title,content,time,logId);
                m.images_base64=getBase64(logId);
                secretLogList.add(m);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    private static void getConPerson(){
        conPersonList.clear();
        String[] phone=new String[15];
        SQLiteDatabase db= myDataBaseHelper.getReadableDatabase(MyDataBaseHelper.DB_PASSWORD);
        Cursor cursor=db.query(true, MyDataBaseHelper.TABLE_LINKMAN,new String[]{LINKMAN_NAME},null,null,null,null,LINKMAN_NAME,null);
        if(cursor.moveToFirst()){
            do{
                String name=cursor.getString(cursor.getColumnIndex(LINKMAN_NAME));
                Cursor cursor1=db.query(true, MyDataBaseHelper.TABLE_LINKMAN,new String[]{LINKMAN_PHONE},LINKMAN_NAME+"=?",new String[]{name},null,null,null,null);
                int i=0;
                if(cursor1.moveToFirst()){
                    do{
                        phone[i++]=cursor1.getString(cursor1.getColumnIndex("phone"));
                    }while(cursor1.moveToNext());
                }
                cursor1.close();
                ConPerson c=new ConPerson(name,phone[0],phone[1]);
                conPersonList.add(c);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    /**
     * @param logId 将私密日志的图片全部转换为Base64编码
     * @return
     */
    public static ArrayList<String> getBase64(String logId){
        ArrayList<String>arr=new ArrayList<String>();
        SQLiteDatabase db= myDataBaseHelper.getReadableDatabase(MyDataBaseHelper.DB_PASSWORD);
        Cursor cursor=db.query(true, MyDataBaseHelper.TABLE_IMAGE,new String[]{IMAGE_BASE64},IMAGE_LOGID+"=?",new String[]{logId},IMAGE_ID,null,null,null);
        if(cursor.moveToFirst()){
            do{
                String base64=cursor.getString(cursor.getColumnIndex(IMAGE_BASE64));
                arr.add(base64);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return arr;
    }
    /**
     * 将广播监听的短信插入数据库
     * @param mes 待插入短信
     */
    public static void insert_Mess(Message mes){
        String name="未知联系人";
        ContentValues values=new ContentValues();
        values.put(MESSAGE_PHONE,mes.getPhone());
        values.put(MESSAGE_TIME,mes.getDate());
        values.put(MESSAGE_CONTENT,mes.getContent());
        values.put(MESSAGE_TYPE,mes.getType());
        SQLiteDatabase db= myDataBaseHelper.getWritableDatabase(MyDataBaseHelper.DB_PASSWORD);
        Cursor cursor=db.query(true, MyDataBaseHelper.TABLE_LINKMAN,new String[]{"name"},"phone=?",new String[]{mes.getPhone()},null,null,null,null);
        if(cursor.moveToNext()){
            name=cursor.getString(cursor.getColumnIndex(LINKMAN_NAME));
            //Log.d("查询名字成功",name);
            cursor.close();
        }else {
            Log.d("查询结果","失败");
        }
        mes.setName(name);
        messageList.add(mes);
        values.put("name",name);
        db.insert(MyDataBaseHelper.TABLE_MESSAGE,null,values);
        Log.d("database","数据插入成功");
        cursor.close();
        db.close();
    }

    /**将日志插入数据库
     * @param log 要插入的日志
     */
    public static void insert_secretLog(SecretLog log){
        ContentValues valuesLog=new ContentValues();
        valuesLog.put("logId",log.getLogId());
        valuesLog.put("title",log.getTitle());
        valuesLog.put("time",log.getTime());
        valuesLog.put("content",log.getContent());
        SQLiteDatabase db= myDataBaseHelper.getWritableDatabase(MyDataBaseHelper.DB_PASSWORD);
        db.insert(MyDataBaseHelper.TABLE_SECURELOG,null,valuesLog);
        for(int i=0;i<log.images_base64.size();i++){
            ContentValues valuesImage=new ContentValues();
            valuesImage.put("logId",log.getLogId());
            valuesImage.put("imageBase64",log.images_base64.get(i));
            db.insert(MyDataBaseHelper.TABLE_IMAGE,null,valuesImage);
        }
        db.close();
    }

    /**将联系人插入数据库
     * @param c 联系人
     */
    public static void insert_conperson(ConPerson c){
        ContentValues values1=new ContentValues();
        values1.put("name",c.getName());
        values1.put("phone",c.getPhone1());
        SQLiteDatabase db= myDataBaseHelper.getWritableDatabase(MyDataBaseHelper.DB_PASSWORD);
        db.insert(MyDataBaseHelper.TABLE_LINKMAN,null,values1);
        ContentValues values2=new ContentValues();
        if(c.getPhone2()!=null){
            values2.put("name",c.getName());
            values2.put("phone",c.getPhone2());
            db.insert(MyDataBaseHelper.TABLE_LINKMAN,null,values2);
        }
        db.close();
    }
}
