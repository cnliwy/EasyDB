package com.liwy.mobile.easydb;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.lang.reflect.Field;
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
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
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
                EasyDB.createTable();
                break;
            case R.id.btn3:
                for (int i = 0; i < 4; i++){
                    EasyDB.insertData("name" + i, i);
                }
                break;
            case R.id.btn4:
                List<User> users = EasyDB.findAll();
                contentTv.setText(users.toString());
                break;
            case R.id.btn5:
                EasyDB.deleteAll();
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
