package com.banking.sofware.design.fxtrading.vo;

import java.math.BigDecimal;

public class TransactionVo {

  private BigDecimal id;

  private String username;

  private String primaryCCY;

  private String secondaryCCY;

  private BigDecimal rate;

  private String action;

  private BigDecimal notional;

  private String tenor;

  private Long date;

  public TransactionVo() {

  }

  public BigDecimal getId() {
    return id;
  }

  public void setId(BigDecimal id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPrimaryCCY() {
    return primaryCCY;
  }

  public void setPrimaryCCY(String primaryCCY) {
    this.primaryCCY = primaryCCY;
  }

  public String getSecondaryCCY() {
    return secondaryCCY;
  }

  public void setSecondaryCCY(String secondaryCCY) {
    this.secondaryCCY = secondaryCCY;
  }

  public BigDecimal getRate() {
    return rate;
  }

  public void setRate(BigDecimal rate) {
    this.rate = rate;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public BigDecimal getNotional() {
    return notional;
  }

  public void setNotional(BigDecimal notional) {
    this.notional = notional;
  }

  public String getTenor() {
    return tenor;
  }

  public void setTenor(String tenor) {
    this.tenor = tenor;
  }

  public Long getDate() {
    return date;
  }

  public void setDate(Long date) {
    this.date = date;
  }

}
