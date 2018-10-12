package com.banking.sofware.design.fxtrading.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RemoteServiceCaller {

  private static final Logger log = LoggerFactory.getLogger(RemoteServiceCaller.class);

  public String doCallServiceGet(URL url) throws IOException {
    return doCallService(url, "GET", null);
  }

  public String doCallServicePost(URL url, String postBody) throws IOException {
    return doCallService(url, "POST", postBody);
  }

  private HttpURLConnection makeRequest(URL url, String method, String postBody) throws IOException {
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestProperty("Accept", "application/json");

    if ("GET".equals(method)) {
      conn.setRequestMethod("GET");
    } else {
      conn.setDoOutput(true);
      conn.setRequestMethod("POST");
      conn.setRequestProperty("content-type", "application/json");

      BufferedWriter streamWriter = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
      streamWriter.write(postBody);
      streamWriter.flush();
      streamWriter.close();
    }
    return conn;
  }

  private String doCallService(URL url, String method, String postBody) throws IOException {
    BufferedReader streamReader = null;
    HttpURLConnection conn = null;
    try {
      conn = makeRequest(url, method, postBody);

      if (conn.getResponseCode() != 200) {
        log.error("Call to URL {} method {} resulted in {} status code", url, method, conn.getResponseCode());
        throw new RuntimeException("Failed with http error code: " + conn.getResponseCode());
      }

      streamReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      StringBuilder stringBuilder = new StringBuilder();
      String inputStr;
      while ((inputStr = streamReader.readLine()) != null) {
        stringBuilder.append(inputStr);
      }

      String result = stringBuilder.toString();
      log.info("Call to URL {} method {} was 200 OK and returned body: {}", url, method, result);
      return result;
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
