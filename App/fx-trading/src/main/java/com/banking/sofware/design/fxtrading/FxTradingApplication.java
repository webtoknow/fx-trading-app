package com.banking.sofware.design.fxtrading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class FxTradingApplication {

  public static void main(String[] args) {
    SpringApplication.run(FxTradingApplication.class, args);
  }
}
