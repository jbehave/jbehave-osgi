package org.jbehave.osgi.equinox.commands;

import org.junit.Test;
import org.osgi.framework.Bundle;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.jbehave.osgi.services.EmbedderService;

public class CoreCommandProviderTest {

	@Test
	public void ensureCommandServiceIsRegistered()
	{
		// assert bundle is not null
		Bundle bundle = FrameworkUtil.getBundle(CoreCommandProvider.class);
		notNullValue().matches(bundle);

		// asserts that test bundle is installed
		BundleContext bundleContextFromFrameworkUtil = FrameworkUtil.getBundle(CoreCommandProvider.class)
                .getBundleContext();
		notNullValue().matches(bundleContextFromFrameworkUtil);

		CoreCommandProvider commandProvider = (CoreCommandProvider) bundle;
		// assert that injected bundleContext is same that Framework one. 
		equalTo(bundleContextFromFrameworkUtil).matches(commandProvider.getInjectedBundleContext());
	}
	
	@Test
	public void ensureJbehaveServiceIsInjected()
	{
	    // asserts that test bundle is resolved
		Bundle bundle = FrameworkUtil.getBundle(CoreCommandProvider.class);
		notNullValue().matches(bundle);

		CoreCommandProvider commandProvider = (CoreCommandProvider) bundle;
		// assert that injected bundleContext is same that Framework one. 
		notNullValue(EmbedderService.class).matches(commandProvider.getEmbedderService());
	    

	}
	
}
