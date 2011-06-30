package org.jbehave.osgi.examples.tycho.rcpmail.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class MyFirstTest {

	private static SWTWorkbenchBot bot;

	@BeforeClass
	public static void beforeClass() throws Exception {
		bot = new SWTWorkbenchBot();

	}

	
	@Test
	public void CreatesAnotherMessageWindow() throws Exception {
		assertEquals(2, viewCount());
		bot.menu("File").menu("Open Another Message View").click();
		
		assertEquals(3, viewCount());
	}

	
	@Test
	public void ClosesAllMessageWindows() throws Exception {
		bot.viewByTitle("Message").close();
		bot.viewByTitle("Message1").close();

		assertEquals(1, viewCount());
	}

	@Test
	public void MyMailBoxContainsDrafts() throws Exception {
		SWTBotTree mailbox = mailBox();
		SWTBotTreeItem myMailBox = mailbox.expandNode("me@this.com");
		assertTrue(myMailBox.getNodes().contains("Drafts"));
	}

	@Test
	public void OtherMailBoxContainsDrafts() throws Exception {
		SWTBotTree mailbox = mailBox();
		SWTBotTreeItem otherMailBox = mailbox.expandNode("other@aol.com");
		assertThat(otherMailBox, is(notNullValue()));

		String node = otherMailBox.getNodes().get(0);
		assertThat(node, is("Inbox"));
	}

	private SWTBotTree mailBox() throws WidgetNotFoundException {
		// find the tree
		return bot.viewByTitle("Mailboxes").bot().tree();
	}

	private int viewCount() throws WidgetNotFoundException {
		return bot.views().size();
	}

//	@Test
//	public void testApplicationWindow() throws Exception {
//		assertThat("Shell is null", bot.shell("RCP Product"),
//				is(notNullValue()));
//	}


	@Test
	public void testOpenMessage() throws Exception {
		SWTBotMenu file = bot.menu("File").menu("Open Message");
		file.click();
		bot.shell("Open").bot().button("OK").click();
	}


	@AfterClass
	public static void sleep() {
		bot.sleep(2000);
	}

}
