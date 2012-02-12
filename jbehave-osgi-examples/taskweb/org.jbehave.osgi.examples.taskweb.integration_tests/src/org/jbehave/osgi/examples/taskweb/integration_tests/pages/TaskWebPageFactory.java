package org.jbehave.osgi.examples.taskweb.integration_tests.pages;


import org.jbehave.web.selenium.WebDriverProvider;

public class TaskWebPageFactory {

    private final WebDriverProvider webDriverProvider;

    public TaskWebPageFactory(WebDriverProvider webDriverProvider) {
        this.webDriverProvider = webDriverProvider;
    }
    
    public LoginPage login() {
        
        return new LoginPage(webDriverProvider);
    }

    public AuthenticatedHomePage authenticatedHome() {
        
        return new AuthenticatedHomePage(webDriverProvider);
    }




}
