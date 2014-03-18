package org.jbehave.osgi.equinox.commands.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.jbehave.osgi.core.commands.DefaultCommandsComponent;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

public class CoreCommandProviderTest {

    private DefaultCommandsComponent commandProvider;

    @Test
    public void ensureCommandServiceIsRegistered() {
        // assert bundle is not null
        Bundle bundle = FrameworkUtil.getBundle(DefaultCommandsComponent.class);
        assertThat("org.jbehave.osgi.equinox.commands bundle is not installed",
                bundle, is(notNullValue()));

        // // get the context
        BundleContext bundleContext = bundle.getBundleContext();
        assertThat("CoreCommand bundle context is null", bundleContext,
                is(notNullValue()));

        ServiceReference<DefaultCommandsComponent> serviceReference = bundleContext
                .getServiceReference(DefaultCommandsComponent.class);
        assertThat("ServiceReference is null", serviceReference,
                is(notNullValue()));
        commandProvider = bundleContext.getService(serviceReference);

        // asserts that test service was registered
        assertThat("DefaultCommandsComponent was not registered properly.",
                commandProvider, is(notNullValue()));

    }
}
