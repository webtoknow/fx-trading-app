package com.fx.rates.quoteservice;

import java.util.Date;

public class RateVO {
	
	private Float buyRate;
	private Float sellRate;
	private Date ts;
	
	
	public RateVO() {
		
	}
	
	
	RateVO(Float buyRate, Float sellRate , Date ts){
		this.buyRate = buyRate;
		this.sellRate = sellRate;
		this.ts = ts;
	}
	
	public Float getBuyRate() {
		return buyRate;
	}
	
	public Float getSellRate() {
		return sellRate;
	}
	
	
	public void setBuyRate (Float buyRate) {
		this.buyRate = buyRate;
	}
	
	public void setTs (Date ts) {
		this.ts = ts;
	}
	
	public Date getTs() {
		return ts;
	}
	
}