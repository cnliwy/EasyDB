
package com.easydb;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.easydb.annotation.ManyToOne;
import com.easydb.annotation.Table;
import com.easydb.table.DbModel;
import com.easydb.table.ManyToOneInfo;
import com.easydb.table.OneToManyInfo;
import com.easydb.table.SqlInfo;
import com.easydb.table.TableInfo;
import com.easydb.utils.ClassUtils;
import com.easydb.utils.CursorUtils;
import com.easydb.utils.FieldUtils;
import com.easydb.utils.SqlUtils;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static android.R.attr.id;


/**
 * Created by liwy on 2017/4/18.
 */

public class EasyDB {
    private static final String TAG = "Debug SQL";
    private static boolean isDebug = true;//是否输出sql语句
    // 数据库路径和名称
    public static final String DATABASE_PATH = android.os.Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/aliwy/";
    public static final String DATABASE_FILENAME = DATABASE_PATH + "easy.db";


    private static SQLiteDatabase db;

    /**
     * 初始化数据库
     * @param name 数据库名称
     */
    public static boolean init(String name){
        File file = new File(DATABASE_PATH);
        if (!file.exists()){
            file.mkdirs();
        }
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
        }
    }

    public static void insertData(String name,int age){
        String sql  = "insert into user(name,age) values('"+ name + "'," + age + ")";
        try {
            db.execSQL(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询所有
     * @return
     */
//    public static List<User> findAll(){
//        String sql = "select * from user";
//        List<User> users = new ArrayList<>();
//        Cursor cursor = db.rawQuery(sql,null);
//        if (cursor.moveToFirst()){
//            int count = cursor.getCount();
//            for (int i = 0; i < count; i++){
//                int id = cursor.getInt(cursor.getColumnIndex("idInfo"));
//                String name = cursor.getString(cursor.getColumnIndex("name"));
//                int age = cursor.getInt(cursor.getColumnIndex("age"));
//                User user = new User(id,name,age);
//                users.add(user);
//                if (!cursor.isLast()){
//                    cursor.moveToNext();
//                }
//            }
//        }
//        return users;
//    }

//    public static void deleteAll(){
//        String sql = "delete from user";
//        try {
//            db.execSQL(sql);
//            Logger.d("清理成功");
//        } catch (SQLException e) {
//            e.printStackTrace();
//            Logger.d("清理失败");
//        }
//    }

    // 判断表是否存在
    public static boolean isTableExist(Class clazz){
        Annotation annotation = clazz.getAnnotation(Table.class);
        if (annotation != null){
            if (annotation instanceof Table){
                Table table = (Table)annotation;
            }
        }
        return false;
    }

    /**
     * 创建表
     * @param clazz
     */
    public static void createDebug(Class clazz){
        String tableName = ClassUtils.getTableName(clazz);
        Field[] fields = clazz.getDeclaredFields();
        StringBuffer sb = new StringBuffer();
        sb.append("create table " + tableName);
        if (fields.length > 0){
            sb.append("(");
            for (Field field : fields){
                if (field.getType() == Integer.TYPE || field.getType() == Integer.class){
                    sb.append(field.getName() + " integer,");
                }else if (field.getType() == String.class){
                    sb.append(field.getName() + " text,");
                }
            }
            sb = new StringBuffer(sb.substring(0,sb.length()-1));
            sb.append(")");
        }
        try {
            db.execSQL(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建表
     * @param clazz
     */
    public static void createTable(Class clazz){
        String sql = SqlUtils.createTable(clazz);
        db.execSQL(sql);
        debugSql(sql);

    }

    /**
     * 插入数据（原理测试）
     * @param obj
     */
    public static void insertDebug(Object obj){
        try {
            String tableName = ClassUtils.getTableName(obj.getClass());
            Field[] fields = obj.getClass().getDeclaredFields();
            StringBuffer sql = new StringBuffer();
            if (fields.length > 0){
                sql.append("insert into " + tableName + "(");
                for (Field field : fields){
                    if (field.getName().equals("$change") || field.getName().equals("serialVersionUID")){
                        continue;
                    }
                    sql.append(field.getName() + ",");
                }
                sql = new StringBuffer(sql.substring(0,sql.length()-1));
                sql.append(") values(");
                for (Field field : fields){
                    if (field.getName().equals("$change") || field.getName().equals("serialVersionUID")){
                        continue;
                    }
                    field.setAccessible(true);
                    if (field.getType() == String.class){
                        sql.append("'" + field.get(obj) + "',");
                    }else{
                        sql.append(field.get(obj) + ",");
                    }

                }
                sql = new StringBuffer(sql.substring(0,sql.length()-1));
                sql.append(")");
                db.execSQL(sql.toString());
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLiteException e){
            e.printStackTrace();
        }
    }

    /**
     * 通过注解的方式新增数据
     * @param obj
     */
    public static void insert(Object obj){
        SqlInfo sqlInfo = SqlUtils.insert(obj);
        if (sqlInfo != null) {
            debugSql(sqlInfo.getSql());
            db.execSQL(sqlInfo.getSql(), sqlInfo.getValues());
        }
    }

    /**
     * FindAll原理
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> findAllDebug(Class<T> clazz){
        List<T> list = new ArrayList<T>();
        Field[] fields = clazz.getDeclaredFields();
        String sql = "select * from " + ClassUtils.getTableName(clazz);
        Cursor cursor = db.rawQuery(sql,null);
        if (cursor.moveToFirst()){
            int count = cursor.getCount();
            for (int i = 0; i < count; i++){
                try {
                    Object obj = clazz.newInstance();
                    for (Field field : fields){
                        if (field.getName().equals("$change") || field.getName().equals("serialVersionUID")){
                            continue;
                        }
                        Method method = FieldUtils.getSetMethodByField(clazz,field);
                        if (field.getType() == String.class){
                            String value = cursor.getString(cursor.getColumnIndex(field.getName()));
                            FieldUtils.setFieldValue(obj,field,value);
                        }else if (field.getType() == Integer.class || field.getType() == Integer.TYPE){
                            int value = cursor.getInt(cursor.getColumnIndex(field.getName()));
                            FieldUtils.setFieldValue(obj,field,value);
                        }

                        list.add((T)obj);
                    }
                    if (!cursor.isLast())cursor.moveToNext();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
        return list;
    }

    /**
     * 查询该表下的所有数据
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> findAll(Class<T> clazz) {
        String sql = SqlUtils.findAll(clazz);
        Cursor cursor = db.rawQuery(sql, null);
        List<T> dataList = CursorUtils.getListByCursor(cursor,clazz);
        //  一对多查询
        for (int i = 0; i < dataList.size(); i++){
            Object obj = dataList.get(i);
            loadOneToMany(obj);
            dataList.set(i,(T)obj);
        }
        return dataList;
    }

    /**
     * 根据搜索条件查询数据
     * @param clazz
     * @param strCondition
     * @param <T>
     * @return
     */
    public static <T> List<T> findByCondition(Class clazz,String strCondition){
        SqlInfo sqlInfo = SqlUtils.findByCondition(clazz,strCondition);
        debugSql(sqlInfo.getSql());
        Cursor cursor = db.rawQuery(sqlInfo.getSql(),null);
        List<T> dataList = CursorUtils.getListByCursor(cursor,clazz);
        return dataList;
    }

    /**
     * 根据对象id查询数据
     * @param entity
     * @param <T>
     * @return
     */
    public static <T> T findById(Object entity){
        SqlInfo sqlInfo = SqlUtils.findById(entity);
        if (sqlInfo == null)throw new NullPointerException("this table has no primary key!(NO ID)");
        Cursor cursor = db.rawQuery(sqlInfo.getSql(),sqlInfo.getArgsStringArray());
        try {

            DbModel dbModel = CursorUtils.getDbModel(cursor);
//            T t = CursorUtils.getObjectByCursor(cursor,entity.getClass());
            T t = CursorUtils.dbModel2Entity(dbModel,entity.getClass());
            loadOneToMany(t);
            loadManyToOne(t,dbModel);
            return t;
        }
//        catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
        finally {
            if (cursor != null){
                cursor.close();
                cursor = null;
            }
        }
    }


    /**
     * ManyToOne
     * 根据查询条件查询父对象
     * @param condition
     * @param <T>
     * @return
     */
    private static <T> T findManyByCondition(String condition,Class clazz){
        SqlInfo sqlInfo = SqlUtils.findByCondition(clazz,condition);
        if (sqlInfo == null)throw new NullPointerException("this table has no primary key!(NO ID)");
        debugSql(sqlInfo.getSql());
        Cursor cursor = db.rawQuery(sqlInfo.getSql(),sqlInfo.getArgsStringArray());
        try {
            T t =  CursorUtils.getObjectByCursor(cursor,clazz);
            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }finally {
            if (cursor != null){
                cursor.close();
                cursor = null;
            }
        }
        return null;
    }

    public static void loadOneToMany(Object entity){
        if (entity == null)return;
        TableInfo table = TableInfo.get(entity);
        Collection<OneToManyInfo> ones = table.getOtms().values();
        for (OneToManyInfo one : ones){
            String condition = " where " + one.getAssociateColumn() + " = " + table.getIdInfo().getValue(entity);
            List<?> list = findByCondition(one.getOneClass(),condition);
            if (list != null && list.size() > 0){
                one.setValue(entity,list);
            }
        }
    }

    /**
     * 加载ManyToOne数据
     * @param entity
     */
    public static void loadManyToOne(Object entity,DbModel dbModel){
        if (entity == null)return;
        TableInfo table = TableInfo.get(entity);
        Collection<ManyToOneInfo> manys =  table.getMtos().values();
        for (ManyToOneInfo many : manys){
            TableInfo tableinfo = TableInfo.get(many.getDataType());
            int id = Integer.valueOf(dbModel.getInt(many.getColumn()));
            String condition = " where " + tableinfo.getIdInfo().getColumn() + " = " + id;
            Object obj = findManyByCondition(condition,tableinfo.getClazz());
            many.setValue(entity,obj);
        }
    }

    /**
     * 修改数据
     * @param entity
     */
    public static void updateById(Object entity)throws NullPointerException{
        SqlInfo sqlInfo = SqlUtils.updateById(entity);
        if (sqlInfo != null){
            debugSql(sqlInfo.getSql());
            db.execSQL(sqlInfo.getSql(),sqlInfo.getValues());
        }else{
            throw new NullPointerException("该表木有ID主键哦");
        }
    }

    /**
     * 根据条件更新数据
     * @param entity
     * @param strCondition
     */
    public static void updateByCondition(Object entity,String strCondition){
        SqlInfo sqlInfo = SqlUtils.updateByCondition(entity,strCondition);
        if (sqlInfo != null){
            debugSql(sqlInfo.getSql());
            db.execSQL(sqlInfo.getSql(),sqlInfo.getValues());
        }else{
            throw new NullPointerException("该表木有ID主键哦");
        }
    }

    /**
     * 删除此条数据
     * @param entity
     */
    public static void deleteById(Object entity){
        String sql = SqlUtils.deleteById(entity);
        if (sql == null)throw new NullPointerException("该表木有ID主键哦");
        debugSql(sql);
        db.execSQL(sql);
    }


    /**
     * 删除该表的所有数据
     * @param clazz
     */
    public static void deleteAll(Class clazz){
        db.delete(ClassUtils.getTableName(clazz),null,null);
        debugSql("已清除" + ClassUtils.getTableName(clazz) + "的所有数据");
    }

    /**
     * 删除表
     * @param clazz
     */
    public static void drop(Class clazz){
        String sql = SqlUtils.drop(clazz);
        debugSql(sql);
        db.execSQL(sql);
    }

    public static void debugSql(String sql) {
        if(isDebug) {
            Log.d(TAG,">>>>>>  " + sql);
        }
    }


}
