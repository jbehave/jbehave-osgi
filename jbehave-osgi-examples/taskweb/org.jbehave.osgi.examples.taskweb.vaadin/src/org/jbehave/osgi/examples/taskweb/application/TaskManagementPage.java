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

import org.apache.shiro.subject.Subject;

import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.Runo;

@SuppressWarnings("serial")
public class TaskManagementPage extends AbstractAuthenticatedCommonPage {

	private Accordion taskAccordion;
	private TaskGroup adminTasksPanel;
	private TaskGroup personalTasksPanel;
	private TaskGroup teamTasksPanel;
	private HorizontalSplitPanel splitPanel;
	private Panel rightPanel;

	public TaskManagementPage() {
		super();
	}

	public Component getCenterComponent() {
		if (splitPanel == null) {
			splitPanel = new HorizontalSplitPanel();
			splitPanel.setDebugId("taskSplitPanel");
			splitPanel.addComponent(getSplitPanelLeftComponent());
			splitPanel.addComponent(getSplitPanelRightComponent());
			splitPanel.setSplitPosition(30, Sizeable.UNITS_PERCENTAGE);
		}
		return splitPanel;
	}

	protected Component getSplitPanelLeftComponent() {
		if (taskAccordion == null) {
			adminTasksPanel = new TaskGroup();
			personalTasksPanel = new TaskGroup();
			teamTasksPanel = new TaskGroup();
			taskAccordion = new Accordion();
			taskAccordion.setDebugId("taskAccordion");

			if (getCurrentUser().hasRole("admin")) {
				Tab tabAdm = taskAccordion.addTab(adminTasksPanel,
						"Administrative Tasks");
				int numTasks = fillTasks(adminTasksPanel, getCurrentUser());
				tabAdm.setCaption(tabAdm.getCaption() + " (" + numTasks + ")");
			}
			if (getCurrentUser().isPermitted("personalTasks")) {
				Tab tabPersonal = taskAccordion.addTab(personalTasksPanel,
						"Personal Tasks");
				int numTasks = fillTasks(personalTasksPanel, getCurrentUser());
				tabPersonal.setCaption(tabPersonal.getCaption() + " ("
						+ numTasks + ")");
			}
			if (getCurrentUser().hasRole("manager")
					|| getCurrentUser().isPermitted("teamTasks")) {
				Tab tabTeam = taskAccordion
						.addTab(teamTasksPanel, "Team Tasks");
				int numTasks = fillTasks(teamTasksPanel, getCurrentUser());
				tabTeam.setCaption(tabTeam.getCaption() + " (" + numTasks + ")");
			}
		}
		return taskAccordion;
	}

	private int fillTasks(TaskGroup taskgroup, Subject currentUser) {
		int numTasks = 5;
		for (int i = 1; i <= numTasks; i++) {
			TaskItem task = new TaskItem("Task " + i);
			taskgroup.addComponent(task);
		}
		return numTasks;
	}

	protected Component getSplitPanelRightComponent() {
		if (rightPanel == null) {
			rightPanel = new Panel();
			rightPanel.setSizeFull();
		}
		return rightPanel;
	}

	class TaskGroup extends VerticalLayout {
		public TaskGroup() {
			super();
			setMargin(false);
			setStyleName(Runo.CSSLAYOUT_SHADOW);
			setSizeFull();
		}
	}

	class TaskItem extends Button {
		public TaskItem(String string) {
			super(string);
			setStyleName(Reindeer.BUTTON_LINK);
			setWidth("100%");
			setHeight("45px");
			setIcon(new ThemeResource("../runo/icons/32/document-add.png"));
			addListener(new Button.ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					getSplitPanelRightComponent().setCaption(
							event.getButton().getCaption());
				}
			});
		}
	}

}
