package pages.enums;

public enum Paths {
    basePath("http://localhost"),
    auth(":8200/user/authenticate"),
    currencies(":8220/currencies"),
    transactions(":8210/transactions");

    private final String value;

    Paths(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
