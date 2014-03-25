package org.jbehave.osgi.core.itests;

import static org.knowhowlab.osgi.testing.assertions.BundleAssert.assertBundleAvailable;
import static org.knowhowlab.osgi.testing.assertions.BundleAssert.assertBundleState;
import static org.knowhowlab.osgi.testing.assertions.ServiceAssert.assertServiceAvailable;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.junit.Before;
import org.knowhowlab.osgi.testing.assertions.BundleAssert;
import org.knowhowlab.osgi.testing.assertions.OSGiAssert;
import org.knowhowlab.osgi.testing.assertions.ServiceAssert;
import org.knowhowlab.osgi.testing.assertions.cmpn.ConfigurationAdminAssert;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.Version;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.log.LogService;

public abstract class AbstractCommonTest {

    /**
     * Injected BundleContext
     */
    @Inject
    protected BundleContext bc;

    @Before
    public void init() throws Exception {
        OSGiAssert.setDefaultBundleContext(bc);
        ServiceAssert.setDefaultBundleContext(bc);
        BundleAssert.setDefaultBundleContext(bc);
        ConfigurationAdminAssert.setDefaultBundleContext(bc);

        ensureCompendiumServicesAreFunctional();
        ensureJBehaveBundlesAreActive();
    }

    public void ensureJBehaveBundlesAreActive() {

        assertBundleAvailable("org.knowhowlab.osgi.testing.utils", new Version(
                "1.2.2"));
        assertBundleAvailable("org.jbehave.osgi.core");
        assertBundleState(Bundle.ACTIVE, "org.knowhowlab.osgi.testing.utils",
                5, TimeUnit.SECONDS);
        assertBundleState(Bundle.ACTIVE, "org.hamcrest.integration", Version.parseVersion("1.3.0"));
        assertBundleState(Bundle.ACTIVE, "org.apache.commons.io");
        assertBundleState(Bundle.ACTIVE, "org.apache.commons.collections");
        assertBundleState(Bundle.ACTIVE, "com.google.guava");
        assertBundleState(Bundle.ACTIVE, "org.jbehave.osgi.core");
        assertBundleState(Bundle.ACTIVE, "org.apache.commons.lang");
        assertBundleState(Bundle.ACTIVE, "javax.xml.stream");
        assertBundleState(Bundle.ACTIVE, "javax.inject");
        assertBundleState(Bundle.ACTIVE,
                "org.apache.servicemix.bundles.xmlpull");
        assertBundleState(Bundle.ACTIVE, "org.apache.servicemix.bundles.xpp3");
        assertBundleState(Bundle.ACTIVE, "javax.xml");
        assertBundleState(Bundle.ACTIVE,
                "org.apache.servicemix.bundles.xstream");
        assertBundleState(Bundle.ACTIVE,
                "org.apache.servicemix.bundles.paranamer");
        assertBundleState(Bundle.ACTIVE,
                "org.apache.servicemix.bundles.freemarker");
        assertBundleState(Bundle.ACTIVE, "slf4j.api");
    }

    public void ensureCompendiumServicesAreFunctional()
            throws InvalidSyntaxException {
        assertServiceAvailable(EventAdmin.class);
        assertServiceAvailable(ConfigurationAdmin.class);
        assertServiceAvailable(LogService.class);
    }
}
