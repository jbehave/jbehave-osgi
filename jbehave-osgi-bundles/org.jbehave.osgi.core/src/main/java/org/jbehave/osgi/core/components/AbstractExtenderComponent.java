package org.jbehave.osgi.core.components;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import org.jbehave.osgi.core.Constants;
import org.osgi.framework.Bundle;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.BundleTracker;

import com.google.common.collect.Lists;

/**
 * 
 * @author Cristiano Gavião
 *
 */
public abstract class AbstractExtenderComponent extends AbstractComponent {

    private String extenderManifestHeader;
    private String extenderTargetFactoryPid;
    private String extenderPropertyGroup;
    private String extenderPropertyItem;
    private String extenderPropertyItemClassifier;

    private BundleTracker<?> extensionTracker;

    private AtomicReference<ConfigurationAdmin> configurationAdminRef = new AtomicReference<>();

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

    protected void activate(ComponentContext context) {
        super.activate(context);

        if (extenderManifestHeader != null && !extenderManifestHeader.isEmpty())
            startExtenderContributionTracker();
    }

    protected void bindConfigurationAdmin(ConfigurationAdmin configurationAdmin) {
        this.configurationAdminRef.set(configurationAdmin);
        logDebug("Binded Configuration Admin Service for "
                + getClass().getSimpleName() + ".");
    }

    protected String createFactoryConfigurationForExtendeeWithMultipleItems(
            String extendeeBundle, String extendeeBundleVersion,
            String extendeeGroup, List<String> extendeeItems,
            Dictionary<String, Object> properties) {
        Configuration configuration;

        String pid = null;
        try {

            // try to find an existing configuration
            configuration = findFactoryConfigurationForExtendeeWithMultipleItems(
                    extendeeBundle, extendeeBundleVersion, extendeeGroup);

            if (configuration == null) {
                // create a new configuration
                configuration = getConfigurationAdmin()
                        .createFactoryConfiguration(
                                getExtenderTargetFactoryPid(), null);
            }
            pid = configuration.getPid();
            if (properties == null) {
                properties = new Hashtable<String, Object>();
            }
            properties.put(Constants.EXTENDEE_BUNDLE, extendeeBundle);
            properties.put(Constants.EXTENDEE_BUNDLE_VERSION,
                    extendeeBundleVersion);
            properties.put(getExtenderPropertyGroup(), extendeeGroup);
            if (getExtenderPropertyItem() != null) {
                properties
                        .put(getExtenderPropertyItem(), extendeeItems
                                .toArray(new String[extendeeItems.size()]));
            }
            configuration.update(properties);

            logDebug("Created configuration for component: '"
                    + extenderTargetFactoryPid + "' with PID: '"
                    + configuration.getPid() + "' , with this properties: "
                    + properties.toString());
        } catch (IOException e) {
            logError("Error on setup Configuration Service", e);
        }
        return pid;
    }

    protected String createFactoryConfigurationForExtendeeWithOneItem(
            String extendeeBundle, String extendeeBundleVersion,
            String extendeeItem, Dictionary<String, Object> properties) {
        return createFactoryConfigurationForExtendeeWithOneItem(extendeeBundle,
                extendeeBundleVersion, extendeeItem, null, properties);
    }

