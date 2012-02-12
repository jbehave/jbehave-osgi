package org.jbehave.osgi.examples.taskweb.integration_tests.pages;

import org.jbehave.web.selenium.WebDriverProvider;

public class LoginPage extends AbstractTaskWebPage {

	public LoginPage(WebDriverProvider driverProvider) {
		super(driverProvider);
	}

	public void pageIsShown() {
        found("Login");
    }

}
