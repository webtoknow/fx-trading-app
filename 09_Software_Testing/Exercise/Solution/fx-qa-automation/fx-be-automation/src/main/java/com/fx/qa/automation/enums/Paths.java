package com.fx.qa.automation.enums;

public enum Paths {
    AUTH("/auth/user/authenticate"),
    CURRENCIES("/quote/currencies"),
    TRANSACTIONS("/trade/transactions"),
    REGISTER("/auth/user/register");

    private final String value;

    Paths(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
