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
package org.jbehave.osgi.examples.taskweb.integration_tests.pages;

import org.jbehave.web.selenium.WebDriverProvider;
import org.openqa.selenium.By;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TaskManagementPage extends AbstractTaskWebPage {

	public TaskManagementPage(WebDriverProvider driverProvider) {
		super(driverProvider);
	}

	public void pageIsPresented() {
		waitUntilElementBePresent(By.id("taskSplitPanel"), 5);
		waitUntilElementBePresent(By.id("taskAccordion"), 5);
	}

	public void roleDescriptionIsPresented(String role) {

		assertThat(findElementWithWait(By.id("roleDescription"), 5).getText(),
				equalTo("Logged in as " + role));
	}

	public void clicksLogoutButton() {
		findElementWithWait(By.id("logoutButton"), 5).click();
	}

}
