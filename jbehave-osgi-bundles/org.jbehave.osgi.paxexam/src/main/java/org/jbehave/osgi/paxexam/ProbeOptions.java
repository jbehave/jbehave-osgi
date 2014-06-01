package org.jbehave.osgi.paxexam;

import static org.ops4j.pax.exam.CoreOptions.bundle;
import static org.ops4j.pax.exam.CoreOptions.cleanCaches;
import static org.ops4j.pax.exam.CoreOptions.frameworkProperty;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.streamBundle;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;
import static org.ops4j.pax.exam.CoreOptions.when;
import static org.osgi.framework.Constants.BUNDLE_ACTIVATIONPOLICY;
import static org.osgi.framework.Constants.BUNDLE_SYMBOLICNAME;
import static org.osgi.framework.Constants.BUNDLE_VERSION;
import static org.osgi.framework.Constants.EXPORT_PACKAGE;
import static org.osgi.framework.Constants.IMPORT_PACKAGE;

import org.jbehave.osgi.paxexam.junit.AbstractPaxExamForStoryRunner;
import org.ops4j.pax.exam.options.CompositeOption;
import org.ops4j.pax.exam.options.DefaultCompositeOption;
import org.ops4j.pax.exam.options.UrlProvisionOption;
import org.ops4j.pax.exam.options.libraries.JUnitBundlesOption;
import org.ops4j.pax.tinybundles.core.TinyBundles;

public class ProbeOptions {

	private static UrlProvisionOption buildPaxExamExtensionBundle() {
		return streamBundle(TinyBundles
				.bundle()
				.add(AbstractPaxExamForStoryRunner.class)
				.set(BUNDLE_ACTIVATIONPOLICY, "lazy")
				.set(EXPORT_PACKAGE, "org.jbehave.osgi.paxexam.junit")
				.set(BUNDLE_SYMBOLICNAME, "org.jbehave.osgi.paxexam.junit")
				.set(BUNDLE_VERSION, "1.0.0")
				.set(IMPORT_PACKAGE,
						"javax.inject,org.jbehave.osgi.core.annotations;version=\"[1.0,2)\","
								+ "org.jbehave.osgi.core.services;version=\"[1.0,2)\",org.junit,org.ops4j.pax.exam;version=\"[3.5,4)\","
								+ "org.ops4j.pax.exam.options;version=\"[3.5,4)\",org.ops4j.pax.exam.options.extra;version=\"[3.5,4)\","
								+ "org.osgi.framework,org.osgi.util.tracker")
				.build(TinyBundles.withClassicBuilder()));
	}

	protected static CompositeOption equinox() {
		DefaultCompositeOption options = new DefaultCompositeOption();

		options.add(mavenBundle("org.lunifera.osgi",
				"org.eclipse.osgi.services").versionAsInProject());
		options.add(mavenBundle("org.lunifera.osgi", "org.eclipse.equinox.ds")
				.versionAsInProject().startLevel(1));
		options.add(mavenBundle("org.lunifera.osgi", "org.eclipse.equinox.cm")
				.startLevel(1).versionAsInProject());
		options.add(mavenBundle("org.lunifera.osgi",
				"org.eclipse.equinox.event").versionAsInProject().startLevel(2));
		options.add(mavenBundle("org.lunifera.osgi",
				"org.eclipse.equinox.common").versionAsInProject());
		options.add(mavenBundle("org.lunifera.osgi", "org.eclipse.equinox.util")
				.versionAsInProject());

		if (isConsoleOn()) {
			options.add(mavenBundle("org.apache.felix",
					"org.apache.felix.gogo.command").start()
					.versionAsInProject());
			options.add(mavenBundle("org.apache.felix",
					"org.apache.felix.gogo.shell").start().versionAsInProject());
			options.add(mavenBundle("org.lunifera.osgi",
					"org.eclipse.equinox.console").start().versionAsInProject());
		}

		return options;
	}

	protected static CompositeOption felix() {
		DefaultCompositeOption options = new DefaultCompositeOption();
		options.add(mavenBundle("org.apache.felix",
				"org.apache.felix.configadmin").versionAsInProject()
				.startLevel(1));
		options.add(mavenBundle("org.apache.felix",
				"org.apache.felix.eventadmin").versionAsInProject().startLevel(
				2));
		options.add(mavenBundle("org.apache.felix", "org.apache.felix.metatype")
				.versionAsInProject());
		options.add(mavenBundle("org.apache.felix", "org.apache.felix.prefs")
				.versionAsInProject().startLevel(2));
		options.add(mavenBundle("org.apache.felix", "org.apache.felix.log")
				.versionAsInProject().startLevel(1));
		options.add(mavenBundle("org.apache.felix", "org.apache.felix.scr")
				.versionAsInProject().startLevel(2));
		options.add(frameworkProperty(
				"org.osgi.framework.system.packages.extra")
				.value("org.ops4j.pax.exam;version=3.5.0,org.ops4j.pax.exam.options;version=3.5.0,org.ops4j.pax.exam.util;version=3.5.0,org.w3c.dom.traversal"));
		return options;
	}

	public static boolean isConsoleOn() {
		String port = System.getProperty(OSGI_CONSOLE_PROPERTY);
		if (port != null && !port.isEmpty())
			return true;
		else
			return false;
	}

