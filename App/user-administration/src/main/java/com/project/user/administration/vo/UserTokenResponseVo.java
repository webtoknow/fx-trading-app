package com.project.user.administration.vo;

public class UserTokenResponseVo {

    private String username;
    private String token;

    public UserTokenResponseVo(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
