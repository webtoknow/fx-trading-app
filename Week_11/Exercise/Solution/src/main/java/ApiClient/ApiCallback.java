package ApiClient;

import java.util.List;
import java.util.Map;

public interface ApiCallback<T> {

    void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders);

    void onSuccess(T result, int statusCode, Map<String, List<String>> responseHeaders);

    void onUploadProgress(long bytesWritten, long contentLength, boolean done);

    void onDownloadProgress(long bytesRead, long contentLength, boolean done);
}
