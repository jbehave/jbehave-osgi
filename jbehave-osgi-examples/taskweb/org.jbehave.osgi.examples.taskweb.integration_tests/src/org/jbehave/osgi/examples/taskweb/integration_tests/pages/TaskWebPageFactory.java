package org.jbehave.osgi.examples.taskweb.integration_tests.pages;


import org.jbehave.web.selenium.WebDriverProvider;

public class TaskWebPageFactory {

    private final WebDriverProvider webDriverProvider;
	private HomePage homePage;

    public TaskWebPageFactory(WebDriverProvider webDriverProvider) {
        this.webDriverProvider = webDriverProvider;
    }
    
    public LoginPage login() {
        
        return new LoginPage(webDriverProvider);
    }

    public TaskManagementPage taskManagement() {
        
        return new TaskManagementPage(webDriverProvider);
    }

	public HomePage home() {
		if (homePage == null)
		{
			homePage = new HomePage(webDriverProvider);
		}
		return homePage;
	}




}
