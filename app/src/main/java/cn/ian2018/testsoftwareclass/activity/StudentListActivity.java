package cn.ian2018.testsoftwareclass.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.ian2018.testsoftwareclass.R;
import cn.ian2018.testsoftwareclass.bean.Student;
import cn.ian2018.testsoftwareclass.utils.ToastUtli;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/10/26/026.
 */
public class StudentListActivity extends AppCompatActivity {
    private List<Student> mStudentList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private ListView lv_student;
    private String URL = "http://suguan.hicc.cn/hicccloudt/getInfo";
    private SwipeRefreshLayout sw_refresh;
    private String title;
    private TextView tv_action_title;
    private int professionalCode;
    private int gradecode;
    private int divisioncode;
    private int classcode;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentlist);
        professionalCode = getIntent().getIntExtra("professionalcode", 0);
        gradecode = getIntent().getIntExtra("gradecode", 0);
        divisioncode = getIntent().getIntExtra("divisioncode", 0);
        classcode = getIntent().getIntExtra("classcode", 0);
        title = getIntent().getStringExtra("title");
        initUI();
        tv_action_title.setText(title);

        showProgressDialog();
        initData();
    }

    private void initData() {

        // 发送GET请求
        OkHttpUtils
                .get()
                .url(URL)
                .addParams("timescode", String.valueOf(gradecode))
                .addParams("divisionCode", String.valueOf(divisioncode))
                .addParams("professionalCode", String.valueOf(professionalCode))
                .addParams("classcode", String.valueOf(classcode))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        sw_refresh.setEnabled(true);
                        sw_refresh.setRefreshing(false);
                        closeProgressDialog();
                        Log.i("获取学生列表", e.toString());
                        Toast.makeText(getApplication(), "服务器繁忙，请重新查询", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        Log.i("获取学生列表", response);
                        // 解析json
                        Log.i("获取学生列表", "解析json");
                        getJsonInfo(response);
                    }
                });
    }

    private void getJsonInfo(final String response) {
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

                            // 现场报道状态
                            student.setLiveReportStatueDescription(studentInfo.getString("LiveReportStatueDescription"));

                            // 性别
                            student.setGenderDescription(studentInfo.getString("GenderDescription"));
                            // 班级
                            student.setClassDescription(studentInfo.getString("ClassDescription"));
                            // 照片
                            String imageUrl = studentInfo.getString("NewImage");
                            if (!imageUrl.equals("null")) {
                                student.setImageUrl("http://home.hicc.cn/StudentImage/" + imageUrl);
                            } else {
                                imageUrl = studentInfo.getString("OldImage");
                                student.setImageUrl("http://home.hicc.cn/OldImage/" + imageUrl);
                            }
                            // 专业
                            student.setProfessionalDescription(studentInfo.getString("ProfessionalDescription"));
                            // 缴费状态
                            student.setPaymentStausDescription(studentInfo.getString("PaymentStausDescription"));
                            // 民族
                            student.setNationalDescription(studentInfo.getString("NationalDescription"));
                            // 省份
                            student.setProvinceDescription(studentInfo.getString("ProvinceDescription"));
                            // 年级代码
                            student.setGradeCode(studentInfo.getInt("GradeCode"));
                            // -宿舍
                            student.setDormitoryDescription(studentInfo.getString("DormitoryDescription"));
                            // 宿舍号
                            if (!studentInfo.getString("DormitoryNo").equals("null")) {
                                int dormitoryNo = studentInfo.getInt("DormitoryNo");
                                student.setDormitoryNo(dormitoryNo);
                            }
                            // -学部
                            student.setDivisionDescription(studentInfo.getString("DivisionDescription"));
                            // -电话
                            student.setYourPhone(studentInfo.getString("YourPhone"));
                            // -年级
                            student.setGradeDescription(studentInfo.getString("GradeDescription"));
                            // -床号
                            student.setBedNumber(studentInfo.getString("BedNumber"));
                            // -家庭住址
                            student.setHomeAddress(studentInfo.getString("HomeAddress"));
                            // -政治面貌
                            student.setPoliticsStatusDescription(studentInfo.getString("PoliticsStatusDescription"));
                            // -现场报道
                            student.setLiveReportStatueDescription(studentInfo.getString("LiveReportStatueDescription"));
                            // -网上报道
                            student.setOnlineReportStatueDescription(studentInfo.getString("OnlineReportStatueDescription"));


                            mStudentList.add(student);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("获取学生列表", "大小是：" + mStudentList.size());
                                lv_student.setAdapter(new MyAdapter());
                                closeProgressDialog();
                                sw_refresh.setRefreshing(false);
                                // 成功时设置不可下拉刷新
                                sw_refresh.setEnabled(false);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                closeProgressDialog();
                                sw_refresh.setRefreshing(false);
                                // 成功时设置不可下拉刷新
                                sw_refresh.setEnabled(false);
                                ToastUtli.show(getApplicationContext(),"没有该班级的数据");
                            }
                        });
                    }
                } catch (JSONException e) {
                    // 解析错误
                    e.printStackTrace();
                    Toast.makeText(getApplication(), "加载失败", Toast.LENGTH_SHORT).show();
                    sw_refresh.setEnabled(true);
                    sw_refresh.setRefreshing(false);
                    closeProgressDialog();
                }
            }
        }.start();
    }

    private void initUI() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lv_student = (ListView) findViewById(R.id.lv_student);
        sw_refresh = (SwipeRefreshLayout) findViewById(R.id.sw_refresh);
        tv_action_title = (TextView) findViewById(R.id.tv_action_title);

        // 初始设置不可下拉刷新
        sw_refresh.setEnabled(false);
        sw_refresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light, android.R.color.holo_orange_light);

        lv_student.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("获取学生列表", mStudentList.get(position).getStudentNu());
                // 学生档案
                Intent intent = new Intent(getApplicationContext(), StudentProfileActivity.class);
                intent.putExtra("studentNu", mStudentList.get(position).getStudentNu());
                intent.putExtra("student", (Serializable) mStudentList.get(position));
                startActivity(intent);
            }
        });

        //下拉加载
        sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mStudentList.size();
        }

        @Override
        public Student getItem(int position) {
            return mStudentList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHoulder viewHoulder;
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.item_class, null);
                viewHoulder = new ViewHoulder();
                viewHoulder.tv_classdes = (TextView) convertView.findViewById(R.id.tv_classdes);
                convertView.setTag(viewHoulder);
            }
            viewHoulder = (ViewHoulder) convertView.getTag();
            viewHoulder.tv_classdes.setText(getItem(position).getStudentName());

            return convertView;
        }
    }

    static class ViewHoulder {
        TextView tv_classdes;
    }

    // 显示进度对话框
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("加载中...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    return;
                }
            });
        }
        progressDialog.show();
    }

    // 关闭进度对话框
    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
