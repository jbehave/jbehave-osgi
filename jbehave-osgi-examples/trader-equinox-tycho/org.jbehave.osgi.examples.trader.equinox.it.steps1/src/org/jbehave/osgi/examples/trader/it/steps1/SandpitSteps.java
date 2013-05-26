package org.jbehave.osgi.examples.trader.it.steps1;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.steps.Steps;
import org.junit.Assert;

public class SandpitSteps extends Steps {

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