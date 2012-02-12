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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.HttpServletRequestListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Window;

public class TaskManagerApp extends Application implements
		HttpServletRequestListener {

	// Logout Listener is defined for the application
	@SuppressWarnings("serial")
	public static class LogoutListener implements Button.ClickListener {

		@Override
		public void buttonClick(ClickEvent event) {
			getInstance().logout();
		}
	}

	@SuppressWarnings("serial")
	public static class LoginListener implements Button.ClickListener {
		
		@Override
		public void buttonClick(ClickEvent event) {
			getInstance().showLoginSubWindow();
		}
	}

	private static final long serialVersionUID = 1L;

	/**
	 * @return the current application instance
	 */
	public static TaskManagerApp getInstance() {
		return currentApplication.get();
	}

	/**
	 * Set the current application instance
	 */
	public static void setInstance(TaskManagerApp application) {
		if (getInstance() == null) {
			currentApplication.set(application);
		}
	}

	private LogService logService;
	private HomePage homePage = new HomePage();
	private Window mainWindow;
	private LoginSubWindow loginSubWindow;

	private static ThreadLocal<TaskManagerApp> currentApplication = new ThreadLocal<TaskManagerApp>();

	protected void activate(ComponentContext context,
			Map<String, Object> properties) {

		getLogService().log(LogService.LOG_DEBUG,
				"Activating TaskWeb Application OSGi Component.");
	}

	protected void bindLogService(LogService logService) {
		this.logService = logService;
		getLogService().log(LogService.LOG_DEBUG, "Binded LogService.");
	}

	protected void deactivate(ComponentContext context,
			Map<String, Object> properties) {

		getLogService().log(LogService.LOG_DEBUG,
				"Deactivating TaskWeb Application OSGi Component.");
	}

	protected LogService getLogService() {
		return logService;
	}

	@Override
	public void init() {

		mainWindow = new Window("JBehave TaskWeb!");
		setTheme("cvgaviao");
		setMainWindow(mainWindow);
		mainWindow.setContent(homePage);

		// set the current application on thread local
		setInstance(this);

	}

	public void login(String username, String password) {
		UsernamePasswordToken token;

		token = new UsernamePasswordToken(username, password);
		// ”Remember Me” built-in, just do this:
		token.setRememberMe(true);

		// With most of Shiro, you'll always want to make sure you're working
		// with the currently executing user,
		// referred to as the subject
		Subject currentUser = SecurityUtils.getSubject();

		// Authenticate
		currentUser.login(token);

		// no error found
		getInstance().setUser(currentUser);

	}

	public void logout() {
		getMainWindow().getApplication().close();

		Subject currentUser = SecurityUtils.getSubject();

		if (currentUser.isAuthenticated()) {
			currentUser.logout();

			getInstance().setUser(null);
		}
	}

	public void onRequestEnd(HttpServletRequest request,
			HttpServletResponse response) {
		getLogService().log(LogService.LOG_DEBUG, "onRequestEnd reached");

		// Clean up thread-local app
		setInstance(null);

		// Clear authentication context
		// Authentication.setAuthenticatedUserId(null);

		// Callback to the login handler
		// loginHandler.onRequestEnd(request, response);

	}

	public void onRequestStart(HttpServletRequest request,
			HttpServletResponse response) {
		getLogService().log(LogService.LOG_DEBUG, "onRequestStart reached.");

		// Set current application object as thread-local to make it easy
		// accessible
		setInstance(this);

		if (getMainWindow() == null)
			return;

		ComponentContainer currentContent = getInstance().getMainWindow()
				.getContent();

		if (HomePage.class.isAssignableFrom(currentContent.getClass()))
			return;

		// Authentication: check if user is found, otherwise send to home page
		Subject currentUser = SecurityUtils.getSubject();
		
		if (currentContent != null && !currentUser.isAuthenticated()) {

			mainWindow.setContent(homePage);
			
			getInstance().getMainWindow()
			.showNotification("You need to authenticate again !");

		} else {
			setUser(currentUser);
		}
	}

	public void showLoginSubWindow() {

		loginSubWindow = new LoginSubWindow();        
        if (loginSubWindow.getParent() == null) {
        	mainWindow.addWindow(loginSubWindow);
        }
        // Center the window
        loginSubWindow.center();
	}
	
	public void closeLoginSubWindow()
	{
		if (loginSubWindow != null && loginSubWindow.getParent() != null)
		{
			mainWindow.removeWindow(loginSubWindow);
		}
	}

	protected void unbindLogService(LogService logService) {
		if (this.logService == logService) {
			getLogService().log(LogService.LOG_DEBUG, "Unbinded LogService.");
			this.logService = null;
		}
	}
}
