package cn.ian2018.testsoftwareclass.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/10/13/013.
 */

public class StudentInfoOpenHelper extends SQLiteOpenHelper {
    /**
     * 创建年级表语句
     */
    public static final String CREAT_GRADE = "create table Grade (id integer primary key autoincrement, grade_code integer)";

    /**
     * 创建班级表语句
     */
    public static final String CREAT_CLASS = "create table Clas (id integer primary key autoincrement," +
            " grade_code integer, division_code integer, professional_code integer, class_code integer," +
            " division_des text, class_des text)";

    /**
     * 创建学生表语句
     */
    public static final String CREAT_STUDENT = "create table Student (" +
            // id                                  学生姓名
            "id integer primary key autoincrement, student_name text, " +
            // 学号
            "student_nu text)";


    public StudentInfoOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAT_GRADE);    //年级表
        db.execSQL(CREAT_CLASS);       //班级表
        db.execSQL(CREAT_STUDENT);       //学生表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
