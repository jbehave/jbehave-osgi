package org.jbehave.osgi.equinox.commands;

import org.junit.Test;
import org.osgi.framework.Bundle;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.jbehave.osgi.services.EmbedderService;

public class CoreCommandProviderTest {

	@Test
	public void ensureCommandServiceIsRegistered() {
		// assert bundle is not null
		Bundle bundle = FrameworkUtil.getBundle(CoreCommandProvider.class);
		assertThat("CoreCommand bundle is not installed", bundle,
				is(notNullValue()));

		// asserts that test bundle is installed
		BundleContext bundleContextFromFrameworkUtil = FrameworkUtil.getBundle(
				CoreCommandProvider.class).getBundleContext();
		assertThat("BundleContext is null", bundleContextFromFrameworkUtil, is(notNullValue()));

		// CoreCommandProvider commandProvider = (CoreCommandProvider) bundle;
		// // assert that injected bundleContext is same that Framework one.
		// equalTo(bundleContextFromFrameworkUtil).matches(commandProvider.getInjectedBundleContext());
	}

	@Test
	public void ensureJbehaveServiceIsInjected() {
		// asserts that test bundle is resolved
		Bundle bundle = FrameworkUtil.getBundle(CoreCommandProvider.class);
		assertThat("CoreCommand bundle is not installed", bundle,
				is(notNullValue()));

		CoreCommandProvider commandProvider = bundle
				.adapt(CoreCommandProvider.class);
		assertThat("CoreCommandProvider is not adaptable to bundle",commandProvider, is(notNullValue()));

		// assert that injected bundleContext is same that Framework one.
		assertThat("EmbedderService was not injected", commandProvider.getEmbedderService(), is(notNullValue()));

	}

}
