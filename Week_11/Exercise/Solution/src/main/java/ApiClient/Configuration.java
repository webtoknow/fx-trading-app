package ApiClient;

import okhttp3.OkHttpClient;
public class Configuration {
    private static ApiClient defaultApiClient;

    public static ApiClient getDefaultApiClient(OkHttpClient.Builder builder) {
        defaultApiClient = new ApiClient(builder);
        return defaultApiClient;
    }

    public static void setDefaultApiClient(ApiClient apiClient) {
        defaultApiClient = apiClient;
    }
}
