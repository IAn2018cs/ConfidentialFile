package cn.ian2018.testsoftwareclass.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.ian2018.testsoftwareclass.bean.Clas;
import cn.ian2018.testsoftwareclass.bean.Grade;
import cn.ian2018.testsoftwareclass.bean.Student;

/**
 * Created by Administrator on 2016/10/13/013.
 */

public class StudentInfoDB {
    /**
     * 数据库名
     */
    public static final String DB_NAME = "hicc_student_info";

    /**
     * 数据库版本
     */
    public static final int VERSION = 1;

    private static StudentInfoDB studentInfoDB;
    private SQLiteDatabase db;

    // 私有化构造方法   单例模式
    private StudentInfoDB(Context context){
        StudentInfoOpenHelper dbHelper = new StudentInfoOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    // 获取 StudentInfoDB 实例
    public synchronized static StudentInfoDB getInstance(Context context){
        if(studentInfoDB == null){
            studentInfoDB = new StudentInfoDB(context);
        }
        return studentInfoDB;
    }

    /**
     * 年级
     * 将 Grade 实例存到数据库中
     */
    public void saveGrade(Grade grade){
        if(grade != null){
            ContentValues values = new ContentValues();
            values.put("grade_code", grade.getGradeCode());
            db.insert("Grade",null,values);
        }
    }

    /**
     * 获取所有年级
     * @return 年级集合
     */
    public List<Grade> getGrades(){
        List<Grade> list = new ArrayList<Grade>();
        Cursor cursor = db.query("Grade", null, null, null, null, null, null);
        while (cursor.moveToNext()){
            Grade grade = new Grade();
            grade.setGradeCode(cursor.getInt(cursor.getColumnIndex("grade_code")));

            list.add(grade);
        }
        cursor.close();
        return list;
    }


    /**
     * 班级
     * 将 Clas 实例存到数据库中
     */
    public void saveClass(Clas clas){
        if(clas != null){
            ContentValues values = new ContentValues();
            values.put("class_des", clas.getClassDes());
            values.put("class_code", clas.getClassCode());
            values.put("professional_code", clas.getProfessionalCode());
            values.put("grade_code", clas.getGradeCode());
            values.put("division_code", clas.getDivisionCode());
            values.put("division_des", clas.getDivisionDes());
            db.insert("Clas",null,values);
        }
    }

    /**
     * 获取所有班级
     * @return 班级集合
     */
    public List<Clas> getAllClass(){
        List<Clas> list = new ArrayList<Clas>();
        Cursor cursor = db.query("Clas", null, null, null, null, null, null);
        while (cursor.moveToNext()){
            Clas clas = new Clas();
            clas.setClassDes(cursor.getString(cursor.getColumnIndex("class_des")));
            clas.setClassCode(cursor.getInt(cursor.getColumnIndex("class_code")));
            clas.setGradeCode(cursor.getInt(cursor.getColumnIndex("grade_code")));
            clas.setProfessionalCode(cursor.getInt(cursor.getColumnIndex("professional_code")));
            clas.setDivisionCode(cursor.getInt(cursor.getColumnIndex("division_code")));
            clas.setDivisionDes(cursor.getString(cursor.getColumnIndex("division_des")));

            list.add(clas);
        }
        cursor.close();
        return list;
    }

    /**
     * 获取年级下的所有班级
     * @param gradeCode 年级代码
     * @return 班级集合
     */
    public List<Clas> getClass(int gradeCode){
        List<Clas> list = new ArrayList<Clas>();
        Cursor cursor = db.query("Clas", null, "grade_code = ?", new String[]{String.valueOf(gradeCode)}, null, null, null);
        while (cursor.moveToNext()){
            Clas clas = new Clas();
            clas.setClassDes(cursor.getString(cursor.getColumnIndex("class_des")));
            clas.setClassCode(cursor.getInt(cursor.getColumnIndex("class_code")));
            clas.setGradeCode(cursor.getInt(cursor.getColumnIndex("grade_code")));
            clas.setProfessionalCode(cursor.getInt(cursor.getColumnIndex("professional_code")));
            clas.setDivisionCode(cursor.getInt(cursor.getColumnIndex("division_code")));
            clas.setDivisionDes(cursor.getString(cursor.getColumnIndex("division_des")));

            list.add(clas);
        }
        cursor.close();
        return list;
    }

    /**
     * 学生
     * 将 Student 实例存到数据库中
     */
    public void saveStudent(Student student){
        if(student != null){
            ContentValues values = new ContentValues();
            values.put("student_name", student.getStudentName());
            values.put("student_nu", student.getStudentNu());

            db.insert("Student",null,values);
        }
    }


    /**
     * 根据姓名查找学生
     * @param name 学生姓名
     * @return 学生集合
     */
    public List<Student> getStudentForName(String name){
        List<Student> list = new ArrayList<Student>();
        Cursor cursor = db.query("Student", null, "student_name = ?", new String[]{name}, null, null, null);
        while (cursor.moveToNext()){
            Student student = new Student();
            student.setId(cursor.getInt(cursor.getColumnIndex("id")));
            student.setStudentName(cursor.getString(cursor.getColumnIndex("student_name")));
            student.setStudentNu(cursor.getString(cursor.getColumnIndex("student_nu")));

            list.add(student);
        }
        cursor.close();
        return list;
    }

    /**
     * 根据姓名查找学生
     * @return 学生集合
     */
    public List<Student> getAllStudentForName(){
        List<Student> list = new ArrayList<Student>();
        Cursor cursor = db.query("Student", null, null, null, null, null, null);
        while (cursor.moveToNext()){
            Student student = new Student();
            student.setId(cursor.getInt(cursor.getColumnIndex("id")));
            student.setStudentName(cursor.getString(cursor.getColumnIndex("student_name")));
            student.setStudentNu(cursor.getString(cursor.getColumnIndex("student_nu")));

            list.add(student);
        }
        cursor.close();
        return list;
    }

}
