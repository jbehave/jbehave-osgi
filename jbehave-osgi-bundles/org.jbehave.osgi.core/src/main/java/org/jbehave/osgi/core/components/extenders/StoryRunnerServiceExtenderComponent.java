package org.jbehave.osgi.core.components.extenders;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Iterator;

import org.jbehave.core.annotations.Configure;
import org.jbehave.core.annotations.UsingPaths;
import org.jbehave.osgi.core.Constants;
import org.jbehave.osgi.core.components.AbstractExtenderComponent;
import org.jbehave.osgi.core.configuration.dto.EmbedderOsgiPropertiesBuilder;
import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

import com.google.common.collect.Lists;

/**
 * 
 * @author Cristiano Gavião
 *
 */
@Component(enabled = true, immediate = true)
public class StoryRunnerServiceExtenderComponent extends
		AbstractExtenderComponent {

	public StoryRunnerServiceExtenderComponent() {
		setComponentDescription("JBehave StoryRunner Extender Component");
		setExtenderManifestHeader(Constants.STORY_RUNNER_EXTENDER_MANIFEST_HEADER);
		setExtenderPropertyGroup(null);
		setExtenderPropertyItem(Constants.STORY_RUNNER_EXTENDER_PROPERTY_ITEM);
		setExtenderPropertyItemClassifier(Constants.STORY_RUNNER_EXTENDER_PROPERTY_ITEM_CLASSIFIER);
		setExtenderTargetFactoryPid(Constants.STORY_RUNNER_FACTORY_FPID);
	}

	@Reference(cardinality = ReferenceCardinality.MANDATORY)
	@Override
	protected void bindConfigurationAdmin(ConfigurationAdmin configurationAdmin) {
		super.bindConfigurationAdmin(configurationAdmin);
	}

	@Override
	public void onExtensionAddition(Bundle extendeeBundle, String[] headerValues) {
		for (int i = 0; i < headerValues.length; i++) {
			String embedderManifestHeaderItem = headerValues[i];
			if (embedderManifestHeaderItem != null
					&& !embedderManifestHeaderItem.isEmpty()) {

				if (JAVA_CLASS_QUALIFIED_NAME_PATTERN.matcher(
						embedderManifestHeaderItem).matches()) {
					processEmbedderManifestHeaderClass(extendeeBundle,
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
					logDebug("Searching for: " + path + pattern);

					BundleWiring bundleWiring = extendeeBundle
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
						processEmbedderManifestHeaderClass(extendeeBundle,
								classFixedName);
					}
				}
			}
		}
	}

	@Override
	public void onExtensionRemoval(Bundle extendeeBundle, Object object) {
		deleteFactoryConfigurationsForExtendeeBundle(
				extendeeBundle.getSymbolicName(), extendeeBundle.getVersion()
						.toString());
		logDebug("Deleted Story Runner Service configurations for bundle '"
				+ extendeeBundle.getSymbolicName() + "'.");
	}

	private void processEmbedderManifestHeaderClass(Bundle extendeeBundle,
			String className) {
		Class<?> clazz = null;
		logDebug("Processing StoryRunner from: " + className);
		try {
			clazz = extendeeBundle.loadClass(className);

			if (!clazz.isAnnotationPresent(Configure.class)) {
				logDebug("Ignoring class "
						+ className
						+ " since it is not annotated with JBehave's @Configure.");
				return;
			}
			if (!clazz.isAnnotationPresent(UsingPaths.class)) {
				logDebug("Ignoring class "
						+ className
						+ " since it is not annotated with JBehave's @UsingPaths.");
				return;
			}
			registerStoryRunnerService(extendeeBundle, clazz);

		} catch (ClassNotFoundException e) {
			logError("Could not instantiate " + className, e);
		}
	}

	private void registerStoryRunnerService(Bundle extendeeBundle,
			Class<?> extendeeItemClazz) {
		Dictionary<String, Object> properties = new EmbedderOsgiPropertiesBuilder(
				extendeeBundle, extendeeItemClazz).buildEmbedderProperties();
		createFactoryConfigurationForExtendeeWithOneItem(
				extendeeBundle.getSymbolicName(), extendeeBundle.getVersion()
						.toString().toString(), extendeeItemClazz.getName(),
				Lists.newArrayList("Embedder"), properties);

	}
}
