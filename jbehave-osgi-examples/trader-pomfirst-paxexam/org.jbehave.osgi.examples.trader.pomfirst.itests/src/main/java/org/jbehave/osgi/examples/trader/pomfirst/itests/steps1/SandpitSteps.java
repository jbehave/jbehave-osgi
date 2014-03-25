package org.jbehave.osgi.examples.trader.pomfirst.itests.steps1;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.steps.Steps;
import org.jbehave.osgi.core.configuration.ConfigurationOsgi;
import org.junit.Assert;
import org.osgi.framework.Bundle;

public class SandpitSteps extends Steps {
	
	public SandpitSteps(Bundle ownerBundle) {
		super(new ConfigurationOsgi(ownerBundle));
	}

	@Given("I do nothing")
	public void doNothing() {
	}

	@Then("I fail")
	public void doFail() {
		Assert.fail("I failed!");
	}

	@Then("I pass")
	public void doPass() {
	}
}