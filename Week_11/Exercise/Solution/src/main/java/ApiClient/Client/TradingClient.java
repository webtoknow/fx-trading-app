package ApiClient.Client;

import ApiClient.*;
import Models.LoginModel;
import Models.TransactionsModel;
import com.google.gson.reflect.TypeToken;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import pages.enums.Paths;
import pages.enums.RequestType;
import utilities.ConfigurationData;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TradingClient {
    private final ApiClient apiClient;
    private final static OkHttpClient.Builder builder = null;
    private String token;

    public TradingClient() {
        this.apiClient = Configuration.getDefaultApiClient(builder);
        this.apiClient.setBasePath(Paths.basePath.getValue());
        login();
    }

    /**
     * method used to get all transactions
     * @return
     * @throws ApiException
     */
    public ApiResponse<List<TransactionsModel>> getTransactions() throws ApiException  {
        final String localVarPath = Paths.transactions.getValue();
        Type localVarReturnType = new TypeToken<List<TransactionsModel>>() { }.getType();

        return execute(localVarReturnType, localVarPath, RequestType.GET, null, null);
    }

    public ApiResponse<String> postTransaction(TransactionsModel model) throws ApiException {
        final String localVarPath = Paths.transactions.getValue();
        Type localVarReturnType = new TypeToken<String>() { }.getType();

        return execute(localVarReturnType, localVarPath, RequestType.POST, null, model);
    }

    /**
     * Method used to get all currencies
     * @return ApiResponse<List<String>>
     * @throws ApiException
     */
    public ApiResponse<List<String>> getCurrencies() throws ApiException {
        final String localVarPath = Paths.currencies.getValue();
        Type localVarReturnType = new TypeToken<List<String>>() { }.getType();

        return execute(localVarReturnType, localVarPath, RequestType.GET, null, null);
    }

    /**
     * Method is for login
     */
    private void login() {
        final String localVarPath = Paths.auth.getValue();
        final String username = ConfigurationData.getUsername();
        final String password = ConfigurationData.getPassword();

        Type localVarReturnType = new TypeToken<LoginModel>() { }.getType();

        LoginModel login = new LoginModel(username, password);

        try {
            ApiResponse<LoginModel> response = execute(localVarReturnType, localVarPath, RequestType.POST, null, login);
            this.token = response.getData().getToken();
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method used to execute the API calls
     * @param localVarReturnType
     * @param localVarPath
     * @param method
     * @param localVarQueryParams
     * @param body
     * @return
     * @param <T>
     * @param <S>
     * @throws ApiException
     */
    private <T, S> ApiResponse<T> execute(Type localVarReturnType, String localVarPath, RequestType method, List<Pair> localVarQueryParams, S body) throws ApiException {
        List<Pair> localVarCollectionQueryParams = new ArrayList<>();

        Map<String, String> localVarHeaderParams = new HashMap<>();

        Map<String, Object> localVarFormParams = new HashMap<>();

        final String[] localVarAccepts = { "*/*" };

        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

        if (localVarAccept != null)
            localVarHeaderParams.put("Accept", localVarAccept);

        if (token != null && !token.isEmpty()) {
            localVarHeaderParams.put("Authorization", "Bearer " + token);
        }

        final String[] localVarContentTypes = { };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);


        String[] localVarAuthNames = new String[] { };
        Call call = apiClient.buildCall(localVarPath, method.getValue(), localVarQueryParams, localVarCollectionQueryParams, body, localVarHeaderParams, localVarFormParams, localVarAuthNames, null);

        return apiClient.execute(call, localVarReturnType);
    }
}
