/*******************************************************************************
 * Copyright (c) 2011 - 2012, Cristiano Gavião - C4Biz
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cristiano Gavião - initial API and implementation
 *******************************************************************************/
package org.jbehave.osgi.examples.taskweb.integration_tests.steps;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.osgi.examples.taskweb.integration_tests.pages.TaskWebPageFactory;
import org.jbehave.web.selenium.WebDriverProvider;

public class AuthenticationSteps extends AbstractTaskWebSteps {

	public AuthenticationSteps(WebDriverProvider driverProvider,
			TaskWebPageFactory pageFactory) {
		super(driverProvider, pageFactory);
	}

	@Given("the user is on home page")
	public void givenUserOnInHomePage() {
		getPages().home().open();
	}

	@Given("the login button is enabled")
	public void givenLoginButtonIsEnabled() {
		getPages().home().assertLoginButtonEnabled();
	}

	@When("the user clicks in login button")
	public void userClicksOnLoginButton() {
		getPages().home().clicksLoginButton();
	}

	@Then("the login dialog is presented")
	public void loginDialogIsPresented() {
		getPages().login().loginDialogIsPresented();
	}

	@Then("the login button is disabled")
	public void ensureLoginButtonIsDisabled() {
		getPages().home().assertLoginButtonDisabled();
	}

	@Then("the login button is enabled")
	public void thenTheLoginButtonIsEnabled(){
		getPages().home().assertLoginButtonEnabled();
	}
	
	@When("the user authenticates with a userID <userID> and password <password>")
	public void whenUserAuthenticate(@Named("userID") String userID,
			@Named("password") String password) {
		getPages().login().authenticateWith(userID, password);
	}

	@Then("the TaskManagement page is presented")
	public void thenTaskManagementPageIsPresented() {
		getPages().taskManagement().pageIsPresented();
	}

	@Then("the user is forwarded to the home page")
	public void thenTheUserIsForwardedToTheHomePage() {
		getPages().home().pageIsPresented();
	}

	@Then("the user main role <role> description is presented")
	public void thenMainRoleDescIsPresented(@Named("role") String role) {
		getPages().taskManagement().roleDescriptionIsPresented(role);
	}

	@When("the user clicks in logout button")
	public void userClicksOnLogoutButton() {
		getPages().taskManagement().clicksLogoutButton();
	}
}
