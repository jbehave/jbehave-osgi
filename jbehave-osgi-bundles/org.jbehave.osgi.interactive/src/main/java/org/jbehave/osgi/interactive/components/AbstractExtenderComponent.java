package org.jbehave.osgi.interactive.components;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.regex.Pattern;

import org.osgi.framework.Bundle;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.ComponentConstants;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.BundleTracker;

/**
 * 
 * @author Cristiano Gavião
 *
 */
public abstract class AbstractExtenderComponent extends AbstractComponent
		implements ExtendableComponent {
	private String extenderManifestHeader;
	private String targetComponentService;
	private String extenderBundleProperty;
	private String extenderItemProperty;
	private String extenderItemClassifierProperty;
	private BundleTracker<?> extensionTracker;
	private ConfigurationAdmin configurationAdmin;
	private String componentName;
	private String componentDescription;

	protected static final String JAVA_IDENTIFIER = "\\p{javaJavaIdentifierStart}(\\p{javaJavaIdentifierPart})*";
	protected static final String JAVA_QUALIFIED_NAME = "(" + JAVA_IDENTIFIER
			+ "\\.)+" + JAVA_IDENTIFIER;
	protected static final String JAVA_CLASS_QUALIFIED_NAME = JAVA_QUALIFIED_NAME
			+ "(\\.[A-Z]\\w+)$";
	protected static final String JAVA_QUALIFIED_NAME_WILDCARD = JAVA_QUALIFIED_NAME
			+ "(\\.(\\*|[A-Z]+\\*))$";
	// Create the pattern, as it will be reused many times
	protected static final Pattern JAVA_CLASS_QUALIFIED_NAME_PATTERN = Pattern
			.compile(JAVA_CLASS_QUALIFIED_NAME);
	protected static final Pattern JAVA_QUALIFIED_NAME_PATTERN_WILDCARD = Pattern
			.compile(JAVA_QUALIFIED_NAME_WILDCARD);

	public String getExtenderManifestHeader() {
		return extenderManifestHeader;
	}

	protected void setExtenderManifestHeader(String extenderManifestHeader) {
		this.extenderManifestHeader = extenderManifestHeader;
	}

	protected void activate(ComponentContext context) {
		super.activate(context);
		extenderManifestHeader = (String) getProperties().get(
				"extender.manifestHeader");
		extenderBundleProperty = (String) getProperties().get(
				"extender.bundleProperty");
		extenderItemProperty = (String) getProperties().get(
				"extender.itemProperty");
		extenderItemClassifierProperty = (String) getProperties().get(
				"extender.itemClassifierProperty");
		targetComponentService = (String) getProperties().get(
				"extender.targetComponentService");
		componentName = (String) getProperties().get(
				ComponentConstants.COMPONENT_NAME);
		componentDescription = (String) getProperties().get(
				"component.description");
		logInfo("Starting " + componentDescription + " (" + componentName + ")");
		if (extenderManifestHeader != null && !extenderManifestHeader.isEmpty())
			startExtenderContributionTracker();
	}

	protected void deactivate(ComponentContext context) {
		logInfo("Stopping JBehave OSGi Extender Component: "
				+ context.getComponentInstance().toString());
		stopExtenderContributionTracker();
		super.deactivate(context);
	}

	protected void startExtenderContributionTracker() {
		extensionTracker = new DefaultExtensionTracker(this,
				getComponentContext().getBundleContext(), Bundle.ACTIVE, null);
		extensionTracker.open();
	}

	protected void stopExtenderContributionTracker() {
		if (extensionTracker != null) {
			extensionTracker.close();
			extensionTracker = null;
		}
	}

	public abstract void onExtensionAddition(Bundle bundle,
			String[] headerValues);

	@Override
	public String[] isValidContribution(Bundle bundle) {

		String header = bundle.getHeaders().get(getExtenderManifestHeader());
		if (header != null && !header.isEmpty()) {
			return header.split(",");
		}
		return null;
	}

	public abstract void onExtensionRemoval(Bundle bundle, Object object);

	protected ConfigurationAdmin getConfigurationAdmin() {
		return configurationAdmin;
	}

	protected void bindConfigurationAdmin(ConfigurationAdmin configurationAdmin) {
		this.configurationAdmin = configurationAdmin;
		logDebug("Binded Configuration Admin Service for the JBehave OSGi Component.");
	}

	protected void unbindConfigurationAdmin(
			ConfigurationAdmin configurationAdmin) {
		if (this.configurationAdmin == configurationAdmin) {
			this.configurationAdmin = configurationAdmin;
			logDebug("Unbinded Configuration Admin Service for the JBehave OSGi Component.");
		}
	}

	protected String createFactoryConfigurationForExtendedItem(
			String extenderBundle, String extenderItem, String extenderItemClassifier,
			Dictionary<String, Object> properties) {
		Configuration configuration;

		String pid = null;
		try {
			if (configurationAdmin == null)
				throw new RuntimeException(
						"Configuration Admin Manager was not wired !!!");

			// try to find an existing configuration
			configuration = findFactoryConfigurationForExtendedItem(
					extenderBundle, extenderItem);
			if (configuration == null) {
				// create a new configuration
				configuration = configurationAdmin.createFactoryConfiguration(
						targetComponentService, null);
			}
			pid = configuration.getPid();

			if (properties == null) {
				properties = new Hashtable<String, Object>();
			}

			properties.put("extender.bundleProperty", extenderBundleProperty);
			properties.put("extender.itemProperty", extenderItemProperty);
			properties.put("extender.itemClassifierProperty", extenderItemClassifierProperty);
			properties.put(extenderBundleProperty, extenderBundle);
			properties.put(extenderItemProperty, extenderItem);
			properties.put(extenderItemClassifierProperty, extenderItemClassifier);
			configuration.update(properties);

			logDebug("Created configuration for component: '"
					+ targetComponentService + "' with PID: '"
					+ configuration.getPid() + "' , with this properties: "
					+ properties.toString());

		} catch (IOException e) {
			logError("Error on setup Configuration Service", e);
		}
		return pid;
	}

	protected Configuration findFactoryConfigurationForExtendedItem(
			String extenderBundle, String extenderItem) throws IOException {
		try {
			String filter = "";
			filter = "(&(service.factoryPid=" + targetComponentService + ") ("
					+ extenderBundleProperty + "=" + extenderBundle + ")("
					+ extenderItemProperty + "=" + extenderItem + "))";

			Configuration[] configurations = getConfigurationAdmin()
					.listConfigurations(filter);
			if (configurations != null && configurations.length > 0) {
				return configurations[0];
			}
		} catch (InvalidSyntaxException e) {
		}

		return null;
	}

	protected void deleteFactoryConfigurationForExtendedBundle(
			String extenderBundle) {
		Configuration[] configurations;
		try {
			configurations = findFactoryConfigurationForExtendedBundle(extenderBundle);
			if (configurations != null) {
				for (int i = 0; i < configurations.length; i++) {
					Configuration configuration = configurations[i];

					try {
						configuration.delete();
					} catch (IOException e) {
						getLogService().log(LogService.LOG_ERROR,
								"Error on setup Configuration Service", e);
					}
				}
			} else
				logDebug("no configuration for factoryPid '"
						+ targetComponentService + "' and extenderBundle '"
						+ extenderBundle + "'");
		} catch (IOException e1) {
			logError("no configuration for factoryPid '"
					+ targetComponentService + "' and extenderBundle '"
					+ extenderBundle + "'", e1);
		}

	}

	protected Configuration[] findFactoryConfigurationForExtendedBundle(
			String extenderBundle) throws IOException {

		if (extenderBundle == null || extenderBundle.isEmpty())
			return null;
		try {
			String filter = "";
			filter = "(&(service.factoryPid=" + targetComponentService + ") ("
					+ extenderBundleProperty + "=" + extenderBundle + "))";

			Configuration[] configurations = getConfigurationAdmin()
					.listConfigurations(filter);

			return configurations;
		} catch (InvalidSyntaxException e) {
			logError("Error on Configuration Search Filter", e);
		}
		return null;
	}

	protected String getTargetComponentService() {
		return targetComponentService;
	}

	protected String getExtenderBundleProperty() {
		return extenderBundleProperty;
	}

	protected String getExtenderItemProperty() {
		return extenderItemProperty;
	}

	protected String getExtenderItemClassifierProperty() {
		return extenderItemClassifierProperty;
	}

	protected String getComponentName() {
		return componentName;
	}

	protected String getComponentDescription() {
		return componentDescription;
	}
}
