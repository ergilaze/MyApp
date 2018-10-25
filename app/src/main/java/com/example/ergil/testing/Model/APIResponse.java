package com.example.ergil.testing.Model;

public class APIResponse {
    private String __invalid_name__0;
    private boolean error;
    private String uid;
    private User user;
    private String error_msg;

    public APIResponse() {

    }

    public String get__invalid_name__0() {
        return __invalid_name__0;
    }

    public void set__invalid_name__0(String __invalid_name__0) {
        this.__invalid_name__0 = __invalid_name__0;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }
}
