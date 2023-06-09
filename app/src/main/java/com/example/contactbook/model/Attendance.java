package com.example.contactbook.model;

public class Attendance {
    private long attendId;

    private String userCode;

    private String username;

    private String fullName;

    private long scheduleId;

    private String scheduleTime;

    private String className;

    private String checkBy;

    private String isAttended;

    public long getAttendId() {
        return attendId;
    }

    public void setAttendId(long attendId) {
        this.attendId = attendId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCheckBy() {
        return checkBy;
    }

    public void setCheckBy(String checkBy) {
        this.checkBy = checkBy;
    }

    public String getIsAttended() {
        return isAttended;
    }

    public void setIsAttended(String isAttended) {
        this.isAttended = isAttended;
    }
}
