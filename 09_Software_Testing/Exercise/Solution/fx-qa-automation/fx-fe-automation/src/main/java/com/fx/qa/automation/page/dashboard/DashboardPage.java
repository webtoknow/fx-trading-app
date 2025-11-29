package com.fx.qa.automation.page.dashboard;

import com.fx.qa.automation.driver.annotations.LazyAutowired;
import com.fx.qa.automation.driver.annotations.Page;
import com.fx.qa.automation.page.Base;
import com.fx.qa.automation.page.dashboard.components.CalendarModelFragment;
import com.fx.qa.automation.page.dashboard.components.DashBoardFormFragment;
import com.fx.qa.automation.page.dashboard.components.DashBoardTransactionFragment;
import lombok.Getter;

@Page
@Getter
public class DashboardPage extends Base {

    @LazyAutowired
    private DashBoardFormFragment dashBoardFormFragment;

    @LazyAutowired
    private DashBoardTransactionFragment dashBoardTransactionFragment;

    @LazyAutowired
    private CalendarModelFragment calendarModelFragment;
}
