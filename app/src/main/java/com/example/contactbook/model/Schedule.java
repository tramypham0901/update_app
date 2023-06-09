package com.example.contactbook.model;

public class Schedule {
    private long scheduleId;

    private String scheduleTime;

    private String slotName;

    private String scheduleFrom;

    private String scheduleTo;

    private String className;

    private String subjectName;

    private String subjectGrade;

    private String teacherName;

    private String attendance;

    public long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public String getSlotName() {
        return slotName;
    }

    public void setSlotName(String slotName) {
        this.slotName = slotName;
    }

    public String getScheduleFrom() {
        return scheduleFrom;
    }

    public void setScheduleFrom(String scheduleFrom) {
        this.scheduleFrom = scheduleFrom;
    }

    public String getScheduleTo() {
        return scheduleTo;
    }

    public void setScheduleTo(String scheduleTo) {
        this.scheduleTo = scheduleTo;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectGrade() {
        return subjectGrade;
    }

    public void setSubjectGrade(String subjectGrade) {
        this.subjectGrade = subjectGrade;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }


    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "scheduleId=" + scheduleId +
                ", scheduleTime='" + scheduleTime + '\'' +
                ", slotName='" + slotName + '\'' +
                ", scheduleFrom='" + scheduleFrom + '\'' +
                ", scheduleTo='" + scheduleTo + '\'' +
                ", className='" + className + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", subjectGrade='" + subjectGrade + '\'' +
                ", teacherName='" + teacherName + '\'' +
                '}';
    }
}
