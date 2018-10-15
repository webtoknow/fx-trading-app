package com.banking.sofware.design.fxtrading.service;

import java.io.IOException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.banking.sofware.design.fxtrading.pojo.QuoteResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class QuoteProxyService {

  @Value("${fxrates.url}")
  private String fxratesUrl;
  
  @Autowired
  private RemoteServiceCaller proxyService;

  public QuoteResponse getRate(String primaryCcy, String secondaryCcy) throws IOException {

    StringBuilder sb = new StringBuilder(fxratesUrl);
    sb = sb.append("?primaryCcy=").append(primaryCcy);
    sb = sb.append("&secondaryCcy=").append(secondaryCcy);
    URL url = new URL(sb.toString());

    String jsonAsString =  proxyService.doCallServiceGet(url);
    
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(jsonAsString, QuoteResponse.class);
  }

}
