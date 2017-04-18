
package com.liwy.mobile.easydb;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.liwy.mobile.easydb.bean.Student;
import com.liwy.mobile.easydb.bean.User;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liwy on 2017/4/18.
 */

public class EasyDB {
    // 数据库路径和名称
    public static final String DATABASE_PATH = android.os.Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/liwy/";
    public static final String DATABASE_FILENAME = DATABASE_PATH + "easy.db";


    private static SQLiteDatabase db;

    /**
     * 初始化数据库
     * @param name 数据库名称
     */
    public static boolean init(String name){
        db = SQLiteDatabase.openOrCreateDatabase(name,null);
        if (db != null){
            return true;
        }else{
            return false;
        }
    }

    public static void createTable(){
        String sql = "create table user(_id integer primary key autoincrement,name text,age integer)";
        try {
            db.execSQL(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            Logger.d("表已存在");
        }
    }

    public static void insertData(String name,int age){
        String sql  = "insert into user(name,age) values('"+ name + "'," + age + ")";
        try {
            db.execSQL(sql);
            Logger.d("插入成功");
        } catch (SQLException e) {
            e.printStackTrace();
            Logger.d("插入失败");
        }
    }

    /**
     * 查询所有
     * @return
     */
    public static List<User> findAll(){
        String sql = "select * from user";
        List<User> users = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql,null);
        if (cursor.moveToFirst()){
            int count = cursor.getCount();
            for (int i = 0; i < count; i++){
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                int age = cursor.getInt(cursor.getColumnIndex("age"));
                User user = new User(id,name,age);
                users.add(user);
                if (!cursor.isLast()){
                    cursor.moveToNext();
                }
            }
        }
        return users;
    }

    public static void deleteAll(){
        String sql = "delete from user";
        try {
            db.execSQL(sql);
            Logger.d("清理成功");
        } catch (SQLException e) {
            e.printStackTrace();
            Logger.d("清理失败");
        }
    }

    public static void insertStudent(Student stu){


    }


    public static void createStudent(Class clazz){

    }


}
