package com.project.user.administration.vo;

public class UserVo {

    private String userName;
    private String lastName;
    private String firstName;
    private String token;

    public UserVo(String userName,String firstName, String lastName, String token ){
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.token = token;
    }

    public UserVo(String userName,String firstName, String lastName ){
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UserVo(){
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
