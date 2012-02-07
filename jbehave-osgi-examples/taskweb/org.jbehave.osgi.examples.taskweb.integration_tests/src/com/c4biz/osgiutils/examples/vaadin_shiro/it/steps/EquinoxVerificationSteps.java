package com.c4biz.osgiutils.examples.vaadin_shiro.it.steps;

import java.util.Map;

import org.jbehave.core.annotations.BeforeStories;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.model.ExamplesTable;
import org.junit.Assert;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.eclipse.core.runtime.adaptor.EclipseStarter;
import com.c4biz.osgiutils.assertions.bundles.BundleAssert;
import com.c4biz.osgiutils.assertions.services.ServiceAssert;

@SuppressWarnings("restriction")
public class EquinoxVerificationSteps {
	
	private BundleContext bc;
	
	@BeforeStories
	public void setup()
	{
		bc = FrameworkUtil.getBundle(EquinoxVerificationSteps.class)
				.getBundleContext();
		ServiceAssert.init(bc);
		BundleAssert.init(bc);
	}

	@Given("equinox server is running")
	public void givenEquinoxServerWasStarted() {
		Assert.assertTrue(EclipseStarter.isRunning());
	}

	@Then("all needed bundles are active: %table")
	public void ensureNeededBundlesAreActive(ExamplesTable table) {

		for (Map<String, String> row : table.getRows()) {
			String bundleName = row.get("bundleName");
			BundleAssert.assertBundleAvailable(bundleName
					+ " bundle is not active.", bundleName);
		}
	}

	@Then("all needed services are registered: %table")
	public void ensureNeededServicesAreRegistered(ExamplesTable table) {

		for (Map<String, String> row : table.getRows()) {
			String serviceName = row.get("serviceName");
			ServiceAssert.assertServiceAvailable(serviceName
					+ " service is not registered.", serviceName);
		}
	}
}
