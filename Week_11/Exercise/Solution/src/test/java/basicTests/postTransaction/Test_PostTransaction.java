package basicTests.postTransaction;

import ApiClient.ApiException;
import ApiClient.ApiResponse;
import ApiClient.Client.TradingClient;
import Models.TransactionsModel;
import com.google.common.collect.Iterables;
import org.apache.commons.lang3.RandomUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.ConfigurationData;

import java.util.List;

public class Test_PostTransaction {

    @Test(description = "This test checks the transactions APIs")
    private static void Transactions() throws ApiException {
        long lowerBound = 1000;
        long upperBound = 234543;
        final String username = ConfigurationData.getUsername();
        final String primaryCcy = "EUR";
        final String secondaryCcy = "USD";
        final String action = "SELL";
        final long notional = RandomUtils.nextLong(lowerBound, upperBound);;
        final String tenor = "SP";

        TransactionsModel model = new TransactionsModel();
        model.setUsername(username);
        model.setPrimaryCcy(primaryCcy);
        model.setSecondaryCcy(secondaryCcy);
        model.setAction(action);
        model.setNotional(notional);
        model.setTenor(tenor);

        TradingClient client = new TradingClient();
        ApiResponse<String> response = client.postTransaction(model);
        Assert.assertEquals(response.getStatusCode(), 200);

        ApiResponse<List<TransactionsModel>> allTransactions = client.getTransactions();
        Assert.assertEquals(allTransactions.getStatusCode(), 200);

        TransactionsModel transaction = Iterables.getLast(allTransactions.getData(), null);
        Assert.assertTrue(transaction.getRate() != null && transaction.getRate() > 0);
        Assert.assertEquals(transaction.getNotional(), notional);
        Assert.assertNotNull(transaction.getId());
    }
}
