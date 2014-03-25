package org.jbehave.osgi.paxexam.junit;

import static org.ops4j.pax.exam.CoreOptions.frameworkProperty;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.url;
import static org.ops4j.pax.exam.CoreOptions.when;
import static org.ops4j.pax.exam.CoreOptions.cleanCaches;

import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.options.CompositeOption;
import org.ops4j.pax.exam.options.DefaultCompositeOption;

public class ProbeOptions {

    /**
     * This is the base download URL for LUNA
     */
    public final static String EQUINOX_LUNA_BASE_DOWNLOAD_URL = "http://download.eclipse.org/eclipse/updates/4.4milestones/S-4.4M6-201403061200/plugins/";

    /**
     * This is the base download URL for LUNA
     */
    public final static String EQUINOX_KEPLER_BASE_DOWNLOAD_URL = "http://download.eclipse.org/eclipse/updates/4.3/R-4.3.1-201309111000/plugins/";

    /**
     * this is the eclipse orbit repository
     */
    public final static String ORBIT_BASE_DOWNLOAD_URL = "http://download.eclipse.org/tools/orbit/downloads/drops/S20140227085123/repository/plugins/";

    /**
     * Property that should be used to specify which OSGi framework to use.
     * <p>
     * Currently the options are: equinox_kepler, equinox_luna, felix.
     */
    public final static String PAXEXAM_FRAMEWORK_PROPERTY = "pax.exam.framework";

    public ProbeOptions() {
    }

    public static boolean isEquinoxLuna() {
        return "equinox_luna".equals(System
                .getProperty(PAXEXAM_FRAMEWORK_PROPERTY));
    }

    public static boolean isEquinoxKepler() {
        return "equinox_kepler".equals(System
                .getProperty(PAXEXAM_FRAMEWORK_PROPERTY));
    }

    public static boolean isFelix() {
        return "felix".equals(System.getProperty(PAXEXAM_FRAMEWORK_PROPERTY));
    }

    /**
     * A collection of PaxExam configuration options that will install all
     * JBehave OSGi core dependencies into the OSGi container.
     * 
     * @return a {@link CompositeOption} for the JBehave dependencies without
     *         clear the caches.
     */
    public static CompositeOption jbehaveDeps() {
        return jbehaveCoreDependencies(false);
    }

