package cn.ian2018.testsoftwareclass.bean;

/**
 * Created by Administrator on 2016/10/13/013.
 * 班级表
 */

public class Clas {
    private String classDes;
    private String divisionDes;
    private int classCode;
    private int gradeCode;
    private int divisionCode;
    private int professionalCode;

    public String getDivisionDes() {
        return divisionDes;
    }

    public void setDivisionDes(String divisionDes) {
        this.divisionDes = divisionDes;
    }

    public int getDivisionCode() {
        return divisionCode;
    }

    public void setDivisionCode(int divisionCode) {
        this.divisionCode = divisionCode;
    }

    public int getGradeCode() {
        return gradeCode;
    }

    public void setGradeCode(int gradeCode) {
        this.gradeCode = gradeCode;
    }

    public String getClassDes() {
        return classDes;
    }

    public void setClassDes(String classDes) {
        this.classDes = classDes;
    }

    public int getClassCode() {
        return classCode;
    }

    public void setClassCode(int classCode) {
        this.classCode = classCode;
    }

    public int getProfessionalCode() {
        return professionalCode;
    }

    public void setProfessionalCode(int professionalCode) {
        this.professionalCode = professionalCode;
    }
}
