package com.banking.sofware.design.fxtrading.configuration;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.banking.sofware.design.fxtrading.pojo.AuthResponse;
import com.banking.sofware.design.fxtrading.service.UserAuthProxyService;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

  private static final String HEADER = "Authorization";
  public static final String TOKEN_PREFIX = "Bearer ";

  public JwtAuthorizationFilter(AuthenticationManager authManager) {
    super(authManager);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
          throws IOException, ServletException {
    String header = req.getHeader(HEADER);

    if(HttpMethod.OPTIONS.equals(req.getMethod())) {
      chain.doFilter(req, res);
      return;
    }

    if (header == null || !header.startsWith(TOKEN_PREFIX)) {
      res.setStatus(401);
      return;
    }

    try {
      UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
      SecurityContextHolder.getContext().setAuthentication(authentication);
      chain.doFilter(req, res);
    } catch (Exception e) {
      res.setStatus(401);
      return;
    }
  }

  private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
    String token = request.getHeader(HEADER);
    if (token != null) {
      String parsedToken = token.replace(TOKEN_PREFIX, "");
      try {
        WebApplicationContext webApplicationContext = WebApplicationContextUtils
                .getWebApplicationContext(request.getServletContext());
        UserAuthProxyService authorizationService = webApplicationContext.getBean(UserAuthProxyService.class);
        AuthResponse response = authorizationService.authorizeUser(parsedToken);
        if (response.isValid()) {
          return new UsernamePasswordAuthenticationToken(response.getUserName(), null, new ArrayList<>());
        }
      } catch (Exception e) {
        throw new RuntimeException("Authorization failed");
      }
    }
    return null;
  }
}