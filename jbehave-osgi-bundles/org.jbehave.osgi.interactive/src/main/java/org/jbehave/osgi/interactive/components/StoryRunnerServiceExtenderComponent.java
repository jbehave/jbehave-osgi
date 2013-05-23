package org.jbehave.osgi.interactive.components;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.jbehave.core.junit.AnnotatedPathRunner;
import org.jbehave.osgi.interactive.config.EmbedderPropertiesBuilder;
import org.jbehave.osgi.interactive.services.StoryRunnerService;
import org.junit.runner.RunWith;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.util.tracker.ServiceTracker;

/**
 * 
 * @author Cristiano Gavião
 *
 */
public class StoryRunnerServiceExtenderComponent extends
		AbstractExtenderComponent {

	@Override
	public void onExtensionAddition(Bundle extensionBundle, String[] headerValues) {
		for (int i = 0; i < headerValues.length; i++) {
			String embedderManifestHeaderItem = headerValues[i];
			if (embedderManifestHeaderItem != null
					&& !embedderManifestHeaderItem.isEmpty()) {

				if (JAVA_CLASS_QUALIFIED_NAME_PATTERN.matcher(
						embedderManifestHeaderItem).matches()) {
					processEmbedderManifestHeaderClass(extensionBundle,
							embedderManifestHeaderItem);
				}

				else if (JAVA_QUALIFIED_NAME_PATTERN_WILDCARD.matcher(
						embedderManifestHeaderItem).matches()) {
					embedderManifestHeaderItem = embedderManifestHeaderItem
							.replace(".", "/");
					String path = "/"
							.concat(embedderManifestHeaderItem
									.substring(0, embedderManifestHeaderItem
											.lastIndexOf("/") + 1));
					String pattern = embedderManifestHeaderItem.substring(
							embedderManifestHeaderItem.lastIndexOf("/") + 1)
							.concat(".class");
					logDebug("Searching for :" + path + pattern);

					BundleWiring bundleWiring = extensionBundle
							.adapt(BundleWiring.class);
					Collection<String> classes = bundleWiring.listResources(
							path, pattern, BundleWiring.LISTRESOURCES_LOCAL);
					for (Iterator<String> iterator = classes.iterator(); iterator
							.hasNext();) {
						// it is a Class and transform dots
						String className = iterator.next();
						if (className.contains("$"))
							continue;
						String classFixedName = className.substring(0,
								className.lastIndexOf(".class")).replace("/",
								".");
						processEmbedderManifestHeaderClass(extensionBundle,
								classFixedName);
					}
				}
			}
		}
	}

	@Override
	public void onExtensionRemoval(Bundle bundle, Object object) {
		deleteFactoryConfigurationForExtendedBundle(bundle.getSymbolicName());
		logDebug("Deleted Story Runner Service configurations for bundle '"
				+ bundle.getSymbolicName() + "'.");
	}

	private void processEmbedderManifestHeaderClass(Bundle extensionBundle,
			String className) {
		Class<?> clazz = null;
		logDebug("Processing " + className);
		try {
			clazz = extensionBundle.loadClass(className);

			if (clazz.isAnnotationPresent(RunWith.class)) {
				// has annotations
				RunWith annotation = clazz.getAnnotation(RunWith.class);
				if (annotation.value().equals(AnnotatedPathRunner.class)) {
					registerStoryRunnerService4AnnotatedPathRunner(extensionBundle,
							clazz);
				} else {
					logWarn("Processed class " + className
							+ " is not an AnnotatedPathRunner based one. ");
				}
			} else {
				logWarn("Processed class " + className
						+ " is not an AnnotatedPathRunner based one. ");
			}

		} catch (ClassNotFoundException e) {
			logError("Could not instantiate " + className, e);
		}
	}

	private void registerStoryRunnerService4AnnotatedPathRunner(Bundle extensionBundle,
			Class<?> clazz) {
		EmbedderPropertiesBuilder embedderPropertiesBuilder = new EmbedderPropertiesBuilder(
				clazz);
		Dictionary<String, Object> properties = embedderPropertiesBuilder
				.buildEmbedderProperties();
		createFactoryConfigurationForExtendedItem(extensionBundle.getSymbolicName(),
				clazz.getName(), "AnnotatedPathRunner", properties);
		StoryRunnerService service = getRegisteredStoryRunnerService(
				extensionBundle, clazz, 5, TimeUnit.SECONDS);
		if (service != null) {
			service.setStoryBundle(extensionBundle);
		}
	}

	private StoryRunnerService getRegisteredStoryRunnerService(
			Bundle bundle, Class<?> clazz, long timeout, TimeUnit timeUnit) {
		BundleContext bc = getComponentContext().getBundleContext();
		Filter filter = null;

		try {
			filter = bc
					.createFilter("(&(objectClass=org.jbehave.osgi.interactive.services.StoryRunnerService)("
							+ getExtenderBundleProperty()
							+ "="
							+ bundle.getSymbolicName()
							+ ")"
							+ "("
							+ getExtenderItemProperty()
							+ "="
							+ clazz.getName()
							+ "))");
		} catch (InvalidSyntaxException e) {
			logError("Error creating filter", e);
			return null;
		}

		ServiceTracker<StoryRunnerService, StoryRunnerService> storyRunnerTracker = new ServiceTracker<StoryRunnerService, StoryRunnerService>(
				bc, filter, null);
		storyRunnerTracker.open();

		try {
			return storyRunnerTracker
					.waitForService(timeUnit.toMillis(timeout));
		} catch (InterruptedException e) {
			return null;
		} finally {
			storyRunnerTracker.close();
		}

	}
}
