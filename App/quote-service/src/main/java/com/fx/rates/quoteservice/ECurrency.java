package com.fx.rates.quoteservice;

import java.util.HashMap;

public enum ECurrency {
	
	
	EUR("EUR"),
	USD("USD"),
	GBP("GBP"),
	RON("RON");
	
    private static final HashMap<String, ECurrency> MAP = new HashMap<String, ECurrency>();

    static {
		for (ECurrency currency : ECurrency.values()) {
			MAP.put(currency.getLabel(), currency);
		}
	} 
    
	private String label;

	ECurrency(String label){
		this.label = label;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public static ECurrency getByLabel(String label) {
		return MAP.get(label);
	}

	
}
