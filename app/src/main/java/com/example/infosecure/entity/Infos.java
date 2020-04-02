package com.example.infosecure.entity;

import android.content.ContentValues;
import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import android.util.Log;
import com.example.infosecure.AesUtil;
import com.example.infosecure.adapter.MyDataBaseHelper;
import java.util.ArrayList;

import static com.example.infosecure.activity.LoginActivity.myDataBaseHelper;

public class Infos {
    public static ArrayList<Message> messageList=new ArrayList<>();
    public static ArrayList<SecretLog> secretLogList =new ArrayList<>();
    public static ArrayList<ConPerson> conPersonList=new ArrayList<>();
    public static String key=AesUtil.generateKey("Faiz555","123456");

    /**
     * APP初始化时，获取数据库所有数据
     */
    public static void getInfos(){
        getMessageInfos();
        getLogInfos();
        getConPerson();
    }
    /**
     * 获取数据库表secureLog的数据
     */
    public static void getMessageInfos(){
        messageList.clear();
        SQLiteDatabase db= myDataBaseHelper.getWritableDatabase(MyDataBaseHelper.DB_PASSWORD);
        Cursor cursor=db.query(true, MyDataBaseHelper.TABLE_MESSAGE,null,null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                String name=cursor.getString(cursor.getColumnIndex("name"));
                String phone=cursor.getString(cursor.getColumnIndex("phone"));
                String time=cursor.getString(cursor.getColumnIndex("time"));
                String content=cursor.getString(cursor.getColumnIndex("content"));
                Message m=new Message(name,AesUtil.getDeString(phone,key),time,AesUtil.getDeString(content,key));
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
    public static void getLogInfos(){
        secretLogList.clear();
        SQLiteDatabase db= myDataBaseHelper.getWritableDatabase(MyDataBaseHelper.DB_PASSWORD);
        Cursor cursor=db.query(true, MyDataBaseHelper.TABLE_SECURELOG,null,null,null,null,null,null,null);
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

    public static void getConPerson(){
        conPersonList.clear();
        String[] phone=new String[5];
        SQLiteDatabase db= myDataBaseHelper.getWritableDatabase(MyDataBaseHelper.DB_PASSWORD);
        Cursor cursor=db.query(true, MyDataBaseHelper.TABLE_CONPERSON,new String[]{"name"},null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                String name=cursor.getString(cursor.getColumnIndex("name"));
                Cursor cursor1=db.query(true, MyDataBaseHelper.TABLE_CONPERSON,new String[]{"phone"},"name=?",new String[]{name},null,null,null,null);
                int i=0;
                if(cursor1.moveToFirst()){
                    do{
                        phone[i++]=AesUtil.getDeString(cursor1.getString(cursor1.getColumnIndex("phone")),key);
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
        SQLiteDatabase db= myDataBaseHelper.getWritableDatabase(MyDataBaseHelper.DB_PASSWORD);
        Cursor cursor=db.query(true, MyDataBaseHelper.TABLE_IMAGE,new String[]{"imageBase64"},"logId=?",new String[]{logId},"imageId",null,null,null);
        if(cursor.moveToFirst()){
            do{
                String base64=cursor.getString(cursor.getColumnIndex("imageBase64"));
                arr.add(AesUtil.getDeString(base64,Infos.key));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return arr;
    }
    /**
     * 将广播监听的短信插入数据库
     * @param phone 电话号
     * @param date  接收时间
     * @param content 短信内容
     */
    public static void insert_Mess(String phone,String date,String content){
        String name="未知联系人";
        ContentValues values=new ContentValues();
        String enphone=AesUtil.getEnString(phone,key);
        values.put("phone",enphone);
        values.put("time",date);
        values.put("content", AesUtil.getEnString(content,key));
        SQLiteDatabase db= myDataBaseHelper.getWritableDatabase(MyDataBaseHelper.DB_PASSWORD);
        Cursor cursor=db.query(true, MyDataBaseHelper.TABLE_CONPERSON,new String[]{"name"},"phone=?",new String[]{enphone},null,null,null,null);
        if(cursor.moveToNext()){
            name=cursor.getString(cursor.getColumnIndex("name"));
            //Log.d("查询名字成功",name);
            cursor.close();
        }else {
            Log.d("查询结果","失败");
        }
        Message m=new Message(name,enphone,date,content);
        messageList.add(m);
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
            valuesImage.put("imageBase64",log.images_encrypt.get(i));
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
        values1.put("phone",AesUtil.getEnString(c.getPhone1(),Infos.key));
        SQLiteDatabase db= myDataBaseHelper.getWritableDatabase(MyDataBaseHelper.DB_PASSWORD);
        db.insert(MyDataBaseHelper.TABLE_CONPERSON,null,values1);
        ContentValues values2=new ContentValues();
        if(!c.getPhone2().equals("")){
            values2.put("name",c.getName());
            values2.put("phone",AesUtil.getEnString(c.getPhone2(),Infos.key));
            db.insert(MyDataBaseHelper.TABLE_CONPERSON,null,values2);
        }
        db.close();
    }
}
