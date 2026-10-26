package com.banking.sofware.design.fx_trading.configuration;

import com.banking.sofware.design.fx_trading.filter.JwtAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@Profile("!dev")
public class CustomWebSecurityConfigurerAdapter {

    private final AuthenticationConfiguration authenticationConfiguration;

    public CustomWebSecurityConfigurerAdapter(AuthenticationConfiguration configuration) {
        this.authenticationConfiguration = configuration;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/transactions/**").permitAll()
                        // .requestMatchers("/auth/**").permitAll() // typically login/register endpoints
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthorizationFilter(authenticationManager()), BasicAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}