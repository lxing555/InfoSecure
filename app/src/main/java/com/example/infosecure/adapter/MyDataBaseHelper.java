package com.example.infosecure.adapter;

import android.content.Context;
import android.widget.Toast;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabase.CursorFactory;
import net.sqlcipher.database.SQLiteOpenHelper;


public class MyDataBaseHelper extends SQLiteOpenHelper {
    private Context mContext;
    private static final String TAG = "TestSQLite";
    public static final String TABLE_LINKMAN ="linkMan";
    public static final String TABLE_MESSAGE="message";
    public static final String TABLE_SECURELOG="secureLog";
    public static final String TABLE_IMAGE="image";
    public static final String TABLE_USER="user";
    public static final String DB_NAME = "Info.db";//数据库名字
    public static final String DB_PASSWORD = "Faiz555";//数据库密码
    public static final int DB_VERSION = 5;//数据库版本


    public static final String MESSAGE_ID = "messageId";
    public static final String MESSAGE_CONNAME = "name";
    public static final String MESSAGE_PHONE = "phone";
    public static final String MESSAGE_TIME = "time";
    public static final String MESSAGE_CONTENT = "content";
    public static final String MESSAGE_TYPE="type";

    public static final String LINKMAN_NAME = "name";
    public static final String LINKMAN_PHONE = "phone";

    public static final String SECURE_ID = "logId";
    public static final String SECURE_TITLE = "title";
    public static final String SECURE_TIME = "time";
    public static final String SECURE_CONTENT = "content";

    public static final String IMAGE_ID = "imageId";
    public static final String IMAGE_LOGID = "logId";
    public static final String IMAGE_BASE64 = "imageBase64";

    public static final String USER_NAME = "username";
    public static final String USER_PASSWORD = "password";
    public static final String USER_AESKEY="aesKey";
    public static final String USER_ENCRYPT_METHOD="encryptMethod";


    //带全部参数的构造函数，此构造函数必不可少
    public MyDataBaseHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库sql语句并执行
        String sql1 = "create table linkMan(name varchar(20) not null,phone char(11) primary key)";
        String sql2 = "create table message(messageId integer primary key autoincrement,name varchar(20)not null,phone char(11) not null,time date,content text,type int(1))";
        String sql3 = "create table secureLog(logId char(14) primary key,title text not null,time date,content text)";
        String sql4 = "create table image(imageId integer primary key autoincrement,logId char(14) references secureLog(logId),imageBase64 text)";
        String sql5 = "create table user(username varchar(20) primary key not null,password varchar(20) not null,aesKey char(32) not null,encryptMethod int(1) not null)";
        db.execSQL(sql1);
        db.execSQL(sql2);
        db.execSQL(sql3);
        db.execSQL(sql4);
        db.execSQL(sql5);
        Toast.makeText(mContext,"Create succeeded", Toast.LENGTH_LONG).show();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists secureLog");
        db.execSQL("drop table if exists image");
        db.execSQL("drop table if exists linkMan");
        db.execSQL("drop table if exists message");
        db.execSQL("drop table if exists user");
        onCreate(db);
        Toast.makeText(mContext,"Upgrade succeeded",Toast.LENGTH_LONG).show();
    }

}
