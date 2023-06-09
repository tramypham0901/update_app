package com.example.contactbook.model;

import java.util.List;

public class User {
    private String userCode;

    private String status;

    private String firstName;

    private String lastName;

    private String username;

    private String dob;

    private String joinedDate;

    private String gender;

    private List<String> roleName;

    private String address;

    private String studentClass;

    private String phoneNumber;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(String joinedDate) {
        this.joinedDate = joinedDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<String> getRoleName() {
        return roleName;
    }

    public void setRoleName(List<String> roleName) {
        this.roleName = roleName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "User{" +
                "userCode='" + userCode + '\'' +
                ", status='" + status + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", dob='" + dob + '\'' +
                ", joinedDate='" + joinedDate + '\'' +
                ", gender='" + gender + '\'' +
                ", roleName=" + roleName +
                ", address='" + address + '\'' +
                ", studentClass='" + studentClass + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
