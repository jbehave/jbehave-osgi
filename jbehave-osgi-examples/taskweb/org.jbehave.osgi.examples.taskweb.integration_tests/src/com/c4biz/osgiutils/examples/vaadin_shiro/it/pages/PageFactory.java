package com.c4biz.osgiutils.examples.vaadin_shiro.it.pages;


import org.jbehave.web.selenium.WebDriverProvider;

public class PageFactory {

    private final WebDriverProvider webDriverProvider;
    private final boolean fluentStyle;

    public PageFactory(WebDriverProvider webDriverProvider) {
        this(webDriverProvider, Boolean.parseBoolean(System.getProperty("FLUENT_STYLE", "false")));
    }
    
    public PageFactory(WebDriverProvider webDriverProvider, boolean fluentStyle) {
        this.webDriverProvider = webDriverProvider;
        this.fluentStyle = fluentStyle;
    }

    public Login newAdvancedSearch() {
        
        return new Login(webDriverProvider);
    }

    public AuthenticatedHome newHome() {
        
        return new AuthenticatedHome(webDriverProvider);
    }




}
