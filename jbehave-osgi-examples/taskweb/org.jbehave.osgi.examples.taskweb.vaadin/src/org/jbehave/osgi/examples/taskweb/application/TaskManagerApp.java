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
import org.apache.shiro.subject.Subject;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.HttpServletRequestListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

public class TaskManagerApp extends Application implements
		HttpServletRequestListener {

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
	private static void setInstance(TaskManagerApp application) {
		if (currentApplication.get() == null) {
			currentApplication.set(application);
		}
	}

	private LogService logService;

	private HomePage homePage;
	private Window taskWebWindow;
	private LoginSubWindow loginSubWindow;
	private TaskManagementPage taskManagementPage;
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
		logService = null;
		currentApplication = null;
	}

	protected LogService getLogService() {
		return logService;
	}

	@Override
	public void init() {

		setTheme("cvgaviao");

		setMainWindow(getTaskWebWindow());

		getTaskWebWindow().setContent(getHomePage());

	}
	
	@Override
	public String getVersion() {

		return "0.0.2-SNAPHOT";
	}
	
	private Window getTaskWebWindow(){
		if (taskWebWindow == null)
		{
			taskWebWindow = new Window("JBehave TaskWeb!");
		}
		return taskWebWindow;
	}
	
	
	private HomePage getHomePage() {
		if (homePage == null)
		{
			homePage = new HomePage();
		}
		return homePage;
	}

	public void loginCancelled() {
		getTaskWebWindow().removeWindow(loginSubWindow);
		loginSubWindow = null;
		if (getUser() != null) {
			getMainWindow().showNotification("Login well succeed.");
		} else {
			getMainWindow().showNotification("Login canceled !");
		}
		getHomePage().enableLoginButton();

	}

	public void loginWellSucceded() {
		getTaskWebWindow().removeWindow(loginSubWindow);

		// Switch to the protected view
		getMainWindow().setContent(getTaskManagementPage());

		loginSubWindow = null;
	}

	public void loginBadlySucceded(String message) {
		getTaskWebWindow().removeWindow(loginSubWindow);
		
		getMainWindow().showNotification(message, Notification.TYPE_ERROR_MESSAGE);
		getHomePage().enableLoginButton();
		
		loginSubWindow = null;
	}

	private TaskManagementPage getTaskManagementPage() {
		if (taskManagementPage == null)
		{
			taskManagementPage = new TaskManagementPage();
		}
		return taskManagementPage;
	}

	public void logout() {

		Subject currentUser = SecurityUtils.getSubject();

		currentUser.logout();

		getInstance().setUser(null);
		
		loginSubWindow = null;
		
		homePage = null;
		
		taskManagementPage = null;

		getTaskWebWindow().setContent(getHomePage());
		
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

			taskWebWindow.setContent(getHomePage());

			getInstance().getMainWindow().showNotification(
					"You need to authenticate again !");

		} else {
			setUser(currentUser);
		}
	}

	public void showLoginSubWindow() {

		if (loginSubWindow == null) {
			loginSubWindow = new LoginSubWindow(this);
			if (loginSubWindow.getParent() == null) {
				taskWebWindow.addWindow(loginSubWindow);
			}
			// Center the window
			loginSubWindow.center();
		}
	}

	protected void unbindLogService(LogService logService) {
		if (this.logService == logService) {
			getLogService().log(LogService.LOG_DEBUG, "Unbinded LogService.");
			this.logService = null;
		}
	}
}
