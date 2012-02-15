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
package org.jbehave.osgi.examples.taskweb.application;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;

import com.vaadin.ui.LoginForm;
import com.vaadin.ui.LoginForm.LoginEvent;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class LoginSubWindow extends Window {

	private static boolean loginSucceed = false;

	public LoginSubWindow() {
		setDebugId("loginDialog");
		setStyleName("login");
		setCaption("TaskWeb Login");
		setHeight("230px");
		setWidth("250px");
		setResizable(false);
		LoginForm loginForm = new LoginForm();
		loginForm.setStyleName("login");
		loginForm.setDebugId("loginForm");
//		loginForm.setPasswordCaption("Password");
//		loginForm.setUsernameCaption("User");
		// loginForm.setLoginButtonCaption("Do it !!!");
		loginForm
				.addListener(new MyLoginListener(TaskManagerApp.getInstance()));
		addComponent(loginForm);

		addListener(new Window.CloseListener() {

			public void windowClose(CloseEvent e) {

				if (loginSucceed) {
					TaskManagerApp.getInstance().getMainWindow()
							.showNotification("Login well succeed.");
				} else {
					TaskManagerApp.getInstance().getMainWindow()
							.showNotification("Login canceled.");
				}
			}
		});
	}

	private static class MyLoginListener implements LoginForm.LoginListener {
		private TaskManagerApp app;

		public MyLoginListener(TaskManagerApp app) {
			this.app = app;
		}

		@Override
		public void onLogin(LoginEvent event) {
			String username = event.getLoginParameter("username");
			String password = event.getLoginParameter("password");

			try {
				app.login(username, password);

				loginSucceed = true;

				// close the login window
				app.closeLoginSubWindow();

				// Switch to the protected view
				app.getMainWindow().setContent(new TaskManagementPage());

			} catch (UnknownAccountException uae) {
				app.getMainWindow().showNotification("Invalid User",
						Notification.TYPE_ERROR_MESSAGE);
			} catch (IncorrectCredentialsException ice) {
				app.getMainWindow().showNotification("Invalid User",
						Notification.TYPE_ERROR_MESSAGE);
			} catch (LockedAccountException lae) {
				app.getMainWindow().showNotification("Invalid User",
						Notification.TYPE_ERROR_MESSAGE);
			} catch (ExcessiveAttemptsException eae) {
				app.getMainWindow().showNotification("Invalid User",
						Notification.TYPE_ERROR_MESSAGE);
			} catch (AuthenticationException ae) {
				app.getMainWindow().showNotification("Invalid User",
						Notification.TYPE_ERROR_MESSAGE);
			} catch (Exception ex) {
				app.getMainWindow().showNotification(
						"Exception " + ex.getMessage(),
						Notification.TYPE_ERROR_MESSAGE);
			}
		}
	}
}
