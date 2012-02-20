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
import org.apache.shiro.subject.Subject;
import org.jbehave.osgi.examples.taskweb.application.HomePage.SmallText;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.Reindeer;

@SuppressWarnings("serial")
public abstract class AbstractAuthenticatedCommonPage extends VerticalLayout {

	private HorizontalLayout toolbar;
	private HorizontalLayout headerTitle;
	private HorizontalLayout header;
	private HorizontalLayout footer;
	private MenuBar mainMenuBar;
	private Button logoutButton;
	private Subject currentUser;
	private Component centerComponent;

	public void setCenterComponent(Component centerComponent) {
		this.centerComponent = centerComponent;
	}

	public AbstractAuthenticatedCommonPage() {

		setCurrentUser(SecurityUtils.getSubject());

		setStyleName(Reindeer.LAYOUT_BLUE);

		setMargin(false);
		setSizeFull();

		addComponent(getHeaderComponent());
		addComponent(getMenuBar());
		addComponent(getCenterComponent());
		addComponent(getFooterComponent());

		setExpandRatio(getCenterComponent(), 1.0f);
	}

	private Component getFooterComponent() {
		if (footer == null) {
			footer = new HorizontalLayout();
			footer.setHeight("30px");
			footer.setWidth("100%");
			footer.setStyleName(Reindeer.LAYOUT_WHITE);
		}

		return footer;
	}

	private Layout getHeaderComponent() {
		if (header == null) {
			header = new HorizontalLayout();
			header.setWidth("100%");
			header.setHeight("80px");
			header.addComponent(getHeaderTitle());
			header.addComponent(getToolbar());
			header.setExpandRatio(getHeaderTitle(), 1.0f);
			header.setExpandRatio(getToolbar(), 2.0f);
		}
		return header;
	}

	private Layout getHeaderTitle() {
		if (headerTitle == null) {
			headerTitle = new HorizontalLayout();
			headerTitle.setSizeFull();
			headerTitle.setMargin(true);
			headerTitle.setSpacing(true);

			CssLayout titleLayout = new CssLayout();
			HomePage.H1 title = new HomePage.H1("JBehave TaskWeb");
			titleLayout.addComponent(title);
			headerTitle.addComponent(titleLayout);
			SmallText roleDescription = new SmallText("Logged in as "
					+ currentUser.getPrincipal().toString());
			roleDescription.setSizeUndefined();
			roleDescription.setDebugId("roleDescription");
			titleLayout.addComponent(roleDescription);
		}
		return headerTitle;
	}

	private Component getMenuBar() {
		if (mainMenuBar == null) {
			mainMenuBar = new MenuBar();
			mainMenuBar.setWidth("100%");

		}
		return mainMenuBar;
	}

	public Component getCenterComponent() {
		if (centerComponent == null) {
			centerComponent = (Panel) new Panel();
			centerComponent.setSizeFull();
		}
		return centerComponent;
	}

	private Layout getToolbar() {
		if (toolbar == null) {
			toolbar = new HorizontalLayout();
			toolbar.setSizeFull();
			toolbar.setMargin(true);
			toolbar.setSpacing(true);
			logoutButton = new Button("Log out", new Button.ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					TaskManagerApp.getInstance().logout();
				}
			});
			logoutButton.setStyleName(Reindeer.BUTTON_DEFAULT);
			logoutButton.setDebugId("logoutButton");
			toolbar.addComponent(logoutButton);
			toolbar.setComponentAlignment(logoutButton, Alignment.BOTTOM_RIGHT);
		}
		return toolbar;
	}

	public Subject getCurrentUser() {
		return currentUser;
	}

	private void setCurrentUser(Subject currentUser) {
		this.currentUser = currentUser;
	}

}
