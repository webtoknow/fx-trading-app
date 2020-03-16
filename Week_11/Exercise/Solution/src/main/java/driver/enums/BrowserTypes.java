package driver.enums;

public enum BrowserTypes {
    CHROME("chromedriver.exe"),
    IE("IEDriverServer.exe");

    private String a;

    BrowserTypes(String a) {
        this.a = a;
    }

    public String getFileName() {
        return a;
    }
}
