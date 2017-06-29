package cn.ian2018.testsoftwareclass.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.ian2018.testsoftwareclass.R;
import cn.ian2018.testsoftwareclass.bean.Clas;
import cn.ian2018.testsoftwareclass.bean.Student;
import cn.ian2018.testsoftwareclass.database.StudentInfoDB;
import cn.ian2018.testsoftwareclass.utils.ConstantValue;
import cn.ian2018.testsoftwareclass.utils.Logs;
import cn.ian2018.testsoftwareclass.utils.SpUtils;
import cn.ian2018.testsoftwareclass.utils.ToastUtli;
import okhttp3.Call;

public class InitDateActivity extends AppCompatActivity {
    private static final String URL = "http://suguan.hicc.cn/hicccloudt/getCode";
    private static final String URL2 = "http://suguan.hicc.cn/hicccloudt/getInfo";
    private StudentInfoDB db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_date);
        db = StudentInfoDB.getInstance(this);

        creatData();
    }

    // 获取专业代码 存到数据库
    private void creatData() {
        if (SpUtils.getBoolSp(this, ConstantValue.FIRST_DATA, true)) {
            // 发送GET请求
            OkHttpUtils
                    .get()
                    .url(URL)
                    .addParams("code", "16")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Logs.i("获取所有班级是失败："+e.toString());
                            // 加载失败  下次进入应用重新加载
                            SpUtils.putBoolSp(getApplicationContext(), ConstantValue.FIRST_DATA, true);
                            ToastUtli.show(getApplicationContext(), "初始化数据失败，请重新进入");
                            finish();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Logs.i(response);
                            // 解析json
                            getJsonInfo(response);
                        }
                    });
        }
    }

    // 解析json
    private void getJsonInfo(final String response) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean sucessed = jsonObject.getBoolean("sucessed");
                    if (sucessed) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        for (int i = 13; i <= 16; i++) {
                            // 年级
                            cn.ian2018.testsoftwareclass.bean.Grade grade = new cn.ian2018.testsoftwareclass.bean.Grade();
                            grade.setGradeCode(i);
                            db.saveGrade(grade);

                            JSONArray Grade = data.getJSONArray("Description_" + i);
                            for (int j = 0; j < Grade.length(); j++) {
                                JSONObject info = Grade.getJSONObject(j);

                                // 当信息全的时候再去保存
                                if (!info.getString("DivisionDescription").equals("null")) {

                                    // 班级
                                    Clas clas = new Clas();
                                    clas.setGradeCode(i);
                                    clas.setClassCode(info.getInt("Nid"));
                                    clas.setProfessionalCode(info.getInt("ProfessionalId"));
                                    clas.setDivisionCode(info.getInt("DivisionCode"));
                                    clas.setClassDes(info.getString("ClassDescription"));
                                    clas.setDivisionDes(info.getString("DivisionDescription"));

                                    db.saveClass(clas);
                                }
                            }
                        }
                        Logs.i("加载所有班级数据成功,开始加载学生信息");

                        // 获取所有学生的信息
                        getAllStudent();

                        Logs.i("加载数据成功");
                        // 下次进入应用不在加载
                        SpUtils.putBoolSp(getApplicationContext(), ConstantValue.FIRST_DATA, false);

                        // 进入主页
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                finish();
                            }
                        });
                    } else {
                        // 加载失败  下次进入应用重新加载
                        SpUtils.putBoolSp(getApplicationContext(), ConstantValue.FIRST_DATA, true);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtli.show(getApplicationContext(), "初始化数据失败，请重新进入");
                                finish();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    // 加载失败  下次进入应用重新加载
                    SpUtils.putBoolSp(getApplicationContext(), ConstantValue.FIRST_DATA, true);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtli.show(getApplicationContext(), "初始化数据失败，请重新进入");
                            finish();
                        }
                    });
                }
            }
        }.start();
    }

    // 获取所有学生
    private void getAllStudent() {
        List<Clas> allClass = db.getAllClass();

        for (Clas allClas : allClass) {
            Logs.d(allClas.getClassDes());
            Logs.d(""+allClas.getGradeCode());
            Logs.d(""+allClas.getDivisionCode());
            Logs.d(""+allClas.getProfessionalCode());
            Logs.d(""+allClas.getClassCode());
            // 发送GET请求
            OkHttpUtils
                    .get()
                    .url(URL2)
                    .addParams("timescode", String.valueOf(allClas.getGradeCode()))
                    .addParams("divisionCode", String.valueOf(allClas.getDivisionCode()))
                    .addParams("professionalCode", String.valueOf(allClas.getProfessionalCode()))
                    .addParams("classcode", String.valueOf(allClas.getClassCode()))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Logs.i("获取学生信息时失败："+e.toString());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.i("获取学生列表", response);
                            // 解析json
                            Log.i("获取学生列表", "解析json");
                            getJsonForStudent(response);
                        }
                    });

        }
    }

    // 解析每个班级的学生信息  并存到数据库中
    private void getJsonForStudent(final String response) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean sucessed = jsonObject.getBoolean("sucessed");
                    if (sucessed) {
                        Log.i("获取学生列表", "开始解析");

                        JSONArray data = jsonObject.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++) {
                            Student student = new Student();
                            JSONObject studentInfo = data.getJSONObject(i);
                            // 学生姓名
                            student.setStudentName(studentInfo.getString("StudentName"));
                            // 学号
                            student.setStudentNu(studentInfo.getString("StudentNu"));
                            db.saveStudent(student);
                            Logs.d("保存学生"+studentInfo.getString("StudentName")+"成功");
                        }

                    }
                } catch (JSONException e) {
                    // 解析错误
                    e.printStackTrace();
                    Logs.e(response+"解析学生信息异常"+e.toString());
                }
            }
        }.start();
    }

}