    protected String createFactoryConfigurationForExtendeeWithOneItem(
            String extendeeBundle, String extendeeBundleVersion,
            String extendeeItem, List<String> extendeeItemClassifiers,
            Dictionary<String, Object> properties) {
        Configuration configuration;

        String pid = null;
        try {

            if (properties.isEmpty()) {
                System.err.println("Could not build a Embedder for "
                        + extendeeBundle);
                return "";
            }

            // try to find an existing configuration
            configuration = findFactoryConfigurationForExtendeeWithOneItem(
                    extendeeBundle, extendeeBundleVersion, extendeeItem,
                    extendeeItemClassifiers);

            if (configuration == null) {
                // create a new configuration
                configuration = getConfigurationAdmin()
                        .createFactoryConfiguration(
                                getExtenderTargetFactoryPid(), null);
            }
            pid = configuration.getPid();

            properties.put(Constants.EXTENDEE_BUNDLE, extendeeBundle);
            properties.put(Constants.EXTENDEE_BUNDLE_VERSION,
                    extendeeBundleVersion);

            if (getExtenderPropertyItem() != null
                    && !getExtenderPropertyItem().isEmpty()) {
                properties.put(getExtenderPropertyItem(), extendeeItem);
            }
            if (getExtenderPropertyItemClassifier() != null) {
                properties.put(getExtenderPropertyItemClassifier(),
                        extendeeItemClassifiers
                                .toArray(new String[extendeeItemClassifiers
                                        .size()]));
            }
            // log
            if (properties.isEmpty()) {
                System.err.println("BUNDLE: " + extendeeBundle + "\t"
                        + this.getComponentName());

            }

            configuration.update(properties);

            logDebug("Created configuration for component: '"
                    + extenderTargetFactoryPid + "' with PID: '"
                    + configuration.getPid() + "' , with this properties: "
                    + properties.toString());

        } catch (IOException e) {
            logError("Error on setup Configuration Service", e);
        }
        return pid;
    }

    protected void deactivate(ComponentContext context) {
        logInfo("Stopping JBehave OSGi Extender Component: "
                + context.getComponentInstance().toString());
        stopExtenderContributionTracker();
        super.deactivate(context);
    }

