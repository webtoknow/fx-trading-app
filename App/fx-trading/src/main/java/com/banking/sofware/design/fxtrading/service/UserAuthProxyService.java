package com.banking.sofware.design.fxtrading.service;

import java.io.IOException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.banking.sofware.design.fxtrading.pojo.AuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserAuthProxyService {

  @Value("${user.auth.url}")
  private String userAuthorization;

  @Autowired
  private RemoteServiceCaller proxyService;

  private static final Logger log = LoggerFactory.getLogger(UserAuthProxyService.class);

  public AuthResponse authorizeUser(String token) throws IOException {
    try {
      String postBody = String.format("{\"token\": \"%s\"}", token);

      String jsonAsString = proxyService.doCallServicePost(new URL(userAuthorization), postBody);

      ObjectMapper mapper = new ObjectMapper();
      return mapper.readValue(jsonAsString, AuthResponse.class);
    } catch (IOException e) {
      log.error("Error while calling authorization service", e);
      throw new RuntimeException("Error while calling authorization service");
    }
  }

}
