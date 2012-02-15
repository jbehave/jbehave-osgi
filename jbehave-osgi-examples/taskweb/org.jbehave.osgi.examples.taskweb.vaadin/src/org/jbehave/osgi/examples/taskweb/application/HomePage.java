package org.jbehave.osgi.examples.taskweb.application;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class HomePage extends VerticalLayout {

	private static final long serialVersionUID = -5562643818682670675L;

	private Button loginButton;
	private Button helpButton;
	private Panel panelNews;
	private Panel panelPeople;
	private VerticalLayout glueLayout;
	private Layout toolbar;
	private HorizontalLayout headerTitle;
	private HorizontalLayout header;
	private HorizontalLayout panels;

	public HomePage() {

		setMargin(false);
		setStyleName(Reindeer.LAYOUT_BLUE);
		setSizeFull();

		addComponent(getHeader());
		addComponent(getToolbar());

		CssLayout margin = new CssLayout();
		margin.setMargin(false, true, true, true);
		margin.setSizeFull();
		margin.addComponent(getPanels());
		addComponent(margin);
		setExpandRatio(margin, 1);
	}

	private Layout getPanels() {
		if (panels == null) {
			panels = new HorizontalLayout();
			panels.setSpacing(true);
			panels.setSizeFull();
			panels.addComponent(getNewsPanel());
			panels.addComponent(getGlueLayout());
			panels.addComponent(getPeoplePanel());
			panels.setExpandRatio(getNewsPanel(), 1.0f);
			panels.setExpandRatio(getGlueLayout(), 0.5f);
			panels.setExpandRatio(getPeoplePanel(), 1.0f);
		}
		return panels;
	}

	private Component getGlueLayout() {
		if (glueLayout == null) {
			glueLayout = new VerticalLayout();
			glueLayout.setSizeFull();
		}
		return glueLayout;
	}

	private Layout getHeader() {
		if (header == null) {
			header = new HorizontalLayout();
			header.setWidth("100%");
			header.addComponent(getHeaderTitle());
			header.setExpandRatio(getHeaderTitle(), 1.0f);
		}
		return header;
	}

	private Panel getNewsPanel() {
		if (panelNews == null) {
			panelNews = new Panel("News");
			panelNews.setStyleName("news");
			panelNews.setSizeFull();
			panelNews
					.addComponent(new NewsItem(
							"This is a Vaadin example application running in a Equinox / Jetty 8.1 OSGi Http environment !!!"));
			panelNews.addComponent(new NewsItem(
					"Security is being handled by Apache Shiro framework."));
			panelNews
					.addComponent(new NewsItem(
							"And the coolest thing is that now you can use JBehave inside a OSGi environment."
									+ " You can test both headless Equinox servers as Visual RCP products."));
			panelNews
					.addComponent(new NewsItem(
							"Plus, you can use JBehave Web for doing integration tests on your Vaadin web application !!!"));

		}
		return panelNews;
	}

	private Panel getPeoplePanel() {
		if (panelPeople == null) {
			panelPeople = new Panel("People");
			panelPeople.setStyleName("people");
			panelPeople.setSizeFull();
			Label ll = new Label(
					"These are the users that you can use to enter in the application: ");
			ll.setStyleName(Reindeer.LABEL_H2);
			panelPeople.addComponent(ll);

			Table peopleTable = new Table();
			peopleTable.setEditable(false);
			peopleTable.setHeight("130px");
			peopleTable.setStyleName(Reindeer.TABLE_STRONG);
			peopleTable.addContainerProperty("ID", String.class, null);
			peopleTable.addContainerProperty("Password", String.class, null);
			peopleTable.addContainerProperty("Role", String.class, null);

			peopleTable.addItem(
					new Object[] { "admin", "demo", "Administrator" },
					new Integer(1));
			peopleTable.addItem(new Object[] { "dan", "demo", "Consultant" },
					new Integer(2));
			peopleTable.addItem(new Object[] { "cvgaviao", "demo", "Manager" },
					new Integer(3));

			panelPeople.addComponent(peopleTable);
		}
		return panelPeople;
	}

	private Layout getHeaderTitle() {
		if (headerTitle == null) {
			headerTitle = new HorizontalLayout();
			headerTitle.setWidth("100%");
			headerTitle.setMargin(true, true, false, true);
			headerTitle.setSpacing(false);

			CssLayout titleLayout = new CssLayout();
			H1 title = new H1("JBehave TaskWeb");
			titleLayout.addComponent(title);
			SmallText description = new SmallText(
					"A Vaadin OSGi Example. Click on Login button to call the login form.");
			description.setSizeUndefined();
			titleLayout.addComponent(description);

			headerTitle.addComponent(titleLayout);
		}
		return headerTitle;
	}

	private Layout getToolbar() {
		if (toolbar == null) {
			toolbar = new CssLayout();
			toolbar.setWidth("100%");
			toolbar.setMargin(false);
			toolbar.addStyleName("toolbar-invert");

			CssLayout right = new CssLayout();
			right.setSizeUndefined();
			right.addStyleName("right");
			toolbar.addComponent(right);

			loginButton = new Button("Log in",
					new TaskManagerApp.LoginListener());
			loginButton.setStyleName(Reindeer.BUTTON_DEFAULT);
			loginButton.setDebugId("loginButton");
			right.addComponent(loginButton);
			// toolbar.setComponentAlignment(loginButton,
			// Alignment.MIDDLE_RIGHT);
			helpButton = new Button("Help", new TaskManagerApp.HelpListener());
			helpButton.setStyleName(Reindeer.BUTTON_DEFAULT);
			helpButton.setDebugId("helpButton");
			right.addComponent(helpButton);
			// toolbar.setComponentAlignment(helpButton,
			// Alignment.MIDDLE_RIGHT);
		}
		return toolbar;
	}

	@SuppressWarnings("serial")
	public static class H1 extends Label {
		public H1(String caption) {
			super(caption);
			setSizeUndefined();
			setStyleName(Reindeer.LABEL_H1);
		}
	}

	@SuppressWarnings("serial")
	public static class H2 extends Label {
		public H2(String caption) {
			super(caption);
			setSizeUndefined();
			setStyleName(Reindeer.LABEL_H2);
		}
	}

	@SuppressWarnings("serial")
	public static class SmallText extends Label {
		public SmallText(String caption) {
			super(caption);
			setStyleName(Reindeer.LABEL_SMALL);
		}
	}

	@SuppressWarnings("serial")
	public static class NewsItem extends Label {
		public NewsItem(String caption) {
			super(caption);
			setStyleName("bubble");
		}
	}
}
