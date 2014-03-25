package org.jbehave.osgi.core.components.extenders;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Iterator;

import org.jbehave.osgi.core.Constants;
import org.jbehave.osgi.core.components.AbstractExtenderComponent;
import org.jbehave.osgi.core.configuration.dto.EmbedderOsgiPropertiesBuilder;
import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.log.LogService;

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
        setExtenderManifestHeader(Constants.STORY_RUNNER_EXTENDER_MANIFEST_HEADER);
        setExtenderPropertyGroup(null);
        setExtenderPropertyItem(Constants.STORY_RUNNER_EXTENDER_PROPERTY_ITEM);
        setExtenderPropertyItemClassifier(Constants.STORY_RUNNER_EXTENDER_PROPERTY_ITEM_CLASSIFIER);
        setExtenderTargetFactoryPid(Constants.STORY_RUNNER_FACTORY_FPID);
    }

    @Activate
    @Override
    protected void activate(ComponentContext context) {
        super.activate(context);
    }

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    @Override
    protected void bindConfigurationAdmin(ConfigurationAdmin configurationAdmin) {
        super.bindConfigurationAdmin(configurationAdmin);
    }

    @Reference
    @Override
    protected void bindLogService(LogService logService) {
        super.bindLogService(logService);
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

    private synchronized void processEmbedderManifestHeaderClass(Bundle extendeeBundle,
            String className) {

        Class<?> clazz = null;
        logDebug("Processing Embedder Annotations from: " + className);
        try {

            clazz = extendeeBundle.loadClass(className);

            if (!isAnnotationPresent(clazz, "org.jbehave.core.annotations.Configure")) {
                logDebug("Ignoring class "
                        + className
                        + " since it is not annotated with JBehave's @Configure.");
                return;
            }
            if (!isAnnotationPresent(clazz,"org.jbehave.core.annotations.UsingPaths")) {
                logDebug("Ignoring class "
                        + className
                        + " since it is not annotated with JBehave's @UsingPaths.");
                return;
            }
            registerStoryRunnerService(extendeeBundle, clazz);

        } catch (ClassNotFoundException e) {

            logError("Could not instantiate " + className, e);
            e.printStackTrace();
        }
    }

    private boolean isAnnotationPresent(Class<?> clazz, String annotation){
        Annotation[] ann = clazz.getDeclaredAnnotations();
        for (int i = 0; i < ann.length; i++) {
            String annName = ann[i].annotationType().getName();
            if (annotation.equals(annName)){
                return true;
            }
        }
        return false;
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
