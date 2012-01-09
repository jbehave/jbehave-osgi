package org.jbehave.osgi.examples.rcpmail.stories.steps;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.jbehave.core.annotations.AfterStories;
import org.jbehave.core.annotations.BeforeStories;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

public class RCPmailCoreSteps {

	private SWTWorkbenchBot bot = null;
	private int currentViewCount = 0;

	@BeforeStories
	public void beforeStories() throws Exception {
		bot = new SWTWorkbenchBot();
	}

	@Given("JBehave RCP Mail application is running")
	public void certifyRCPmailIsRunning() {

		//TODO waiting for swtbot list answer question about how to test application is on
		currentViewCount = viewCount();
		assertThat(currentViewCount, is(2));
	}

	@When("user chooses menu item \"Open Another Message View\"")
	public void whenUserChoosesMenuItem() throws Exception {
		
		bot.menu("File").menu("Open Another Message View").click();
		
	}

	@Then("application shows another message view")
	public void thenEnsureMessageViewIsVisible() {

//		bot.viewById("Message1");
		assertEquals(3, viewCount());
	}
	
	private int viewCount() throws WidgetNotFoundException {
		return bot.views().size();
	}

	@AfterStories
	public void sleep() {
		bot.sleep(2000);
	}

}