    /**
     * A collection of PaxExam configuration options that will install all
     * JBehave OSGi core dependencies into the OSGi container.
     * 
     * @param cleanCaches
     *            determine if the caches must be clean.
     * @return a {@link CompositeOption} for the JBehave dependencies.
     */
    public static CompositeOption jbehaveCoreDependencies(boolean cleanCaches) {
        return new DefaultCompositeOption(
                when(cleanCaches).useOptions(cleanCaches()),

                when(isEquinoxLuna())
                        .useOptions(
                                url(EQUINOX_LUNA_BASE_DOWNLOAD_URL
                                        + "org.eclipse.osgi.services_3.4.0.v20131120-1328.jar"),
                                url(EQUINOX_LUNA_BASE_DOWNLOAD_URL
                                        + "org.eclipse.equinox.cm_1.1.0.v20131021-1936.jar"),
                                url(EQUINOX_LUNA_BASE_DOWNLOAD_URL
                                        + "org.eclipse.equinox.common_3.6.200.v20130402-1505.jar"),
                                url(EQUINOX_LUNA_BASE_DOWNLOAD_URL
                                        + "org.eclipse.equinox.ds_1.4.200.v20131126-2331.jar"),
                                url(EQUINOX_LUNA_BASE_DOWNLOAD_URL
                                        + "org.eclipse.equinox.console_1.1.0.v20140131-1639.jar"),
                                url(EQUINOX_LUNA_BASE_DOWNLOAD_URL
                                        + "org.eclipse.equinox.event_1.3.100.v20140115-1647.jar"),
                                url(EQUINOX_LUNA_BASE_DOWNLOAD_URL
                                        + "org.eclipse.equinox.util_1.0.500.v20130404-1337.jar")),
                when(isEquinoxKepler())
                        .useOptions(
                                url(EQUINOX_KEPLER_BASE_DOWNLOAD_URL
                                        + "org.eclipse.osgi.services_3.3.100.v20130513-1956.jar"),
                                url(EQUINOX_KEPLER_BASE_DOWNLOAD_URL
                                        + "org.eclipse.equinox.cm_1.0.400.v20130327-1442.jar").startLevel(1),
                                url(EQUINOX_KEPLER_BASE_DOWNLOAD_URL
                                        + "org.eclipse.equinox.common_3.6.200.v20130402-1505.jar"),
                                url(EQUINOX_KEPLER_BASE_DOWNLOAD_URL
                                        + "org.eclipse.equinox.ds_1.4.101.v20130813-1853.jar").startLevel(1),
                                url(EQUINOX_KEPLER_BASE_DOWNLOAD_URL
                                        + "org.eclipse.equinox.console_1.0.100.v20130429-0953.jar"),
                                url(EQUINOX_KEPLER_BASE_DOWNLOAD_URL
                                        + "org.eclipse.equinox.event_1.3.0.v20130327-1442.jar"),
                                url(EQUINOX_KEPLER_BASE_DOWNLOAD_URL
                                        + "org.eclipse.equinox.util_1.0.500.v20130404-1337.jar")),
                when(isFelix()).useOptions(
                        mavenBundle("org.apache.felix",
                                "org.apache.felix.configadmin", "1.8.0")
                                .startLevel(1),
                        mavenBundle("org.apache.felix",
                                "org.apache.felix.eventadmin", "1.3.2")
                                .startLevel(2),
                        mavenBundle("org.apache.felix",
                                "org.apache.felix.prefs", "1.0.6")
                                .startLevel(2),
                        mavenBundle("org.apache.felix",
                                "org.apache.felix.log", "1.0.1")
                                .startLevel(2),
                        mavenBundle("org.apache.felix", "org.apache.felix.scr", "1.8.2")
                                .startLevel(1)),

                frameworkProperty("org.osgi.framework.system.packages.extra")
                        .value("org.ops4j.pax.exam;version=3.4.0,org.ops4j.pax.exam.options;version=3.4.0,org.ops4j.pax.exam.util;version=3.4.0,org.w3c.dom.traversal"),
                url(ORBIT_BASE_DOWNLOAD_URL
                        + "org.junit_4.11.0.v201303080030.jar"),
                url(ORBIT_BASE_DOWNLOAD_URL
                        + "org.hamcrest.integration_1.3.0.v201305210900.jar"),
                url(ORBIT_BASE_DOWNLOAD_URL
                        + "org.hamcrest.library_1.3.0.v201305281000.jar"),
                url(ORBIT_BASE_DOWNLOAD_URL
                        + "org.hamcrest.core_1.3.0.v201303031735.jar"),
                mavenBundle("org.knowhowlab.osgi",
                        "org.knowhowlab.osgi.testing.utils", "1.2.2"),
                mavenBundle("org.knowhowlab.osgi",
                        "org.knowhowlab.osgi.testing.assertions", "1.2.2"),
                mavenBundle("org.apache.servicemix.bundles",
                        "org.apache.servicemix.bundles.xstream", "1.4.4_2"),
                mavenBundle("org.apache.servicemix.bundles",
                        "org.apache.servicemix.bundles.xpp3", "1.1.4c_6"),
                mavenBundle("org.apache.servicemix.bundles",
                        "org.apache.servicemix.bundles.xmlpull", "1.1.3.1_2"),
                mavenBundle("org.apache.servicemix.bundles",
                        "org.apache.servicemix.bundles.woodstox", "3.2.9_3"),
                mavenBundle("org.apache.servicemix.bundles",
                        "org.apache.servicemix.bundles.freemarker", "2.3.20_1"),
                mavenBundle("commons-lang", "commons-lang", "2.6"),
                mavenBundle("org.apache.felix",
                        "org.apache.felix.gogo.runtime", "0.10.0"),
                mavenBundle("commons-collections",
                        "commons-collections", "3.2.1"), 
                mavenBundle(
                        "commons-io", "commons-io", "2.4"), 
                mavenBundle(
                        "org.apache.servicemix.bundles",
                        "org.apache.servicemix.bundles.paranamer", "2.4_1"),
                mavenBundle("com.google.guava", "guava", "16.0.1"));
    }

    public static CompositeOption jbehaveCoreAndDependencies() {
        return jbehaveCoreAndDependencies(true);
    }

    public static CompositeOption jbehaveCoreAndDependencies(boolean cleanCache) {
        return new DefaultCompositeOption(jbehaveCoreDependencies(cleanCache),
                mavenBundle("org.jbehave.osgi", "org.jbehave.osgi.core")
                        .versionAsInProject());
    }

    public static Option simpleOSGiLogging() {
        return mavenBundle("org.jbehave.osgi", "org.jbehave.osgi.logging")
                .versionAsInProject();
    }
}
