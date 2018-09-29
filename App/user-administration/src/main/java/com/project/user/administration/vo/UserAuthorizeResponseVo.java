package com.project.user.administration.vo;

public class UserAuthorizeResponseVo {

    private String userName;
    private boolean isValid;

    public UserAuthorizeResponseVo(String userName, boolean isValid){
        this.userName = userName;
        this.isValid = isValid;
    }

    public UserAuthorizeResponseVo(){
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
