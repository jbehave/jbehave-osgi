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

	private static final long serialVersionUID = 1L;

	private LogService logService;

	private static ThreadLocal<TaskManagerApp> currentApplication = new ThreadLocal<TaskManagerApp>();

	@Override
	public void init() {

		final Window mainFirstWindow = new Window("Guess It!");
		setMainWindow(mainFirstWindow);
		setTheme("chameleon");
		mainFirstWindow.setWidth("300px");

		// mainFirstWindow.setContent(new LoginScreen(get()));

		// set the current application on thread local
		set(this);

	}
	
	protected void activate(ComponentContext context,
			Map<String, Object> properties) {

		getLogService().log(LogService.LOG_DEBUG,
				"Activating GuessIt Application OSGi Component.");
	}

	protected void deactivate(ComponentContext context,
			Map<String, Object> properties) {

		getLogService().log(LogService.LOG_DEBUG,
				"Deactivating GuessIt Application OSGi Component.");
	}

	/**
	 * @return the current application instance
	 */
	public static TaskManagerApp get() {
		return currentApplication.get();
	}

	/**
	 * Set the current application instance
	 */
	public static void set(TaskManagerApp application) {
		if (get() == null) {
			currentApplication.set(application);
		}
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
		get().setUser(currentUser);

	}

	public void logout() {
		getMainWindow().getApplication().close();

		Subject currentUser = SecurityUtils.getSubject();

		if (currentUser.isAuthenticated()) {
			currentUser.logout();

			get().setUser(null);
		}
	}

	public void onRequestStart(HttpServletRequest request,
			HttpServletResponse response) {
		getLogService().log(LogService.LOG_DEBUG, "onRequestStart reached.");

		// Set current application object as thread-local to make it easy
		// accessible
		set(this);

		if (getMainWindow() == null)
			return;

		// Authentication: check if user is found, otherwise send to login page
		Subject currentUser = SecurityUtils.getSubject();

		if (!currentUser.isAuthenticated()) {

			ComponentContainer currentContent = get().getMainWindow()
					.getContent();

			if (!LoginScreen.class.isAssignableFrom(currentContent.getClass())) {

				get().getMainWindow().setContent(new LoginScreen());
			}

		} else {
			setUser(currentUser);
		}
	}

	public void onRequestEnd(HttpServletRequest request,
			HttpServletResponse response) {
		getLogService().log(LogService.LOG_DEBUG, "onRequestEnd reached");

		// Clean up thread-local app
		set(null);

		// Clear authentication context
		// Authentication.setAuthenticatedUserId(null);

		// Callback to the login handler
		// loginHandler.onRequestEnd(request, response);

	}

	protected void bindLogService(LogService logService) {
		this.logService = logService;
		getLogService().log(LogService.LOG_DEBUG, "Binded LogService.");
	}

	protected LogService getLogService() {
		return logService;
	}

	protected void unbindLogService(LogService logService) {
		if (this.logService == logService) {
			getLogService().log(LogService.LOG_DEBUG, "Unbinded LogService.");
			this.logService = null;
		}
	}

	// Logout Listener is defined for the application
	public static class LogoutListener implements Button.ClickListener {
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			get().logout();
		}
	}
}
