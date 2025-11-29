package com.fx.qa.automation.enums;

public enum Tenor {
    SP("SP"),
    ONE_M("1M"),
    THREE_M("3M");


    private final String value;

    public String getValue(){
        return value;
    }
    Tenor(String value) {
        this.value = value;
    }
}
