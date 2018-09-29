package com.banking.sofware.design.fxtrading.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.stereotype.Service;

import com.banking.sofware.design.fxtrading.pojo.RatePair;

@Service
public class FxProxyRatesService {

  Logger log = LoggerFactory.getLogger(FxProxyRatesService.class);

  @Value("${fxrates.url}")
  private String fxratesUrl;

  public RatePair getRate(String primaryCcy, String secondaryCcy) throws IOException {

    StringBuilder sb = new StringBuilder(fxratesUrl);
    sb = sb.append("?primaryCcy=").append(primaryCcy);
    sb = sb.append("&secondaryCcy=").append(secondaryCcy);
    URL url = new URL(sb.toString());

    BufferedReader streamReader = null;
    HttpURLConnection conn = null;
    try {
      conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Accept", "application/json");

      if (conn.getResponseCode() != 200) {
        log.error("Rates service returned {} status code", conn.getResponseCode());
        throw new RuntimeException("Failed with http error code: " + conn.getResponseCode());
      }

      streamReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      StringBuilder responseStrBuilder = new StringBuilder();
      String inputStr;
      while ((inputStr = streamReader.readLine()) != null) {
        responseStrBuilder.append(inputStr);
      }
      JacksonJsonParser jparse = new JacksonJsonParser();
      Map<String, Object> obj = jparse.parseMap(responseStrBuilder.toString());
      log.info("Received rate object from rates service: {}", obj);

      return new RatePair(BigDecimal.valueOf((Double) obj.get("buyRate")),
              BigDecimal.valueOf((Double) obj.get("sellRate")));
    } finally {
      if (streamReader != null) {
        streamReader.close();
      }
      if (conn != null) {
        conn.disconnect();
      }
    }
  }

}
