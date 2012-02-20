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

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.jbehave.osgi.examples.taskweb.application.LoginComponent.LoginEventType;

import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class LoginSubWindow extends Window {

	private TaskManagerApp app;
	private LoginComponent loginComponent;
	private LoginComponent.LoginAttemptListener loginAttemptListener;
	private boolean loginEventProcessed = false;

	public LoginSubWindow(final TaskManagerApp app) {
		this.app = app;

		setStyleName("login");
		setCaption("TaskWeb Login");
		setHeight("170px");
		setWidth("340px");
		setResizable(false);
		setDebugId("loginDialog");

		addComponent(getLoginComponent());

		addListener(new Window.CloseListener() {

			public void windowClose(CloseEvent e) {

				if (loginEventProcessed == false && app.getUser() == null)
					getLoginComponent().notifyLoginCancelEvent();

				loginComponent.removeListener(getLoginAttemptListener());
				loginComponent = null;
			}
		});
	}

	private LoginComponent getLoginComponent() {
		if (loginComponent == null) {
			loginComponent = new LoginComponent();
			loginComponent.addListener(getLoginAttemptListener());
		}
		return loginComponent;
	}

	public void login(String username, String password, Boolean rememberMe) {
		UsernamePasswordToken token;

		token = new UsernamePasswordToken(username, password);
		// ”Remember Me” built-in, just do this:
		token.setRememberMe(rememberMe);

		// With most of Shiro, you'll always want to make sure you're working
		// with the currently executing user,
		// referred to as the subject
		Subject currentUser = SecurityUtils.getSubject();

		// Authenticate
		currentUser.login(token);

		// no error found
		app.setUser(currentUser);

	}

	private LoginComponent.LoginAttemptListener getLoginAttemptListener() {
		if (loginAttemptListener == null) {
			loginAttemptListener = new MyLoginListener(app);
		}
		return loginAttemptListener;
	}

	private class MyLoginListener implements
			LoginComponent.LoginAttemptListener {
		private TaskManagerApp app;

		public MyLoginListener(TaskManagerApp app) {
			this.app = app;
		}

		@Override
		public void onLoginEvent(LoginComponent.LoginEvent event) {

			loginEventProcessed = true;

			if (event.getType() == LoginEventType.ATTEMPT) {

				String username = (String) event.getLoginParameter("username");
				String password = (String) event.getLoginParameter("password");
				Boolean rememberMe = (Boolean) event
						.getLoginParameter("rememberMe");

				try {
					login(username, password, rememberMe);

					// close the login window
					app.loginWellSucceded();

				} catch (UnknownAccountException uae) {
					app.loginBadlySucceded("Invalid User");
				} catch (IncorrectCredentialsException ice) {
					app.loginBadlySucceded("Invalid User");
				} catch (LockedAccountException lae) {
					app.loginBadlySucceded("Blocked User");
				} catch (ExcessiveAttemptsException eae) {
					app.loginBadlySucceded("Blocked User");
				} catch (AuthenticationException ae) {
					app.loginBadlySucceded("Authentication Error");
				} catch (Exception ex) {
					app.loginBadlySucceded("Exception: " + ex.getMessage());
				}
			} else if (event.getType() == LoginEventType.CANCEL) {
				app.loginCancelled();
			}
		}
	}
}
