package com.fx.rates.quote_service;

import java.util.HashMap;

public enum Currency {
	
	
	EUR("EUR"),
	USD("USD"),
	GBP("GBP"),
	RON("RON");
	
    private static final HashMap<String, Currency> MAP = new HashMap<String, Currency>();

    static {
		for (Currency currency : Currency.values()) {
			MAP.put(currency.getLabel(), currency);
		}
	} 
    
	private String label;

	Currency(String label){
		this.label = label;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public static Currency getByLabel(String label) {
		return MAP.get(label);
	}

	
}
