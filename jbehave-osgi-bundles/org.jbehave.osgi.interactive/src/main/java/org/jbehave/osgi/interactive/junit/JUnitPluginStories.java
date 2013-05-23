package org.jbehave.osgi.interactive.junit;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.jbehave.osgi.interactive.services.StoryRunnerService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * <p>
 * JUnit-Plugin runnable entry-point to run multiple OSGi stories.
 * 
 * @author Cristiano Gavião
 */
public abstract class JUnitPluginStories {

	private StoryRunnerService storyRunnerService;
	protected BundleContext bc;
	private ServiceTracker<StoryRunnerService, StoryRunnerService> tracker;
	private Filter filter;
	private long timeOutInSeconds = 3; // 3 seconds

	@Before
	public void setupServices() {
		bc = FrameworkUtil.getBundle(getClass()).getBundleContext();
		final CountDownLatch latch = new CountDownLatch(1);
		try {
			filter = FrameworkUtil
					.createFilter("(objectClass=org.jbehave.osgi.interactive.services.StoryRunnerService)");
		} catch (InvalidSyntaxException e1) {
			Assert.fail("Could not create a filter for JBehave Embedder Service");
		}

		tracker = new ServiceTracker<StoryRunnerService, StoryRunnerService>(
				bc,
				filter,
				new ServiceTrackerCustomizer<StoryRunnerService, StoryRunnerService>() {

					@Override
					public StoryRunnerService addingService(
							ServiceReference<StoryRunnerService> serviceReference) {
						try {
							return bc.getService(serviceReference);
						} finally {
							latch.countDown();
						}
					}

					@Override
					public void modifiedService(
							ServiceReference<StoryRunnerService> reference,
							StoryRunnerService service) {
					}

					@Override
					public void removedService(
							ServiceReference<StoryRunnerService> reference,
							StoryRunnerService service) {
					}
				});

		tracker.open();

		try {
			storyRunnerService = tracker.waitForService(timeOutInSeconds);
		} catch (InterruptedException e) {
			storyRunnerService = null;
			Assert.fail("Could not find registered JBehave Embedder Service");
		} finally {
			tracker.close();
		}
	}

	@After
	public void releaseResources() {
		tracker.close();
		tracker = null;
		storyRunnerService = null;
	}

	@Test
	public void run() throws Throwable {
		try {
			
//			TODO storyRunnerService.runStoriesAsPaths(storyPaths());
		} finally {
//			storyRunnerService.generateCrossReference();
		}
	}

	protected abstract List<String> storyPaths();
}
