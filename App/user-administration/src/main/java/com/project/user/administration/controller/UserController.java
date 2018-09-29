package com.project.user.administration.controller;

import com.project.user.administration.services.UserService;
import com.project.user.administration.vo.UserResponseVo;
import com.project.user.administration.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/{userId}")
    public UserVo getAnswersByQuestionId(@PathVariable Long userId) {
        return userService.findByUserId(userId);
    }

    @PostMapping("/user/register")
    public void registerNewUser(@RequestBody UserVo userVo) {
        userService.registerNewUser(userVo);
    }

    @PostMapping("/user/login")
    public String login(@RequestBody UserVo userVo) {
        return userService.validateUserCredentialsAndGenerateToken(userVo);
    }

    @PostMapping("/user/authorize")
    public UserResponseVo authorize(@RequestBody UserVo userVo) throws ParseException {
        return userService.authorize(userVo);
    }

}
