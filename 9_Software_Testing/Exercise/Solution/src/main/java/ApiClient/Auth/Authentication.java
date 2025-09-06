package ApiClient.Auth;

import ApiClient.Pair;

import java.util.List;
import java.util.Map;

public interface Authentication {

    void applyToParams(List<Pair> queryParams, Map<String, String> headerParams);
}
