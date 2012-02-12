package org.jbehave.osgi.examples.taskweb.integration_tests.steps;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.osgi.examples.taskweb.integration_tests.pages.TaskWebPageFactory;
import org.jbehave.web.selenium.WebDriverProvider;

public class AuthenticationSteps extends AbstractTaskWebSteps {
	
	public AuthenticationSteps(WebDriverProvider driverProvider,
			TaskWebPageFactory pageFactory) {
		super(driverProvider, pageFactory);
	}

	@Given("the user has opened a new blank page")
	public void userOpenedBlankPage()
	{
		
	}
	
	@When("the user calls the application using the URL \"%url\"")
	public void userCallApplication(String url)
	{
//		driver.get(url);
	}

	@When("the user authenticate with a userID <userID> and password <password>")
	public void whenUserAuthenticate(String url)
	{
//		driver.get(url);
	}
	
	@Then("the AuthenticatedHome page is showed")
	public void thenAuthenticatedHomePageIsShown() {
		getPages().login().pageIsShown();
	}

	@Then("the login page is showed")
	public void thenLoginPageIsShown() {
		getPages().login().pageIsShown();
	}
}
