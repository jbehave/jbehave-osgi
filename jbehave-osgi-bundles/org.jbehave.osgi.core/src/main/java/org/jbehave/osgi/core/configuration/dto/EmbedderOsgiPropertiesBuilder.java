package org.jbehave.osgi.core.configuration.dto;

import java.beans.Introspector;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.jbehave.core.annotations.Configure;
import org.jbehave.core.annotations.UsingEmbedder;
import org.jbehave.core.annotations.UsingPaths;
import org.jbehave.core.annotations.UsingSteps;
import org.jbehave.core.configuration.AnnotationFinder;
import org.jbehave.core.configuration.AnnotationRequired;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.steps.ParameterConverters.ParameterConverter;
import org.jbehave.core.steps.PrintStreamStepMonitor;
import org.jbehave.osgi.core.Constants;
import org.jbehave.osgi.core.annotations.UsingStepsFactoryServiceFilter;
import org.jbehave.osgi.core.configuration.AnnotationBuilderOsgi;
import org.jbehave.osgi.core.configuration.ConfigurationOsgi;
import org.jbehave.osgi.core.embedder.EmbedderOsgi;
import org.jbehave.osgi.core.io.LoadFromBundleClasspath;
import org.jbehave.osgi.core.reporters.StoryReporterBuilderOsgi;
import org.jbehave.osgi.core.services.InjectableStepsFactoryService;
import org.osgi.framework.Bundle;

/**
 * Allows to transform an annotated {@link Embedder} class into a Map<String,
 * String> of properties that will be used to set an OSGi Configuration for a
 * Story Runner Service. The map of properties will be then used later to create
 * a {@link Embedder} object.
 * 
 * @author Cristiano Gavião
 */
public class EmbedderOsgiPropertiesBuilder {

    private final AnnotationFinder finder;
    private final Bundle extendeeBundle;
    private StringBuilder filterBuilder;

    public EmbedderOsgiPropertiesBuilder(Bundle extendeeBundle,
            Class<?> annotatedClass) {
        this.finder = new AnnotationFinder(annotatedClass);
        this.extendeeBundle = extendeeBundle;
    }

    /**
     * Builds a ConfigurationDTO instance based on annotation {@link Configure}
     * found in the annotated object instance
     * 
     * @return A ConfigurationDTO instance
     */
    private ConfigurationDTO buildConfigurationModel()
            throws AnnotationRequired {

        if (!finder.isAnnotationPresent(Configure.class)) {
            return null;
        }
        ConfigurationDTO configuration = new ConfigurationDTO();
        // uses a specific osgi configuration
        configuration.setConfigurationClass(configurationElement(finder,
                "using", ConfigurationOsgi.class));
        configuration.setKeywords(configurationElement(finder, "keywords"));
        configuration.setFailureStrategy(configurationElement(finder,
                "failureStrategy"));
        configuration.setPendingStepStrategy(configurationElement(finder,
                "pendingStepStrategy"));
        configuration.setParanamer(configurationElement(finder, "paranamer"));
        configuration.setStoryControls(configurationElement(finder,
                "storyControls"));
        configuration.setStepCollector(configurationElement(finder,
                "stepCollector"));
        configuration.setStepdocReporter(configurationElement(finder,
                "stepdocReporter"));
        configuration.setStepFinder(configurationElement(finder, "stepFinder"));
        configuration.setStepMonitor(configurationElement(finder,
                "stepMonitor", PrintStreamStepMonitor.class));
        configuration.setStepPatternParser(configurationElement(finder,
                "stepPatternParser"));
        configuration.setStoryLoader(configurationElement(finder,
                "storyLoader", LoadFromBundleClasspath.class));
        configuration
                .setStoryParser(configurationElement(finder, "storyParser"));
        configuration.setStoryPathResolver(configurationElement(finder,
                "storyPathResolver"));
        configuration.setStoryReporterBuilder(configurationElement(finder,
                "storyReporterBuilder", StoryReporterBuilderOsgi.class));
        configuration.setViewGenerator(configurationElement(finder,
                "viewGenerator"));
        configuration.setParameterConverters(parameterConverters());
        configuration.setParameterControls(configurationElement(finder,
                "parameterControls"));
        configuration.setPathCalculator(configurationElement(finder,
                "pathCalculator"));
        return configuration;
    }

