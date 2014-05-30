package org.jbehave.osgi.core.itests;

import static org.jbehave.osgi.core.Constants.STEP_FACTORY_EXTENDER_MANIFEST_HEADER;
import static org.jbehave.osgi.core.Constants.STORY_RUNNER_EXTENDER_MANIFEST_HEADER;
import static org.knowhowlab.osgi.testing.assertions.BundleAssert.assertBundleAvailable;
import static org.knowhowlab.osgi.testing.assertions.BundleAssert.assertBundleState;
import static org.knowhowlab.osgi.testing.assertions.ServiceAssert.assertServiceAvailable;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.streamBundle;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;
import static org.osgi.framework.Constants.BUNDLE_SYMBOLICNAME;
import static org.osgi.framework.Constants.BUNDLE_VERSION;
import static org.osgi.framework.Constants.EXPORT_PACKAGE;
import static org.osgi.framework.Constants.IMPORT_PACKAGE;
import static org.osgi.framework.Constants.REQUIRE_BUNDLE;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.jbehave.osgi.core.itests.lib.embedders.AnnotatedPathRunnerOsgiExample;
import org.jbehave.osgi.core.itests.lib.steps.NamedParametersSteps;
import org.jbehave.osgi.core.itests.lib.steps.SearchSteps;
import org.jbehave.osgi.paxexam.ProbeOptions;
import org.junit.Before;
import org.knowhowlab.osgi.testing.assertions.BundleAssert;
import org.knowhowlab.osgi.testing.assertions.OSGiAssert;
import org.knowhowlab.osgi.testing.assertions.ServiceAssert;
import org.knowhowlab.osgi.testing.assertions.cmpn.ConfigurationAdminAssert;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.ProbeBuilder;
import org.ops4j.pax.exam.TestProbeBuilder;
import org.ops4j.pax.exam.options.UrlProvisionOption;
import org.ops4j.pax.tinybundles.core.TinyBundles;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.Version;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.log.LogService;

public abstract class AbstractCommonIntegrationTest {

	/**
	 * Injected BundleContext
	 */
	@Inject
	protected BundleContext bc;

	protected UrlProvisionOption buildEmbedderExtendeeBundle() {
		return streamBundle(TinyBundles
				.bundle()
				.add(AnnotatedPathRunnerOsgiExample.class)
				.set(EXPORT_PACKAGE,
						"org.jbehave.osgi.core.itests.lib.embedders")
				.set(BUNDLE_SYMBOLICNAME,
						"org.jbehave.osgi.core.itests.embedders")
				.set(BUNDLE_VERSION, "1.0.0")
				.set(STORY_RUNNER_EXTENDER_MANIFEST_HEADER,
						"org.jbehave.osgi.core.itests.lib.embedders.AnnotatedPathRunnerOsgiExample")
				.set(REQUIRE_BUNDLE,
						"org.jbehave.osgi.core,org.hamcrest.integration;bundle-version=\"1.3.0\"")
				.set(IMPORT_PACKAGE,
						"org.jbehave.osgi.core.itests.lib.steps,"
								+ "org.apache.commons.lang;version=\"[2.6,3)\","
								+ "org.junit,org.osgi.framework,"
								+ "com.thoughtworks.paranamer")
				.build(TinyBundles.withClassicBuilder()));
	}

	protected UrlProvisionOption buildInjectableStepsFactoryExtendeeBundle() {
		return streamBundle(TinyBundles
				.bundle()
				.add(SearchSteps.class)
				.add(NamedParametersSteps.class)
				.set(REQUIRE_BUNDLE,
						"org.jbehave.osgi.core,org.hamcrest.integration;bundle-version=\"1.3.0\"")
				.set(IMPORT_PACKAGE,
						"org.apache.commons.lang;version=\"[2.6,3)\","
								+ "org.junit,"
								+ "org.osgi.framework,com.thoughtworks.paranamer")
				.set(EXPORT_PACKAGE, "org.jbehave.osgi.core.itests.lib.steps")
				.set(BUNDLE_SYMBOLICNAME, "org.jbehave.osgi.core.itests.steps")
				.set(BUNDLE_VERSION, "1.0.0")
				.set(STEP_FACTORY_EXTENDER_MANIFEST_HEADER,
						"stepFactory1:org.jbehave.osgi.core.itests.lib.steps.*")
				.build(TinyBundles.withClassicBuilder()));
	}

