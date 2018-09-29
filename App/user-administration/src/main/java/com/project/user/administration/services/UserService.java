package com.project.user.administration.services;


import com.project.user.administration.model.User;
import com.project.user.administration.model.UserLogin;
import com.project.user.administration.repository.UserLoginRepository;
import com.project.user.administration.repository.UserRepository;
import com.project.user.administration.vo.UserAuthorizeResponseVo;
import com.project.user.administration.vo.UserTokenResponseVo;
import com.project.user.administration.vo.UserRequestVo;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserLoginRepository userLoginRepository;

    public UserRequestVo findByUserId(Long userId){

        User user = userRepository.findByUserId(userId);

        UserRequestVo uservo  = new UserRequestVo();
        uservo.setUserName(user.getUserName());

        return uservo;
    }

    public void registerNewUser(UserRequestVo userRequestVo) {
        User user = new User();
        user.setUserName(userRequestVo.getUserName());
        user.setPassword(userRequestVo.getPassword());
        user.setEmail(userRequestVo.getEmail());

        userRepository.save(user);
    }

    public UserTokenResponseVo validateUserCredentialsAndGenerateToken(UserRequestVo userRequestVo) {

        User user = userRepository.findUserByStatusAndName(userRequestVo.getUserName(), userRequestVo.getPassword());

        if( user != null) {

            String token=  RandomStringUtils.random(25, true, true);
            UserLogin userLogin= new UserLogin(user, token, getCurrentTimeStamp());
            userLoginRepository.save(userLogin);

            UserTokenResponseVo userTokenResponseVo = new UserTokenResponseVo();
            userTokenResponseVo.setToken(token);
            userTokenResponseVo.setUserName(userRequestVo.getUserName());

            return userTokenResponseVo;
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public UserAuthorizeResponseVo authorize(UserRequestVo userRequestVo) throws ParseException {
        UserLogin userLogin = userLoginRepository.findByUserAndToken(userRequestVo.getUserName(), userRequestVo.getToken());
        if(userLogin != null){
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = format.parse(userLogin.getTokenExpireTime());

            if(new Date().compareTo(date) <1){
                return new UserAuthorizeResponseVo(userRequestVo.getUserName(), true);
            } else {
                return new UserAuthorizeResponseVo(userRequestVo.getUserName(), false);
            }
        }
        return new UserAuthorizeResponseVo(userRequestVo.getUserName(), false);
    }

    public String getCurrentTimeStamp() {
        Date newDate = DateUtils.addHours(new Date(), 3);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(newDate);
    }
}
