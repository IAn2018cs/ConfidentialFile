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
import cn.ian2018.testsoftwareclass.bean.Clas;
import cn.ian2018.testsoftwareclass.database.StudentInfoDB;

public class ClassListActivity extends AppCompatActivity {

    private String title;
    private StudentInfoDB db;
    private List<Clas> clasList;
    private ImageView iv_back;
    private TextView tv_action_title;
    private ListView lv_class;
    private int gradeCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);
        gradeCode = getIntent().getIntExtra("gradecode", 0);
        title = getIntent().getStringExtra("title");

        db = StudentInfoDB.getInstance(this);
        clasList = db.getClass(gradeCode);

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

        tv_action_title = (TextView) findViewById(R.id.tv_action_title);
        tv_action_title.setText(title);

        lv_class = (ListView) findViewById(R.id.lv_class);

        lv_class.setAdapter(new MyAdapter());

        lv_class.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), StudentListActivity.class);
                intent.putExtra("professionalcode", clasList.get(position).getProfessionalCode());
                intent.putExtra("gradecode", clasList.get(position).getGradeCode());
                intent.putExtra("divisioncode", clasList.get(position).getDivisionCode());
                intent.putExtra("classcode", clasList.get(position).getClassCode());
                String title = clasList.get(position).getDivisionDes()+gradeCode+"级"+clasList.get(position).getClassDes();
                intent.putExtra("title", title);
                startActivity(intent);
            }
        });
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return clasList.size();
        }

        @Override
        public Clas getItem(int position) {
            return clasList.get(position);
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
            viewHoulder.tv_classdes.setText(getItem(position).getDivisionDes()+gradeCode+"级"+getItem(position).getClassDes());

            return convertView;
        }
    }

    static class ViewHoulder {
        TextView tv_classdes;
    }
}
