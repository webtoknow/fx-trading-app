package com.fx.qa.automation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.fx.qa.automation"})
public class FxFeAutomationApplication {
    public static void main(String[] args) {
        SpringApplication.run(FxFeAutomationApplication.class, args);
    }
}