    private EmbedderDTO buildEmbedderModel() {
        if (!finder.isAnnotationPresent(UsingEmbedder.class)) {
            return defaultEmbedderOsgi();
        }
        EmbedderDTO embedderDTO = new EmbedderDTO();

        // An specific osgi embedder
        Class<?> embedder = finder.getAnnotatedValue(UsingEmbedder.class,
                Class.class, "embedder");
        if (embedder.isAssignableFrom(EmbedderOsgi.class)) {
            embedderDTO.setEmbedderClass(embedder.getName());
        } else {
            embedderDTO.setEmbedderClass(EmbedderOsgi.class.getName());
        }

        embedderDTO.setBatch(finder.getAnnotatedValue(UsingEmbedder.class,
                Boolean.class, "batch"));
        embedderDTO.setSkip(finder.getAnnotatedValue(UsingEmbedder.class,
                Boolean.class, "skip"));
        embedderDTO
                .setGenerateViewAfterStories(finder.getAnnotatedValue(
                        UsingEmbedder.class, Boolean.class,
                        "generateViewAfterStories"));
        embedderDTO.setIgnoreFailureInStories(finder.getAnnotatedValue(
                UsingEmbedder.class, Boolean.class, "ignoreFailureInStories"));
        embedderDTO.setIgnoreFailureInView(finder.getAnnotatedValue(
                UsingEmbedder.class, Boolean.class, "ignoreFailureInView"));
        embedderDTO.setVerboseFailures(finder.getAnnotatedValue(
                UsingEmbedder.class, Boolean.class, "verboseFailures"));
        embedderDTO.setVerboseFiltering(finder.getAnnotatedValue(
                UsingEmbedder.class, Boolean.class, "verboseFiltering"));
        embedderDTO.setStoryTimeoutInSecs(finder.getAnnotatedValue(
                UsingEmbedder.class, Long.class, "storyTimeoutInSecs"));
        embedderDTO.setThreads(finder.getAnnotatedValue(UsingEmbedder.class,
                Integer.class, "threads"));
        embedderDTO.setMetaFilters(finder.getAnnotatedValues(
                UsingEmbedder.class, String.class, "metaFilters"));
        embedderDTO.setSystemProperties(finder.getAnnotatedValue(
                UsingEmbedder.class, String.class, "systemProperties"));

        return embedderDTO;
    }

    protected <T, I extends T> String configurationElement(
            AnnotationFinder finder, String name) {
        return configurationElement(finder, name, null);
    }

