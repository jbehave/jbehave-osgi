package org.jbehave.osgi.equinox.commands.test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.jbehave.osgi.equinox.commands.CoreCommandProvider;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

public class CoreCommandProviderTest {

	private CoreCommandProvider commandProvider;

	@Test
	public void ensureCommandServiceIsRegistered() {
		// assert bundle is not null
		Bundle bundle = FrameworkUtil.getBundle(CoreCommandProvider.class);
		assertThat("CoreCommand bundle is not installed", bundle,
				is(notNullValue()));
		
		// get the context 
		BundleContext bundleContext = bundle.getBundleContext();
		assertThat("CoreCommand bundle context is null", bundle,
				is(notNullValue()));
		
		ServiceReference<CoreCommandProvider> serviceReference = bundleContext.getServiceReference(CoreCommandProvider.class);
		assertThat("ServiceReference is null", serviceReference,
				is(notNullValue()));

		commandProvider = bundleContext.getService(serviceReference);
		
		// asserts that test bundle is installed
		assertThat("CoreCommand was not inject by Blueprint DI", commandProvider,
				is(notNullValue()));
	
	}

	@Test
	public void ensureJbehaveServiceIsInjected() {

		// assert that injected bundleContext is same that Framework one.
		assertThat("EmbedderService was not injected by Blueprint DI",
				commandProvider.getEmbedderService(), is(notNullValue()));

	}
}
