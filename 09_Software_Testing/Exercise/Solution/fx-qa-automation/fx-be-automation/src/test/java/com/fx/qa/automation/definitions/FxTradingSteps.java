package com.fx.qa.automation.definitions;

import com.fx.qa.automation.Utils.Utils;
import com.fx.qa.automation.client.ApiClient;
import com.fx.qa.automation.cucumber.configuration.TestConfig;
import com.fx.qa.automation.dto.Register;
import com.fx.qa.automation.dto.Transactions;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.testng.Assert;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@SpringBootTest(classes = TestConfig.class)
public class FxTradingSteps {
    private final ApiClient client;
    private Register register;
    private int responseCode;
    private ResponseEntity<List<String>> actualCurrencies;
    private static final List<String> EXPECTED_CURRENCIES = Arrays.asList("EUR","USD","GBP","RON");
    private Transactions transactionsBody;

    @Given("I create the register body with random data")
    public void iCreateTheRegisterBodyWithRandomData() {
        final String USERNAME = "QAA_" + Utils.getRandomCharacterString(5);
        final String PASSWORD = Utils.getRandomCharacterString(8);
        final String EMAIL = USERNAME + "@test.com";

        Register body = Register.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .confirmPassword(PASSWORD)
                .email(EMAIL)
                .build();

        log.info("Register body: {}", body);
        register = body;
    }

    @When("I send the request with a random username and password")
    public void iSendTheRequestWithARandomUsernameAndPassword() {
        responseCode = client.createUser(register).statusCode().value();
    }


    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int statusCode) {
        Assert.assertEquals(statusCode, responseCode, "Status code did not match");
    }

    @Given("the website is running")
    public void theWebsiteIsRunning() {
        int statusCode = client.healthCheck();
        Assert.assertTrue(statusCode >= 200 && statusCode < 300,
                "API is not reachable. Status code: " + statusCode);
    }

    @When("I call the currencies endpoint")
    public void iCallTheCurrenciesEndpoint() {
        actualCurrencies = client.getCurrencies();
        responseCode = actualCurrencies.getStatusCode().value();
    }

    @And("the returned currencies should match the expected list")
    public void theReturnedCurrenciesShouldMatchTheExpectedList() {
        assert actualCurrencies.getBody() != null;
        Assert.assertTrue(
                actualCurrencies.getBody().containsAll(EXPECTED_CURRENCIES)
                        && EXPECTED_CURRENCIES.containsAll(actualCurrencies.getBody())
        );
    }

    @When("I call transactions endpoint")
    public void iCallTransactionsEndpoint() {
        long lowerBound = 1000;
        long upperBound = 234543;
        transactionsBody = Transactions.builder()
                .username("QAA_" + Utils.getRandomCharacterString(5))
                .primaryCcy("EUR")
                .secondaryCcy("USD")
                .action("SELL")
                .notional(RandomUtils.nextLong(lowerBound, upperBound))
                .tenor("SP")
                .build();

        responseCode = client.postTransaction(transactionsBody).statusCode().value();
    }

    @And("the list of transactions should include the previous transaction")
    public void theListOfTransactionsShouldIncludeThePreviousTransaction() throws InterruptedException {
        ResponseEntity<List<Transactions>> transactionsList = client.getTransactions();

        assert transactionsList.getBody() != null;
        Transactions transaction = transactionsList.getBody().getLast();
        Assert.assertTrue(transaction.getRate() != null && transaction.getRate() > 0);
        Assert.assertEquals(transaction.getNotional(), transactionsBody.getNotional());
        Assert.assertEquals(transaction.getPrimaryCcy(), transactionsBody.getPrimaryCcy());
        Assert.assertEquals(transaction.getSecondaryCcy(), transactionsBody.getSecondaryCcy());
        Assert.assertNotNull(transaction.getId());
    }
}
