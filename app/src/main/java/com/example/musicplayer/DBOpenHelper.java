package com.example.musicplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.IntRange;

import java.util.ArrayList;

public class DBOpenHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;// 声明一个AndroidSDK自带的数据库变量db
    //类的构造函数
    public DBOpenHelper(Context context){
        super(context,"db_test",null,1);
        db = getReadableDatabase();
    }
    //重写方法
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS user(" + "_id INTEGER PRIMARY KEY AUTOINCREMENT," + "name TEXT," + "password TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }
    //增删改
    public void add(String name,String password){
        db.execSQL("INSERT INTO user (name,password) VALUES(?,?)",new Object[]{name,password});
    }
    public void delete(String name,String password){
        db.execSQL("DELETE FROM user WHERE name = AND password ="+name+password);
    }
    public void updata(String password){
        db.execSQL("UPDATE user SET password = ?",new Object[]{password});
    }
     //查询出来的内容，需要有个容器存放，所以定义了一个ArrayList类的list
     //有了容器,使用游标Cursor查询出来内容的排序方式："name DESC"
     //接下来写一个while循环，让游标从表头游到表尾
     //在游的过程中把游出来的数据存放到list容器中
    public ArrayList<User> getAllData(){
        ArrayList<User> list = new ArrayList<User>();
        Cursor cursor = db.query("user",null,null,null,null,null,"name DESC");
        while(cursor.moveToNext()){
            @SuppressLint("Range")
            String name = cursor.getString(cursor.getColumnIndex("name"));
            @SuppressLint("Range")
            String password = cursor.getString(cursor.getColumnIndex("password"));
            list.add(new User(name,password));
        }
        return list;
    }
}
