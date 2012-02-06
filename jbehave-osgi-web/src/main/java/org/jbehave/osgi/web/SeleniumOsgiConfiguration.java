package org.jbehave.osgi.web;

import org.jbehave.osgi.configuration.OsgiConfiguration;
import org.jbehave.web.selenium.SeleniumContext;
import org.jbehave.web.selenium.WebDriverProvider;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.condition.ConditionRunner;
import com.thoughtworks.selenium.condition.JUnitConditionRunner;

public class SeleniumOsgiConfiguration extends OsgiConfiguration {

	private Selenium selenium;
	private SeleniumContext seleniumContext;
	private WebDriverProvider driverProvider;

    public SeleniumOsgiConfiguration() {
    }

    public Selenium selenium() {
        synchronized (this) {
            if (selenium == null) {
                selenium = defaultSelenium();
            }
        }
        return selenium;
    }

    public SeleniumOsgiConfiguration useSelenium(Selenium selenium){
        this.selenium = selenium;
        return this;
    }
    
    public SeleniumContext seleniumContext() {
        synchronized (this) {
            if (seleniumContext == null) {
                seleniumContext = new SeleniumContext();
            }
        }
        return seleniumContext;
    }

    public SeleniumOsgiConfiguration useSeleniumContext(SeleniumContext seleniumContext) {
        this.seleniumContext = seleniumContext;
        return this;
    }
    
    public WebDriverProvider webDriverProvider() {
        return driverProvider;
    }

    public SeleniumOsgiConfiguration useWebDriverProvider(WebDriverProvider webDriverProvider){
        this.driverProvider = webDriverProvider;
        return this;
    }
    
    /**
     * Creates default Selenium instance: {@link DefaultSelenium("localhost",
     * 4444, "*firefox", "http://localhost:8080")}
     * 
     * @return A Selenium instance
     */
    public static Selenium defaultSelenium() {
        return new DefaultSelenium("localhost", 4444, "*firefox", "http://localhost:8080");
    }

    /**
     * Creates default ConditionRunner: {@link JUnitConditionRunner(selenium,
     * 10, 100, 1000)}.
     * 
     * @param selenium
     *            the Selenium instance
     * @return A ConditionRunner
     */
    public static ConditionRunner defaultConditionRunner(Selenium selenium) {
        return new JUnitConditionRunner(selenium, 10, 100, 1000);
    }
    
}
