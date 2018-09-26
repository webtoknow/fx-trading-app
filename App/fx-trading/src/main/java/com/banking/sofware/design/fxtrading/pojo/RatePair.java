package com.banking.sofware.design.fxtrading.pojo;

import java.math.BigDecimal;

public class RatePair {

	private BigDecimal buy;
	private BigDecimal sell;
	
	public RatePair(BigDecimal buy, BigDecimal sell) {
		this.buy = buy;
		this.sell = sell;
	}
	public BigDecimal getBuy() {
		return buy;
	}
	public void setBuy(BigDecimal buy) {
		this.buy = buy;
	}
	public BigDecimal getSell() {
		return sell;
	}
	public void setSell(BigDecimal sell) {
		this.sell = sell;
	}
	
}
