package org.jbehave.osgi.equinox.commands.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.apache.aries.blueprint.annotation.Bean;
import org.apache.aries.blueprint.annotation.Inject;
import org.jbehave.osgi.equinox.commands.CoreCommandProvider;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

@Bean(id="CoreCommandProviderTest")
public class CoreCommandProviderTest {

	@Inject(ref="commandProviderImpl")
	private CoreCommandProvider commandProvider;

	@Test
	public void ensureCommandServiceIsRegistered() {
		// assert bundle is not null
		Bundle bundle = FrameworkUtil.getBundle(CoreCommandProvider.class);
		assertThat("CoreCommand bundle is not installed", bundle,
				is(notNullValue()));

		// asserts that test bundle is installed
		assertThat("CoreCommand was not inject by Blueprint DI", commandProvider,
				is(notNullValue()));

		
	}

	@Test
	public void ensureJbehaveServiceIsInjected() {
		// asserts that test bundle is resolved
		assertThat("CoreCommand was not inject by Blueprint DI", commandProvider,
				is(notNullValue()));

		// assert that injected bundleContext is same that Framework one.
		assertThat("EmbedderService was not injected by Blueprint DI",
				commandProvider.getEmbedderService(), is(notNullValue()));

	}

	public CoreCommandProvider getCommandProvider() {
		return commandProvider;
	}

	public void setCommandProvider(CoreCommandProvider commandProvider) {
		this.commandProvider = commandProvider;
	}
}
