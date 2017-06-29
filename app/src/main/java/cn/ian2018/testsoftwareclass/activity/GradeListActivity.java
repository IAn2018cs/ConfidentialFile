package cn.ian2018.testsoftwareclass.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cn.ian2018.testsoftwareclass.R;
import cn.ian2018.testsoftwareclass.bean.Grade;
import cn.ian2018.testsoftwareclass.database.StudentInfoDB;

public class GradeListActivity extends AppCompatActivity {

    private ImageView iv_back;
    private StudentInfoDB db;
    private List<Grade> gradeList;
    private ListView lv_grade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_list);
        db = StudentInfoDB.getInstance(this);
        gradeList = db.getGrades();
        initUI();
    }

    private void initUI() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lv_grade = (ListView) findViewById(R.id.lv_grade);

        lv_grade.setAdapter(new MyAdapter());

        lv_grade.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),ClassListActivity.class);
                intent.putExtra("gradecode",gradeList.get(position).getGradeCode());
                String title = "20"+gradeList.get(position).getGradeCode()+"级";
                intent.putExtra("title",title);
                startActivity(intent);
            }
        });
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return gradeList.size();
        }

        @Override
        public Grade getItem(int position) {
            return gradeList.get(position);
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
            viewHoulder.tv_classdes.setText("20"+getItem(position).getGradeCode()+"级");

            return convertView;
        }
    }

    static class ViewHoulder {
        TextView tv_classdes;
    }
}