	public static boolean isTraceOn() {
		String trace = System.getProperty(JBEHAVE_OSGi_TRACE_PROPERTY);
		return "true".equals(trace) || "on".equals(trace);
	}

	public static boolean isEquinoxKepler() {
		String env = System.getProperty(PAXEXAM_FRAMEWORK_PROPERTY);
		return "equinox_kepler".equals(env) || "equinox-kepler".equals(env);
	}
	
	public static boolean isEquinoxLuna() {
		String env = System.getProperty(PAXEXAM_FRAMEWORK_PROPERTY);
		return "equinox_luna".equals(env) || "equinox-luna".equals(env);
	}

	public static boolean isFelix() {

		return "felix".equals(System.getProperty(PAXEXAM_FRAMEWORK_PROPERTY));
	}

	public static CompositeOption jbehaveCoreAndDependencies() {
		return jbehaveCoreAndDependencies(true);
	}

	public static CompositeOption jbehaveCoreAndDependencies(boolean cleanCache) {
		DefaultCompositeOption composite = new DefaultCompositeOption(jbehaveDependenciesOnly(cleanCache));
		composite.add(mavenBundle("org.jbehave.osgi", "org.jbehave.osgi.core")
						.versionAsInProject());
		if (isTraceOn()){
			composite.add(mavenBundle("org.jbehave.osgi", "org.jbehave.osgi.logging")
				.versionAsInProject().startLevel(1));
		}
		return composite;
	}

	/**
	 * A collection of PaxExam configuration options that will install all
	 * JBehave OSGi core dependencies into the OSGi container.
	 * 
	 * @param cleanCaches
	 *            determine if the caches must be clean.
	 * @return a {@link CompositeOption} for the JBehave dependencies.
	 */
	public static CompositeOption jbehaveDependenciesOnly(boolean cleanCaches) {

		DefaultCompositeOption options = new DefaultCompositeOption();

		// repository(
		// "http://maven.lunifera.org:8086/nexus/content/repositories/releases/")
		// .id("lunifera"),
		options.add(junitBundles());
		options.add(when(cleanCaches).useOptions(cleanCaches()));
		if (isFelix()) {
			options.add(felix());
		} else if (isEquinoxLuna()) {
			options.add(equinox());
		} else if (isEquinoxKepler()) {
			options.add(equinox());
		}
		options.add(mavenBundle("org.knowhowlab.osgi",
				"org.knowhowlab.osgi.testing.utils").versionAsInProject());
		options.add(mavenBundle("org.knowhowlab.osgi",
				"org.knowhowlab.osgi.testing.assertions").versionAsInProject());
		options.add(mavenBundle("org.apache.servicemix.bundles",
				"org.apache.servicemix.bundles.xstream").versionAsInProject());
		options.add(mavenBundle("org.apache.servicemix.bundles",
				"org.apache.servicemix.bundles.xpp3").versionAsInProject());
		options.add(mavenBundle("org.apache.servicemix.bundles",
				"org.apache.servicemix.bundles.xmlpull").versionAsInProject());
		options.add(mavenBundle("org.apache.servicemix.bundles",
				"org.apache.servicemix.bundles.woodstox").versionAsInProject());
		options.add(mavenBundle("org.apache.servicemix.bundles",
				"org.apache.servicemix.bundles.freemarker")
				.versionAsInProject());
		options.add(mavenBundle("commons-lang", "commons-lang")
				.versionAsInProject());
		options.add(mavenBundle("org.apache.felix",
				"org.apache.felix.gogo.runtime").versionAsInProject());
		options.add(mavenBundle("commons-collections", "commons-collections")
				.versionAsInProject());
		options.add(mavenBundle("commons-io", "commons-io")
				.versionAsInProject());
		options.add(mavenBundle("org.apache.servicemix.bundles",
				"org.apache.servicemix.bundles.paranamer").versionAsInProject());
		options.add(buildPaxExamExtensionBundle());
		options.add(mavenBundle("com.google.guava", "guava")
				.versionAsInProject());
		return options;
	}

	/**
	 * Creates a {@link JUnitBundlesOption}.
	 * 
	 * @return junit bundles option
	 */
	public static CompositeOption junitBundles() {
		DefaultCompositeOption options = new DefaultCompositeOption();

		options.add(new OrbitJUnitBundlesOption());
		options.add(systemProperty("pax.exam.invoker").value("junit"));
		options.add(bundle("link:classpath:META-INF/links/org.ops4j.pax.exam.invoker.junit.link"));

		options.add(mavenBundle("org.lunifera.osgi", "org.hamcrest.integration")
				.versionAsInProject());
		options.add(mavenBundle("org.lunifera.osgi", "org.hamcrest.library")
				.versionAsInProject());
		options.add(mavenBundle("org.lunifera.osgi", "org.hamcrest.core")
				.versionAsInProject());
		return options;
	}


	/**
	 * Property that should be used to specify which OSGi framework to use.
	 * <p>
	 * Currently the options are: equinox_kepler, equinox_luna, felix.
	 */
	public final static String PAXEXAM_FRAMEWORK_PROPERTY = "pax.exam.framework";

	public final static String OSGI_CONSOLE_PROPERTY = "osgi.console";
	
	public final static String JBEHAVE_OSGi_TRACE_PROPERTY = "jbehave.osgi.trace";

	public ProbeOptions() {
	}

}
