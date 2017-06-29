package cn.ian2018.testsoftwareclass.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import cn.ian2018.testsoftwareclass.R;

public class HomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initUI();
    }

    private void initUI() {
        Button bt_all =  (Button) findViewById(R.id.bt_all);
        Button bt_name =  (Button) findViewById(R.id.bt_name);
        Button bt_num =  (Button) findViewById(R.id.bt_num);

        // 全校所有信息
        bt_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),GradeListActivity.class));
            }
        });

        // 根据学号查询
        bt_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),StudentForNumActivity.class));
            }
        });

        // 根据姓名查询
        bt_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),StudentForNameActivity.class));
            }
        });
    }
}
