package com.easydb.mobile.easydb;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.easydb.EasyDB;
import com.easydb.mobile.easydb.bean.House;
import com.easydb.mobile.easydb.bean.User;
import com.easydb.table.TableInfo;
import com.easydb.utils.SqlUtils;
import com.easydb.utils.Utils;
import com.orhanobut.logger.Logger;
import com.yanzhenjie.permission.AndPermission;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.easydb.EasyDB.DATABASE_FILENAME;
import static com.easydb.EasyDB.debugSql;
import static com.easydb.EasyDB.findById;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView contentTv;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;
    private Button btn7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Logger.init("暮醉南山");
        if(EasyDB.init(DATABASE_FILENAME)){
            Logger.d("数据库创建成功");
        }
        contentTv = (TextView)findViewById(R.id.tv_content);
        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        btn3 = (Button)findViewById(R.id.btn3);
        btn4 = (Button)findViewById(R.id.btn4);
        btn5 = (Button)findViewById(R.id.btn5);
        btn6 = (Button)findViewById(R.id.btn6);
        btn7 = (Button)findViewById(R.id.btn7);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn1:
                EasyDB.createTable(House.class);
                EasyDB.createTable(User.class);
                break;
            case R.id.btn2:
                insert();
                break;
            case R.id.btn3:
                //                findAll();
//                findById();
                findByCondition();
                break;
            case R.id.btn4:
                User updateObj = new User(10,"jecknew");
//                EasyDB.updateById(updateObj);
                EasyDB.updateByCondition(updateObj,"where age = 24 and _id = 10");
                break;
            case R.id.btn5:
//                EasyDB.deleteAll();//
                User user = new User(13,"liwy");
                EasyDB.deleteById(user);
                break;
            case R.id.btn6:
                EasyDB.drop(User.class);
                EasyDB.drop(House.class);
                break;
            case R.id.btn7:
//                findOneToMany();
                findManyToOne();
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AndPermission.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)){
            Logger.d("已经有读写SD卡的权限了");
        }else{
            AndPermission.with(this)
                    .requestCode(120)
                    .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                    .send();
        }
    }
    // 插入数据
    public void insert(){
        House house = new House(1,"西湖小区");
        EasyDB.insert(house);
        User user1 = new User(12,"tom");
        user1.setDead(true);
        user1.setWeight(55.66);
        user1.setMarried(true);
        user1.setHouse(house);
        Date date = new Date();
        System.out.println(date.toString());
        user1.setDate("2017-05-12 12:25:12");
        user1.setRemark("聪明机智");
        EasyDB.insert( user1);

        User user2= new User(11,"tom2");
        user2.setHouse(house);
        EasyDB.insert(user2);
//        EasyDB.insert(new User(10,"lll"));
//        EasyDB.insert(new User(13,"wzs"));
//        EasyDB.insert(new User(14,"hjk"));
    }
    //根据id查询OneToMany
    public void findOneToMany(){
//        House findHouse = new House();
//        findHouse.setId(1);
//        List<User> users = new ArrayList<User>();
//        users.add(new User(1,"wangliu",2));
//        findHouse.setUsers(users);
//        House house = EasyDB.findById(findHouse);
        List<House> houses = EasyDB.findAll(House.class);
        for (House house : houses){
            System.out.println(house.toString());
            if (house.getUsers() == null)continue;
            for (User user : house.getUsers()){
                System.out.println(user.toString());
            }
            contentTv.setText(house.toString());
        }
    }

    /**
     * manytoOne演示
     */
    public void findManyToOne(){
        User user = new User();
        user.setId(11);
        House house = new House();
        house.setId(1);
        user.setHouse(house);

        User tom = EasyDB.findByIdWithRelation(user);
        System.out.println(tom.toString());
        if (tom.getHouse() != null){
            System.out.println(tom.getHouse().toString());
            contentTv.setText(tom.getHouse().toString());
        }else{
            System.out.println("未查到House数据");
        }

    }

    // 根据id查询用户
    public void findById(){
        User user = EasyDB.findById(new User(12));
        printUser(user);
    }
    //查询所有
    public void findAll(){
        List<User> users = EasyDB.findAll(User.class);
        printUsers(users);
    }
    //根据查询条件查询数据
    public void findByCondition(){
       List<User> users =  EasyDB.findByCondition(User.class," where houseId = 1");
        printUsers(users);
    }

    /**
     * 输出查询内容
     * @param users
     */
    public void printUsers(List<User> users){
        StringBuffer sb = new StringBuffer();
        if (users != null && users.size() > 0){
            for (User user : users){
                sb.append(user.getName()).append(",");
            }
        }else {
            sb.append("无数据");
        }
        contentTv.setText(sb.toString());
    }

    public void printUser(User user){
        if (user == null){
            contentTv.setText("无数据");
        }else{
            contentTv.setText(user.toString());
        }
    }


}
