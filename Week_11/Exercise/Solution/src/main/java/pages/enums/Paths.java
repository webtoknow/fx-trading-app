package pages.enums;

public enum Paths {
    basePath("http://localhost"),
    auth("/user/authenticate"),
    currencies("/currencies"),
    transactions("/transactions");

    private final String value;

    Paths(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
