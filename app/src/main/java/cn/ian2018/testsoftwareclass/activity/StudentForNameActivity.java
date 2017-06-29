package cn.ian2018.testsoftwareclass.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cn.ian2018.testsoftwareclass.R;
import cn.ian2018.testsoftwareclass.bean.Student;
import cn.ian2018.testsoftwareclass.database.StudentInfoDB;
import cn.ian2018.testsoftwareclass.utils.Logs;
import cn.ian2018.testsoftwareclass.utils.ToastUtli;

public class StudentForNameActivity extends AppCompatActivity {

    private ImageView iv_back;
    private EditText et_search;
    private ImageButton ib_search;
    private ListView lv_name;
    private StudentInfoDB db;
    private List<Student> studentList;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_for_name);

        // 创建数据库实例
        db = StudentInfoDB.getInstance(this);

        findUI();

        searchForName();
    }

    private void searchForName() {
        ib_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_search.getText().toString().trim();
                if(!name.equals("")){
                    showProgressDialog();
                    // 从数据库中查询
                    queryStudent(name);
                }else{
                    ToastUtli.show(getApplicationContext(),"姓名不能为空");
                }
            }
        });
    }

    private void queryStudent(final String name) {
        new Thread(){
            @Override
            public void run() {
                Logs.d("数据库里的学生信息人数:"+db.getAllStudentForName().size());
                // 根据姓名 从数据库中查询
                studentList = db.getStudentForName(name);
                Logs.d("长度为："+studentList.size());

                //studentList = db.getAllStudentForName();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 设置数据适配器
                        lv_name.setAdapter(new MyAdapter());
                        closeProgressDialog();
                    }
                });
            }
        }.start();
    }

    private void findUI() {
        // 返回
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        et_search = (EditText) findViewById(R.id.et_search);
        ib_search = (ImageButton) findViewById(R.id.ib_search);

        lv_name = (ListView) findViewById(R.id.lv_name);

        lv_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),StudentProfileActivity.class);
                intent.putExtra("studentNu",studentList.get(position).getStudentNu());
                startActivity(intent);
            }
        });
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return studentList.size();
        }

        @Override
        public Student getItem(int position) {
            return studentList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHoulder viewHoulder;
            if(convertView == null){
                convertView = View.inflate(getApplicationContext(),R.layout.item_class,null);
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
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                }
            });
        }
        progressDialog.show();
    }

    // 关闭进度对话框
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
