package com.fx.qa.automation.page.login;

import com.fx.qa.automation.driver.annotations.LazyAutowired;
import com.fx.qa.automation.driver.annotations.Page;
import com.fx.qa.automation.page.Base;
import com.fx.qa.automation.page.login.components.LoginFormFragment;
import lombok.Getter;

@Page
@Getter
public class LoginPage extends Base {

    @LazyAutowired
    private LoginFormFragment loginFormFragment;

    public void initialize() {
        getDriver().get(getApplicationUrl());
    }
}
