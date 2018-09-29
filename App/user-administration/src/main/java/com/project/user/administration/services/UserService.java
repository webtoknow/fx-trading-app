package com.project.user.administration.services;


import com.project.user.administration.model.User;
import com.project.user.administration.model.UserLogin;
import com.project.user.administration.repository.UserLoginRepository;
import com.project.user.administration.repository.UserRepository;
import com.project.user.administration.vo.UserResponseVo;
import com.project.user.administration.vo.UserVo;
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

    public UserVo findByUserId(Long userId){

        User user = userRepository.findByUserId(userId);
        return new UserVo(user.getUserName(),user.getFirstName(), user.getLastName() );
    }

    public void registerNewUser(UserVo userVo) {
        userRepository.save(new User(userVo.getUserName(),userVo.getFirstName(), userVo.getLastName() ));
    }

    public String validateUserCredentialsAndGenerateToken(UserVo userVo) {

        User user = userRepository.findUserByStatusAndName(userVo.getUserName());

        if( user != null) {

            String token=  RandomStringUtils.random(25, true, true);
            UserLogin userLogin= new UserLogin(user, token, getCurrentTimeStamp());
            userLoginRepository.save(userLogin);

            return token;
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public UserResponseVo authorize(UserVo userVo) throws ParseException {
        UserLogin userLogin = userLoginRepository.findByUserAndToken(userVo.getUserName(), userVo.getToken());
        if(userLogin != null){
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = format.parse(userLogin.getTokenExpireTime());

            if(new Date().compareTo(date) <1){
                return new UserResponseVo(userVo.getUserName(), true);
            } else {
                return new UserResponseVo(userVo.getUserName(), false);
            }
        }
        return new UserResponseVo(userVo.getUserName(), false);
    }

    public String getCurrentTimeStamp() {
        Date newDate = DateUtils.addHours(new Date(), 3);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(newDate);
    }
}
