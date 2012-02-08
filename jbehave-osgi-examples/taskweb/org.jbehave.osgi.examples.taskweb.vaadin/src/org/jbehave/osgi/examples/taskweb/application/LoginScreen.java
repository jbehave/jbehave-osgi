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

import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.LoginForm;
import com.vaadin.ui.LoginForm.LoginEvent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;

public class LoginScreen extends VerticalLayout
{
	private static final long serialVersionUID = 1L;


	public LoginScreen()
	{

		// The application caption is shown in the caption bar of the
		// browser window.
		TaskManagerApp.get().getMainWindow().setCaption("Simple Vaadin Shiro example");

		setSizeFull();

		Panel loginPanel = new Panel("Login");
		loginPanel.setWidth("400px");

		LoginForm loginForm = new LoginForm();
		loginForm.setPasswordCaption("Password");
		loginForm.setUsernameCaption("User");
		loginForm.setLoginButtonCaption("Do it !!!");

		loginForm.setHeight("100px");
		loginForm.addListener(new MyLoginListener(TaskManagerApp.get(), loginForm));

		loginPanel.addComponent(loginForm);

		addComponent(loginPanel);
		setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);

		HorizontalLayout footer = new HorizontalLayout();
		footer.setHeight("50px");
		addComponent(footer);
	}

	private static class MyLoginListener implements LoginForm.LoginListener
	{
		private static final long serialVersionUID = 1L;
		private TaskManagerApp app;
		private LoginForm loginForm;


		public MyLoginListener(TaskManagerApp app, LoginForm loginForm)
		{
			this.app = app;
			this.loginForm = loginForm;
		}


		@Override
		public void onLogin(LoginEvent event)
		{
			String username = event.getLoginParameter("username");
			String password = event.getLoginParameter("password");

			try
			{
				TaskManagerApp.get().login(username, password);
				
				// Switch to the protected view
				app.getMainWindow().setContent(new AuthenticatedMainScreen());
			}
			catch (UnknownAccountException uae)
			{
				this.loginForm.getWindow().showNotification("Invalid User", Notification.TYPE_ERROR_MESSAGE);
			}
			catch (IncorrectCredentialsException ice)
			{
				this.loginForm.getWindow().showNotification("Invalid User", Notification.TYPE_ERROR_MESSAGE);
			}
			catch (LockedAccountException lae)
			{
				this.loginForm.getWindow().showNotification("Invalid User", Notification.TYPE_ERROR_MESSAGE);
			}
			catch (ExcessiveAttemptsException eae)
			{
				this.loginForm.getWindow().showNotification("Invalid User", Notification.TYPE_ERROR_MESSAGE);
			}
			catch (AuthenticationException ae)
			{
				this.loginForm.getWindow().showNotification("Invalid User", Notification.TYPE_ERROR_MESSAGE);
			}
			catch (Exception ex)
			{
				this.loginForm.getWindow().showNotification("Exception " + ex.getMessage(), Notification.TYPE_ERROR_MESSAGE);
			}
		}
	}
}
