package com.fx.qa.automation.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Register {
    private String username;
    private String password;
    private String confirmPassword;
    private String email;
}
