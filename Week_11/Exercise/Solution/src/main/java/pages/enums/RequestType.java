package pages.enums;

public enum RequestType {

    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    UPDATE("UPDATE"),
    DELETE("DELETE");

    private final String value;

    RequestType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