    protected <T, I extends T> String configurationElement(
            AnnotationFinder finder, String name, Class<I> defaultImplementation) {
        Class<T> implementation = elementImplementation(finder, name);

        if (defaultImplementation != null) {
            if (AnnotationBuilderOsgi.isDefaultConfigureItemImplementation(
                    name, implementation)) {
                return defaultImplementation.getName();
            } else {
                return implementation.getName();
            }
        } else {
            return implementation.getName();
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> Class<T> elementImplementation(AnnotationFinder finder,
            String name) {
        return finder.getAnnotatedValue(Configure.class, Class.class, name);
    }

    private EmbedderDTO defaultEmbedderOsgi() {
        EmbedderDTO embedderDTO = new EmbedderDTO();
        embedderDTO.setEmbedderClass(EmbedderOsgi.class.getName());

        embedderDTO.setBatch(false);
        embedderDTO.setSkip(false);
        embedderDTO.setGenerateViewAfterStories(true);
        embedderDTO.setIgnoreFailureInStories(false);
        embedderDTO.setIgnoreFailureInView(false);
        embedderDTO.setVerboseFailures(false);
        embedderDTO.setVerboseFiltering(false);
        embedderDTO.setStoryTimeoutInSecs(300);
        embedderDTO.setThreads(1);
        embedderDTO.setMetaFilters(new ArrayList<String>());
        embedderDTO.setSystemProperties("");

        return embedderDTO;
    }

    public synchronized Dictionary<String, Object> buildEmbedderProperties() {

        Dictionary<String, Object> properties = new Hashtable<String, Object>();
        transformToProperties(buildEmbedderModel(),
                Constants.DTO_PREFIX_EMBEDDER, properties);
        transformToProperties(buildStorySearchModel(),
                Constants.DTO_PREFIX_STORY_SEARCH, properties);
        transformToProperties(buildConfigurationModel(),
                Constants.DTO_PREFIX_CONFIGURATION, properties);

        if (finder.isAnnotationPresent(UsingStepsFactoryServiceFilter.class)) {
            String custom = finder.getAnnotatedValue(
                    UsingStepsFactoryServiceFilter.class, String.class,
                    "custom");
            if (custom != null && !custom.isEmpty()) {
                filterBuilder = new StringBuilder(custom);

            } else {

                this.filterBuilder = new StringBuilder("(&");
                this.filterBuilder.append("(")
                        .append(org.osgi.framework.Constants.OBJECTCLASS)
                        .append("=")
                        .append(InjectableStepsFactoryService.class.getName())
                        .append(")");
                this.filterBuilder.append("(")
                        .append(Constants.EXTENDEE_BUNDLE).append("=")
                        .append(extendeeBundle.getSymbolicName()).append(")");

                this.filterBuilder.append("(")
                        .append(Constants.EXTENDEE_BUNDLE_VERSION).append("=")
                        .append(extendeeBundle.getVersion().toString())
                        .append(")");

                List<String> values = finder.getAnnotatedValues(
                        UsingStepsFactoryServiceFilter.class, String.class,
                        "value");
                for (String factoryId : values) {
                    filterBuilder
                            .append("(")
                            .append(Constants.STEP_FACTORY_EXTENDER_PROPERTY_GROUP)
                            .append("=").append(factoryId).append(")");
                }
            }
            this.filterBuilder.append(")");

        } else if (finder.isAnnotationPresent(UsingSteps.class)) {
            List<Class<Object>> stepsClasses = finder.getAnnotatedClasses(
                    UsingSteps.class, Object.class, "instances");
            this.filterBuilder = new StringBuilder("(&");
            this.filterBuilder.append("(")
                    .append(org.osgi.framework.Constants.OBJECTCLASS)
                    .append("=")
                    .append(InjectableStepsFactoryService.class.getName())
                    .append(")");
            this.filterBuilder.append("(").append(Constants.EXTENDEE_BUNDLE)
                    .append("=").append(extendeeBundle.getSymbolicName())
                    .append(")");
            this.filterBuilder.append("(")
                    .append(Constants.EXTENDEE_BUNDLE_VERSION).append("=")
                    .append(extendeeBundle.getVersion().toString()).append(")");

            for (Class<Object> stepsClass : stepsClasses) {
                // build the LDAP filter
                filterBuilder.append("(")
                        .append(Constants.STEP_FACTORY_EXTENDER_PROPERTY_ITEM)
                        .append("=").append(stepsClass.getName()).append(")");
            }
            filterBuilder.append(")");
        }

        properties.put(
                Constants.STORY_RUNNER_SERVICE_STEP_FACTORY_SERVICE_TARGET,
                filterBuilder.toString());

        return properties;
    }

    private StorySearchDTO buildStorySearchModel() {
        if (!finder.isAnnotationPresent(UsingPaths.class)) {
            return null;
        }
        StorySearchDTO storySearchDTO = new StorySearchDTO();
        storySearchDTO.setSearchIn(finder.getAnnotatedValue(UsingPaths.class,
                String.class, "searchIn"));
        storySearchDTO.setStoryFinder(finder.getAnnotatedValue(
                UsingPaths.class, Class.class, "storyFinder").getName());
        storySearchDTO.setIncludes(finder.getAnnotatedValues(UsingPaths.class,
                String.class, "includes"));
        storySearchDTO.setExcludes(finder.getAnnotatedValues(UsingPaths.class,
                String.class, "excludes"));

        return storySearchDTO;
    }

    private List<String> parameterConverters() {
        List<String> converters = new ArrayList<String>();
        for (Class<ParameterConverter> converterClass : finder
                .getAnnotatedClasses(Configure.class, ParameterConverter.class,
                        "parameterConverters")) {
            converters.add(converterClass.getName());
        }
        return converters;
    }

    private void transformToProperties(Object object, String prefix,
            Dictionary<String, Object> mapResult) {
        if (object == null)
            return;

        for (Method method : object.getClass().getDeclaredMethods()) {
            if (Modifier.isPublic(method.getModifiers())
                    && method.getParameterTypes().length == 0
                    && method.getReturnType() != void.class
                    && method.getName() != "toString") {

                String name = method.getName().substring(
                        method.getName().indexOf("get") + 3);
                name = Introspector.decapitalize(name);
                Object value;
                try {
                    value = method.invoke(object);
                    if (value == null)
                        continue;
                    if (Collection.class.isAssignableFrom(value.getClass())){
                        int size = (Integer) value.getClass().getMethod("size").invoke(value);
                        if (size == 0)
                            continue;
                    }
                    if (value.getClass().isArray()) {
                        int size = java.lang.reflect.Array.getLength(value);
                        if (size == 0)
                            continue;
                    }
                    mapResult.put(prefix + "." + name, value);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
        }
    }
}
