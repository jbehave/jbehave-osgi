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

public class LoginPage extends AbstractTaskWebPage {

	public LoginPage(WebDriverProvider driverProvider) {
		super(driverProvider);
	}

	public void loginDialogIsPresented() {

		findElementWithWait(By.id("loginDialog"), 15);
	}

	public void authenticateWith(String userID, String password) {
				
		findElementWithWait(By.id("loginUser"), 5).sendKeys(userID);
		findElementWithWait(By.id("loginPassword"), 5).sendKeys(password);
		findElementWithWait(By.id("loginButton"), 5).click();
	}
	

	public void verifyLoginCancelation() {
		// Alert alert = driver.switchTo().alert();
	}

	public void cancelLoginByClickingCloseButton() {
		findElementWithWait(By.id("cancelButton"), 5).click();
	}

}
