package com.fx.qa.automation.listeners;

import com.fx.qa.automation.driver.annotations.LazyAutowired;
import org.junit.jupiter.api.Order;
import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.util.UUID;

@Configuration
@Order((Ordered.HIGHEST_PRECEDENCE))
public class TestNGCustomListener implements ITestListener {

    @LazyAutowired
    ApplicationContext ctx;

    @Override
    public void onTestStart(ITestResult iTestResult) {
        String testName = iTestResult.getTestClass().getName() + "." + iTestResult.getName();
        String threadId = Thread.currentThread().getName();
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);

        MDC.put("testName", testName);
        MDC.put("threadId", threadId);
        MDC.put("uniqueId", uniqueId);
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) { MDC.clear();}

    @Override
    public void onTestFailure(ITestResult iTestResult) { MDC.clear();}

    @Override
    public void onTestSkipped(ITestResult iTestResult) { MDC.clear();}

    @Override
    public void onStart(ITestContext context) {
        new File("target/test-logs").mkdir();
    }

    @Override
    public void onFinish(ITestContext context) {
        MDC.clear();
    }
}
