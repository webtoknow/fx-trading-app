package com.project.user.administration.vo;

public class UserResponseVo {

    private String userName;
    private boolean isValid;

    public UserResponseVo(String userName, boolean isValid){
        this.userName = userName;
        this.isValid = isValid;
    }

    public UserResponseVo(){
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
