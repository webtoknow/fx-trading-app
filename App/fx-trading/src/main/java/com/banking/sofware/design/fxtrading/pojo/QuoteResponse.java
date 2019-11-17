package com.banking.sofware.design.fxtrading.pojo;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteResponse {

  private BigDecimal buyRate;
  private BigDecimal sellRate;

  public QuoteResponse() {

  }

  public QuoteResponse(BigDecimal buyRate, BigDecimal sellRate) {
    this.buyRate = buyRate;
    this.sellRate = sellRate;
  }

  public BigDecimal getBuyRate() {
    return buyRate;
  }

  public void setBuyRate(BigDecimal buyRate) {
    this.buyRate = buyRate;
  }

  public BigDecimal getSellRate() {
    return sellRate;
  }

  public void setSellRate(BigDecimal sellRate) {
    this.sellRate = sellRate;
  }

}
