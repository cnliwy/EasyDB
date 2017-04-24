package com.liwy.mobile.easydb;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.liwy.mobile.easydb.bean.Student;
import com.liwy.mobile.easydb.bean.User;
import com.orhanobut.logger.Logger;
import com.yanzhenjie.permission.AndPermission;

import java.io.File;
import java.util.List;

import static com.liwy.mobile.easydb.EasyDB.DATABASE_FILENAME;
import static com.liwy.mobile.easydb.EasyDB.DATABASE_PATH;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView contentTv;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Logger.init("暮醉南山");
        contentTv = (TextView)findViewById(R.id.tv_content);
        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        btn3 = (Button)findViewById(R.id.btn3);
        btn4 = (Button)findViewById(R.id.btn4);
        btn5 = (Button)findViewById(R.id.btn5);
        btn6 = (Button)findViewById(R.id.btn6);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn1:
                File file = new File(DATABASE_PATH);
                if (!file.exists()){
                    boolean result = file.mkdirs();
                }
                if(EasyDB.init(DATABASE_FILENAME)){
                    Logger.d("数据库创建成功");
                }
                break;
            case R.id.btn2:
//                EasyDB.createTable();
                EasyDB.createTable(User.class);
//                EasyDB.create(Student.class);
                break;
            case R.id.btn3:
//                for (int i = 0; i < 4; i++){
//                    EasyDB.insertData("name" + i, i);
                    EasyDB.insert(new User(12,"tom",25));
//                    EasyDB.insert(new Student(233,"zhangsan","男"));

//                }
                break;
            case R.id.btn4:
//                List<Student> datas = EasyDB.findAll(Student.class);
                List<User> datas = EasyDB.findAll(User.class);
                if (datas != null && datas.size() > 0) {
                    contentTv.setText(datas.get(0).toString());
                }else {
                    contentTv.setText("啥都没查到");
                }
                break;
            case R.id.btn5:
                EasyDB.deleteAll();
                break;
            case R.id.btn6:
                EasyDB.drop(User.class);
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
}
