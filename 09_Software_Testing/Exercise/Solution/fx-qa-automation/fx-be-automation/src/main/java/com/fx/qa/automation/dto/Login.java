package com.fx.qa.automation.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Login {
    private String username;
    private String password;
    private String token;
}
