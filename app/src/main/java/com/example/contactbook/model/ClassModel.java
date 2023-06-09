package com.example.contactbook.model;

import java.util.List;

public class ClassModel {
    private long id;

    private String className;

    private String classGrade;

    private String formTeacherCode;

    private List<String> listStudentCode;

    private List<String> listStudentName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassGrade() {
        return classGrade;
    }

    public void setClassGrade(String classGrade) {
        this.classGrade = classGrade;
    }

    public String getFormTeacherCode() {
        return formTeacherCode;
    }

    public void setFormTeacherCode(String formTeacherCode) {
        this.formTeacherCode = formTeacherCode;
    }

    public List<String> getListStudentCode() {
        return listStudentCode;
    }

    public void setListStudentCode(List<String> listStudentCode) {
        this.listStudentCode = listStudentCode;
    }

    public List<String> getListStudentName() {
        return listStudentName;
    }

    public void setListStudentName(List<String> listStudentName) {
        this.listStudentName = listStudentName;
    }

    @Override
    public String toString() {
        return "ClassModel{" +
                "id=" + id +
                ", className='" + className + '\'' +
                ", classGrade='" + classGrade + '\'' +
                ", formTeacherCode='" + formTeacherCode + '\'' +
                ", listStudentCode=" + listStudentCode +
                ", listStudentName=" + listStudentName +
                '}';
    }
}
