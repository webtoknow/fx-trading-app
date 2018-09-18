package com.fx.rates.quoteservice;

import java.util.Date;

public class RateVO {
	
	private Float rate;
	private Date ts;
	
	
	public RateVO() {
		
	}
	
	
	RateVO(Float rate , Date ts){
		this.rate = rate;
		this.ts = ts;
	}
	
	public Float getRate() {
		return rate;
	}
	
	public Date getTs() {
		return ts;
	}
	
	public void setRate (Float rate) {
		this.rate = rate;
	}
	
	public void setTs (Date ts) {
		this.ts = ts;
	}
}