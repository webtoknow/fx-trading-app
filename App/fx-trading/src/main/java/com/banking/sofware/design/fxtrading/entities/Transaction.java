package com.banking.sofware.design.fxtrading.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="Transactions")
public class Transaction {

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="transactionid")
    BigDecimal transactionId;
	
	@Column(name="username")
	String username;
	
	@Column(name="primaryccy")
	String primaryCcy;
	
	@Column(name="secondaryccy")
	String secondaryCcy;
	
	@Column(name="rate")
	BigDecimal rate; 
	
	@Column(name="action")
	String action;
	
	@Column(name="notional")
	BigDecimal notional; 
	
	@Column(name="tenor")
	String tenor;
	
	@Column(name="date")
	Date date;

	public BigDecimal getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(BigDecimal transactionId) {
		this.transactionId = transactionId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPrimaryCcy() {
		return primaryCcy;
	}

	public void setPrimaryCcy(String primaryCcy) {
		this.primaryCcy = primaryCcy;
	}

	public String getSecondaryCcy() {
		return secondaryCcy;
	}

	public void setSecondaryCcy(String secondaryCcy) {
		this.secondaryCcy = secondaryCcy;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
	
}
