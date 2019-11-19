package com.banking.sofware.design.fxtrading.service;

import com.banking.sofware.design.fxtrading.pojo.QuoteResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class QuoteProxyService {

    @Value("${fxrates.url}")
    private String fxratesUrl;

    public QuoteResponse getRate(String primaryCcy, String secondaryCcy) {

        RestTemplate restTemplate = new RestTemplate();

        StringBuilder sb = new StringBuilder(fxratesUrl);
        sb = sb.append("?primaryCcy=").append(primaryCcy);
        sb = sb.append("&secondaryCcy=").append(secondaryCcy);

        return restTemplate.getForObject(sb.toString(), QuoteResponse.class);
    }

}
