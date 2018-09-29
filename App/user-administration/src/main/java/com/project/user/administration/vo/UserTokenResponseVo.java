package com.project.user.administration.vo;

public class UserTokenResponseVo {

    private String userName;
    private String token;

    public UserTokenResponseVo(){

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
