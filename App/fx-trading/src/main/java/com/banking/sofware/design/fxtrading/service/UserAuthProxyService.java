package com.banking.sofware.design.fxtrading.service;

import com.banking.sofware.design.fxtrading.pojo.AuthRequest;
import com.banking.sofware.design.fxtrading.pojo.AuthResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserAuthProxyService {

    @Value("${user.auth.url}")
    private String userAuthorization;


    public AuthResponse authorizeUser(String token) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(userAuthorization, new AuthRequest(token), AuthResponse.class);
    }

}
