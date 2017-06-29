package cn.ian2018.testsoftwareclass.activity;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.ian2018.testsoftwareclass.R;
import cn.ian2018.testsoftwareclass.utils.ConstantValue;
import cn.ian2018.testsoftwareclass.utils.SpUtils;

public class MainActivity extends AppCompatActivity {

    private String imei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Vibrator vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);

        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        imei = tm.getDeviceId();
        //imei = "A100004C7B9CBB";

        TextView tv_warning = (TextView) findViewById(R.id.tv_warning);
        if (!imei.equals("A100004C7B9CBB")){
            tv_warning.setVisibility(View.VISIBLE);
            vibrator.vibrate(new long[]{500,2000},0);
        }

        final EditText et_psw = (EditText) findViewById(R.id.et_psw);
        Button bt_send = (Button) findViewById(R.id.bt_send);

        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imei.equals("A100004C7B9CBB")){
                    String pwd = et_psw.getText().toString().trim();
                    if(pwd.equals("15511297542")){
                        if(SpUtils.getBoolSp(getApplicationContext(), ConstantValue.FIRST_DATA,true)){
                            startActivity(new Intent(getApplicationContext(),InitDateActivity.class));
                            finish();
                        } else {
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                            finish();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),"密码错误",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"别想看了，我帮你退出吧",Toast.LENGTH_SHORT).show();
                    finish();
                    System.exit(0);
                }
            }
        });
    }
}
