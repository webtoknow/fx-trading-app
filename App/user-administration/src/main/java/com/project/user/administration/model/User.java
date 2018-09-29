package com.project.user.administration.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_table")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false, nullable = false)
    private Long userId;

    @Column(columnDefinition = "user_name")
    private String userName;

    @Column(columnDefinition = "last_name")
    private String lastName;

    @Column(columnDefinition = "first_name")
    private String firstName;


    @OneToMany(mappedBy = "user")
    private List<UserLogin> logins = new ArrayList<>();

    public User(){
    }

    public User(String userName,String firstName, String lastName ){
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public List<UserLogin> getLogins() {
        return logins;
    }

    public void setLogins(List<UserLogin> logins) {
        this.logins = logins;
    }
}
