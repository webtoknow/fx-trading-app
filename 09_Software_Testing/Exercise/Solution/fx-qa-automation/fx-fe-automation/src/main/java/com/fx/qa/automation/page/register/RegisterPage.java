package com.fx.qa.automation.page.register;

import com.fx.qa.automation.driver.annotations.LazyAutowired;
import com.fx.qa.automation.driver.annotations.Page;
import com.fx.qa.automation.page.Base;
import com.fx.qa.automation.page.register.components.RegisterFormFragment;
import lombok.Getter;

@Page
@Getter
public class RegisterPage extends Base {

    @LazyAutowired
    private RegisterFormFragment registerFormFragment;
}
