package com.example.contactbook.model;

public class SortMark {

    private String userCode;

    private String subjectName;

    private String mark1S;

    private String mark2S;

    private String finalMark;

    private String teacherName;

    private String feedback;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getMark1S() {
        return mark1S;
    }

    public void setMark1S(String mark1S) {
        this.mark1S = mark1S;
    }

    public String getMark2S() {
        return mark2S;
    }

    public void setMark2S(String mark2S) {
        this.mark2S = mark2S;
    }


    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getFinalMark() {
        return finalMark;
    }

    public void setFinalMark(String finalMark) {
        this.finalMark = finalMark;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
