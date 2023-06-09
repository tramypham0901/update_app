package com.example.contactbook.model;

public class CheckPassword {
    String username;

    String oldPassword;

    String newPassword;

    public CheckPassword() {
    }

    public CheckPassword(String username, String oldPassword) {
        this.username = username;
        this.oldPassword = oldPassword;
    }

    public CheckPassword(String username, String oldPassword, String newPassword) {
        this.username = username;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
