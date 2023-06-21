package com.mobile.uko.model;

public class ChangePassword {
    private String newPassword;
    private String oldPassword;

    public ChangePassword(String newPass, String oldPass) {
        this.newPassword = newPass;
        this.oldPassword = oldPass;
    }
}
