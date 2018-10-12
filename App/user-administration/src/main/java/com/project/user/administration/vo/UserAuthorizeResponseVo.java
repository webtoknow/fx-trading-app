package com.project.user.administration.vo;

public class UserAuthorizeResponseVo {

    private String username;
    private boolean isValid;

    public UserAuthorizeResponseVo(String username, boolean isValid){
        this.username = username;
        this.isValid = isValid;
    }

    public UserAuthorizeResponseVo(){
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
