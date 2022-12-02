package ApiClient.Auth;

import ApiClient.Pair;

import java.util.List;
import java.util.Map;

public class CookieAuth implements Authentication {
    private String cookie;

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    @Override
    public void applyToParams(List<Pair> queryParams, Map<String, String> headerParams) {
        if (cookie != null) {
            headerParams.put("Cookie", cookie);
        }
    }
}
