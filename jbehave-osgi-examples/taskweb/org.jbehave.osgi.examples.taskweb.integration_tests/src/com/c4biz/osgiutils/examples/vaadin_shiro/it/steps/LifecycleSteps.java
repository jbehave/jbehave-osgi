package com.c4biz.osgiutils.examples.vaadin_shiro.it.steps;

import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.web.selenium.PerStoryWebDriverSteps;
import org.jbehave.web.selenium.WebDriverProvider;
import org.openqa.selenium.WebDriverException;

public class LifecycleSteps extends PerStoryWebDriverSteps {

    public LifecycleSteps(WebDriverProvider webDriverProvider) {
        super(webDriverProvider);
    }

    @BeforeScenario
    public void emptyCart() {
        try {
            driverProvider.get().manage().deleteCookieNamed("uaid");
        } catch (WebDriverException e) {
            e.printStackTrace();
        }
    }
}
