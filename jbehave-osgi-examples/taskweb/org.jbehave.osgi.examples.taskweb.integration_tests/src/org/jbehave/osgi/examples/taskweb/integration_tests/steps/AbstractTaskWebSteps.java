package org.jbehave.osgi.examples.taskweb.integration_tests.steps;

import org.jbehave.osgi.examples.taskweb.integration_tests.pages.TaskWebPageFactory;
import org.jbehave.web.selenium.WebDriverProvider;
import org.jbehave.web.selenium.WebDriverSteps;

public class AbstractTaskWebSteps extends WebDriverSteps {

	private final TaskWebPageFactory pages;

	public AbstractTaskWebSteps(WebDriverProvider driverProvider,
			TaskWebPageFactory pageFactory) {
		super(driverProvider);
		this.pages = pageFactory;
	}

	public TaskWebPageFactory getPages() {
		return pages;
	}
}
