package com.jkrm.fupin.bean.request;

/**
 * Created by hzw on 2018/8/13.
 */

public class LoginRequestBean {

    private String username;
    private String password;

    public LoginRequestBean() {
    }

    public LoginRequestBean(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