    protected void deleteFactoryConfigurationsForExtendeeBundle(
            String extenderBundle, String extenderBundleVersion) {
        Configuration[] configurations;
        try {
            configurations = findFactoryConfigurationsForExtendeeBundle(
                    extenderBundle, extenderBundleVersion);
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
                        + extenderTargetFactoryPid + "' and extenderBundle '"
                        + extenderBundle + "'");
        } catch (IOException e1) {
            logError("no configuration for factoryPid '"
                    + extenderTargetFactoryPid + "' and extenderBundle '"
                    + extenderBundle + "'", e1);
        }
    }

    protected Configuration findFactoryConfigurationForExtendeeWithMultipleItems(
            String extendeeBundleName, String extendeeBundleVersion,
            String extendeeGroup) throws IOException {

        if (extendeeBundleName == null || extendeeBundleName.isEmpty())
            return null;
        StringBuilder filterStr = new StringBuilder("(&");
        try {
            filterStr.append("(").append(ConfigurationAdmin.SERVICE_FACTORYPID)
                    .append("=").append(extenderTargetFactoryPid).append(")");
            filterStr.append("(").append(Constants.EXTENDEE_BUNDLE).append("=")
                    .append(extendeeBundleName).append(")");
            filterStr.append("(").append(getExtenderPropertyGroup())
                    .append("=").append(extendeeGroup).append(")");
            filterStr.append("(").append(Constants.EXTENDEE_BUNDLE_VERSION)
                    .append("=").append(extendeeBundleVersion)
                    .append(getExtenderPropertyGroup()).append(")");
            filterStr.append(")");

            Configuration[] configurations = getConfigurationAdmin()
                    .listConfigurations(filterStr.toString());
            if (configurations != null && configurations.length > 0) {
                return configurations[0];
            }
        } catch (Exception e) {
            logError("Error on building the filter.", e);
        } finally {
            filterStr = null;
        }
        return null;

    }

    protected Configuration findFactoryConfigurationForExtendeeWithOneItem(
            String extendeeBundle, String extendeeBundleVersion,
            String extendeeItem, List<String> extendeeItemClassifiers)
            throws IOException {
        if (extendeeBundle == null || extendeeBundle.isEmpty())
            return null;

        StringBuilder filter = new StringBuilder("(&");
        try {
            filter.append("(service.factoryPid=")
                    .append(extenderTargetFactoryPid).append(")");
            filter.append("(").append(Constants.EXTENDEE_BUNDLE).append("=")
                    .append(extendeeBundle).append(")");
            filter.append("(").append(Constants.EXTENDEE_BUNDLE_VERSION)
                    .append("=").append(extendeeBundleVersion).append(")");
            filter.append("(").append(extenderPropertyItem).append("=")
                    .append(extendeeItem).append(")");

            for (String itemClassifier : extendeeItemClassifiers) {

                filter.append("(").append(extenderPropertyItemClassifier)
                        .append("=").append(itemClassifier).append(")");
            }
            filter.append(")");

            Configuration[] configurations = getConfigurationAdmin()
                    .listConfigurations(filter.toString());
            if (configurations != null && configurations.length > 0) {
                return configurations[0];
            }
        } catch (InvalidSyntaxException e) {
        } finally {
            filter = null;
        }
        return null;
    }

    protected Configuration findFactoryConfigurationForExtendeeWithOneItem(
            String extendeeBundle, String extendeeBundleVersion,
            String extendeeItem, String extendeeItemClassifier)
            throws IOException {

        return findFactoryConfigurationForExtendeeWithOneItem(extendeeBundle,
                extendeeBundleVersion, extendeeItem,
                Lists.newArrayList(extendeeItemClassifier));
    }

    protected Configuration[] findFactoryConfigurationsForExtendeeBundle(
            String extendeeBundle, String extendeeBundleVersion)
            throws IOException {

        if (extendeeBundle == null || extendeeBundle.isEmpty())
            return null;
        StringBuilder filterStr = new StringBuilder("(&");
        try {
            filterStr.append("(").append(ConfigurationAdmin.SERVICE_FACTORYPID)
                    .append("=").append(extenderTargetFactoryPid).append(")");
            filterStr.append("(").append(Constants.EXTENDEE_BUNDLE).append("=")
                    .append(extendeeBundle).append(")");
            filterStr.append("(").append(Constants.EXTENDEE_BUNDLE_VERSION)
                    .append("=").append(extendeeBundleVersion)
                    .append(getExtenderPropertyGroup()).append(")");
            filterStr.append(")");

            Configuration[] configurations = getConfigurationAdmin()
                    .listConfigurations(filterStr.toString());

            return configurations;

        } catch (InvalidSyntaxException e) {
            logError("Error on Configuration Search Filter", e);
        }
        return null;
    }

    protected ConfigurationAdmin getConfigurationAdmin() {
        return configurationAdminRef.get();
    }

    protected String getExtenderManifestHeader() {
        return extenderManifestHeader;
    }

    protected String getExtenderPropertyGroup() {
        return extenderPropertyGroup;
    }

    protected String getExtenderPropertyItem() {
        return extenderPropertyItem;
    }

    protected String getExtenderPropertyItemClassifier() {
        return extenderPropertyItemClassifier;
    }

    protected String getExtenderTargetFactoryPid() {
        return extenderTargetFactoryPid;
    }

    protected String getTargetComponentService() {
        return extenderTargetFactoryPid;
    }

    public String[] isValidContribution(Bundle bundle) {

        String header = bundle.getHeaders().get(getExtenderManifestHeader());
        if (header != null && !header.isEmpty()) {
            return header.split(",");
        }
        return null;
    }

    public abstract void onExtensionAddition(Bundle bundle,
            String[] headerValues);

    public abstract void onExtensionRemoval(Bundle bundle, Object object);

    protected void setExtenderManifestHeader(String extenderManifestHeader) {
        this.extenderManifestHeader = extenderManifestHeader;
    }

    public void setExtenderPropertyGroup(String extenderPropertyGroup) {
        this.extenderPropertyGroup = extenderPropertyGroup;
    }

    protected void setExtenderPropertyItem(String extenderPropertyItem) {
        this.extenderPropertyItem = extenderPropertyItem;
    }

    protected void setExtenderPropertyItemClassifier(
            String extenderPropertyItemClassifier) {
        this.extenderPropertyItemClassifier = extenderPropertyItemClassifier;
    }

    protected void setExtenderTargetFactoryPid(String extenderTargetFactoryPid) {
        this.extenderTargetFactoryPid = extenderTargetFactoryPid;
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

    protected void unbindConfigurationAdmin(
            ConfigurationAdmin configurationAdmin) {
        this.configurationAdminRef.compareAndSet(configurationAdmin, null);
        logDebug("Unbinded Configuration Admin Service for "
                + getClass().getSimpleName() + ".");
    }
}