	@Configuration
	public Option[] configureIntegrationTests() {

		return options(
				// systemProperty("osgi.console").value("8885"),
				systemProperty("eclipse.consoleLog").value("true"),
				systemProperty("eclipse.log.level").value("DEBUG"),
				ProbeOptions.consoleOSGiLogging().startLevel(1),
				ProbeOptions.jbehaveCoreAndDependencies(),
				buildInjectableStepsFactoryExtendeeBundle().startLevel(4),
				buildEmbedderExtendeeBundle().startLevel(4));
	}

	public void ensureCompendiumServicesAreFunctional()
			throws InvalidSyntaxException {
		assertServiceAvailable(EventAdmin.class);
		assertServiceAvailable(ConfigurationAdmin.class);
		assertServiceAvailable(LogService.class);
	}

	public void ensureJBehaveBundlesAreActive() {

		assertBundleAvailable("org.knowhowlab.osgi.testing.utils", new Version(
				"1.2.2"));
		assertBundleAvailable("org.jbehave.osgi.core");
		assertBundleState(Bundle.ACTIVE, "org.knowhowlab.osgi.testing.utils",
				5, TimeUnit.SECONDS);
		assertBundleState(Bundle.ACTIVE, "org.apache.commons.io");
		assertBundleState(Bundle.ACTIVE, "org.apache.commons.collections");
		assertBundleState(Bundle.ACTIVE, "org.apache.commons.lang");
		assertBundleState(Bundle.ACTIVE, "com.google.guava");
		assertBundleState(Bundle.ACTIVE, "org.jbehave.osgi.core");
		assertBundleState(Bundle.ACTIVE, "org.apache.felix.gogo.runtime");
		assertBundleState(Bundle.ACTIVE,
				"org.apache.servicemix.bundles.woodstox");
		assertBundleState(Bundle.ACTIVE,
				"org.apache.servicemix.bundles.xmlpull");
		assertBundleState(Bundle.ACTIVE, "org.apache.servicemix.bundles.xpp3");
		assertBundleState(Bundle.ACTIVE,
				"org.apache.servicemix.bundles.xstream");
		assertBundleState(Bundle.ACTIVE,
				"org.apache.servicemix.bundles.paranamer");
		assertBundleState(Bundle.ACTIVE,
				"org.apache.servicemix.bundles.freemarker");
		assertBundleState(Bundle.ACTIVE, "org.jbehave.osgi.core.itests.steps");
		assertBundleState(Bundle.ACTIVE,
				"org.jbehave.osgi.core.itests.embedders");
	}

	@Before
	public void init() throws Exception {
		OSGiAssert.setDefaultBundleContext(bc);
		ServiceAssert.setDefaultBundleContext(bc);
		BundleAssert.setDefaultBundleContext(bc);
		ConfigurationAdminAssert.setDefaultBundleContext(bc);

		ensureCompendiumServicesAreFunctional();
		ensureJBehaveBundlesAreActive();
	}

	@ProbeBuilder
	public TestProbeBuilder probeConfiguration(TestProbeBuilder probe) {
		probe.setHeader(Constants.REQUIRE_BUNDLE, "org.jbehave.osgi.core,"
				+ "org.jbehave.osgi.core.itests.steps,"
				+ "org.jbehave.osgi.core.itests.embedders");
		probe.setHeader(Constants.BUNDLE_SYMBOLICNAME, "JBEHAVE.ITEST.BUNDLE");
		return probe;
	}
}
