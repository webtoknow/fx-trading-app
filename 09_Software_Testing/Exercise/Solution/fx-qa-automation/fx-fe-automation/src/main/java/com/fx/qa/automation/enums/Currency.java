package com.fx.qa.automation.enums;

public enum Currency {
    EUR("EUR"),
    USD("USD"),
    GBP("GBP"),
    RON("RON");

    private final String value;

    public String getValue(){
        return value;
    }
    Currency(String value) {
        this.value = value;
    }
}
