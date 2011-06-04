package org.jbehave.osgi.equinox.commands;

import org.junit.Test;
import org.osgi.framework.Bundle;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

public class CoreCommandProviderTest {

	@Test
	public void ensureCommandServiceIsRegistered()
	{
		// asserts that test bundle is installed
		BundleContext bundleContext = FrameworkUtil.getBundle(CoreCommandProvider.class)
                .getBundleContext();
		
		Bundle bundle = FrameworkUtil.getBundle(CoreCommandProvider.class);
		notNullValue().matches(bundle);
		
		
		equalTo(bundleContext).matches(CoreCommandProvider.getBundleContext());
	}
	
	@Test
	public void ensureJbehaveServiceIsInjected()
	{
	    // asserts that test bundle is resolved
	    

	}
	
}
