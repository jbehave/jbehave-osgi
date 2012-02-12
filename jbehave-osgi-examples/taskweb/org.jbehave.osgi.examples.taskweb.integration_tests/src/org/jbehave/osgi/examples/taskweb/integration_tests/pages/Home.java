package org.jbehave.osgi.examples.taskweb.integration_tests.pages;

import java.util.concurrent.TimeUnit;

import org.jbehave.web.selenium.WebDriverProvider;

public class Home extends AbstractTaskWebPage {

    public Home(WebDriverProvider driverProvider) {
        super(driverProvider);
    }

    public void open() {
        get("http://localhost:8080/taskweb/");
        manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

}
