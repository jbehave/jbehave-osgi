package org.jbehave.osgi.examples.taskweb.integration_tests.pages;

import java.util.concurrent.TimeUnit;

import org.jbehave.web.selenium.WebDriverProvider;
import org.openqa.selenium.By;

public class HomePage extends AbstractTaskWebPage {

	public HomePage(WebDriverProvider driverProvider) {
		super(driverProvider);
	}

	public void open() {
		get("http://localhost:8088/taskweb/?debug");
		manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
	}

	public void clicksLoginButton() {
		findElementWithWait(By.id("loginAttemptButton"), 5).click();
	}

	public void assertLoginButtonEnabled() {
		waitUntilElementIsEnabled(By.id("loginAttemptButton"), 5);

	}

	public void assertLoginButtonDisabled() {
		waitUntilElementIsDisabled(By.id("loginAttemptButton"), 5);
	}

	public void pageIsPresented() {
		waitUntilElementBePresent(By.id("panelPeople"), 5);
		waitUntilElementBePresent(By.id("panelNews"), 5);
	}

	public void assertLoginDialogWasClosed() {
		waitUntilDialogBeClosed("loginDialog");
	}

	public void assertNotificationWasDisplayed(String message) {
		waitUntilNotificationAppears(message, 5);
		waitAndClickOnCurrentNotification();
	}
}
