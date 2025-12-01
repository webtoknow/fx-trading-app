package com.fx.qa.automation.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

@Slf4j
public class RetryAnalyzer implements IRetryAnalyzer {
    private static int MAX_ATTEMPTS;
    private static int attempts = 0;

    static {
        try {
            ApplicationContext context =
                    SpringContextProvider.getApplicationContext();
            String value = context.getEnvironment().getProperty("application.failed-reattempts", "1");
            MAX_ATTEMPTS = Integer.parseInt(value);
        } catch (Exception e) {
            MAX_ATTEMPTS = 1;
        }
    }


    @Override
    public boolean retry(ITestResult iTestResult) {
        if (!iTestResult.isSuccess()) {
            if (attempts < MAX_ATTEMPTS) {
                attempts++;

                return true;
            }
        } else {
            iTestResult.setStatus(ITestResult.SUCCESS);
        }

        log.error("Test was re-executed {} times, but it still failed.", MAX_ATTEMPTS);
        return false;
    }
}
