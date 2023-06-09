package com.example.contactbook.model;

public class Mark {

    private long markId;
    private double halfMark;
    private double semesterMark;
    private double finalMark;
    private String halfFeedback;
    private String semesterFeedback;
    private String studentCode;
    private String teacherCode;
    private long markSubjectId;
    private String subjectName;
    private String teacherName;
    private String studentName;
    private String semester;

    public long getMarkId() {
        return markId;
    }

    public void setMarkId(long markId) {
        this.markId = markId;
    }

    public double getHalfMark() {
        return halfMark;
    }

    public void setHalfMark(double halfMark) {
        this.halfMark = halfMark;
    }

    public double getSemesterMark() {
        return semesterMark;
    }

    public void setSemesterMark(double semesterMark) {
        this.semesterMark = semesterMark;
    }

    public double getFinalMark() {
        return finalMark;
    }

    public void setFinalMark(double finalMark) {
        this.finalMark = finalMark;
    }

    public String getHalfFeedback() {
        return halfFeedback;
    }

    public void setHalfFeedback(String halfFeedback) {
        this.halfFeedback = halfFeedback;
    }

    public String getSemesterFeedback() {
        return semesterFeedback;
    }

    public void setSemesterFeedback(String semesterFeedback) {
        this.semesterFeedback = semesterFeedback;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getTeacherCode() {
        return teacherCode;
    }

    public void setTeacherCode(String teacherCode) {
        this.teacherCode = teacherCode;
    }

    public long getMarkSubjectId() {
        return markSubjectId;
    }

    public void setMarkSubjectId(long markSubjectId) {
        this.markSubjectId = markSubjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    @Override
    public String toString() {
        return "Mark{" +
                "markId=" + markId +
                ", halfMark=" + halfMark +
                ", semesterMark=" + semesterMark +
                ", finalMark=" + finalMark +
                ", halfFeedback='" + halfFeedback + '\'' +
                ", semesterFeedback='" + semesterFeedback + '\'' +
                ", studentCode='" + studentCode + '\'' +
                ", teacherCode='" + teacherCode + '\'' +
                ", markSubjectId=" + markSubjectId +
                ", subjectName='" + subjectName + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", studentName='" + studentName + '\'' +
                ", semester='" + semester + '\'' +
                '}';
    }
}
