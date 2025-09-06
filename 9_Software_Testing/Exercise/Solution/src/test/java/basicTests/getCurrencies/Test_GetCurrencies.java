package basicTests.getCurrencies;

import ApiClient.ApiException;
import ApiClient.ApiResponse;
import ApiClient.Client.TradingClient;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class Test_GetCurrencies {

    @Test(description = "This test checks the list of currencies")
    private static void getAllCurrencies() {
        final List<String> expectedCurrencies = Arrays.asList("EUR","USD","GBP","RON");
        TradingClient client = new TradingClient();
        try {
            ApiResponse<List<String>> response = client.getCurrencies();

            Assert.assertEquals(response.getStatusCode(), 200);
            Assert.assertEquals(expectedCurrencies.size(), response.getData().size());
            Assert.assertEquals(expectedCurrencies, response.getData());
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }
}
