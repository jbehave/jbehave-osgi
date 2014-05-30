package org.jbehave.osgi.core.configuration;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.jbehave.core.annotations.Configure;
import org.jbehave.core.annotations.UsingEmbedder;
import org.jbehave.core.configuration.AnnotationBuilder;
import org.jbehave.core.configuration.AnnotationFinder;
import org.jbehave.core.configuration.AnnotationMonitor;
import org.jbehave.core.configuration.AnnotationRequired;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.Keywords;
import org.jbehave.core.configuration.PrintStreamAnnotationMonitor;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.embedder.StoryControls;
import org.jbehave.core.failures.FailureStrategy;
import org.jbehave.core.failures.PendingStepStrategy;
import org.jbehave.core.io.PathCalculator;
import org.jbehave.core.io.StoryLoader;
import org.jbehave.core.io.StoryPathResolver;
import org.jbehave.core.parsers.StepPatternParser;
import org.jbehave.core.parsers.StoryParser;
import org.jbehave.core.reporters.StepdocReporter;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.reporters.ViewGenerator;
import org.jbehave.core.steps.MarkUnmatchedStepsAsPending;
import org.jbehave.core.steps.ParameterControls;
import org.jbehave.core.steps.PrintStreamStepMonitor;
import org.jbehave.core.steps.StepCollector;
import org.jbehave.core.steps.StepFinder;
import org.jbehave.core.steps.StepMonitor;
import org.jbehave.osgi.core.embedder.EmbedderOsgi;
import org.jbehave.osgi.core.io.LoadFromBundleClasspath;
import org.jbehave.osgi.core.reporters.StoryReporterBuilderOsgi;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import com.thoughtworks.paranamer.Paranamer;

public class AnnotationBuilderOsgi extends AnnotationBuilder {

    private final Bundle ownerBundle;
    private volatile Configuration configuration;

    public AnnotationBuilderOsgi(Class<?> annotatedClass) {
        this(annotatedClass, new PrintStreamAnnotationMonitor());
    }

    public AnnotationBuilderOsgi(Class<?> annotatedClass,
            AnnotationMonitor annotationMonitor) {
        super(annotatedClass, annotationMonitor);
        ownerBundle = FrameworkUtil.getBundle(annotatedClass);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Embedder embedder() {
        Class<? extends Embedder> clazz = finder().getAnnotatedValue(
                UsingEmbedder.class, Class.class, "embedder");
        if (clazz.isAssignableFrom(EmbedderOsgi.class)) {
            return instanceOf(Embedder.class, clazz);
        }
        return defaultEmbedder();
    }

    @Override
    protected Embedder defaultEmbedder() {
        return new EmbedderOsgi(ownerBundle);
    }

    public Configuration buildConfiguration() throws AnnotationRequired {

        if (!finder().isAnnotationPresent(Configure.class)) {
            // not using annotation configuration, default to most useful
            // configuration
            return new ConfigurationOsgi(ownerBundle);
        }
        configuration = configurationElement(finder(), "using",
                ConfigurationOsgi.class, ConfigurationOsgi.class);
        configuration.useKeywords(configurationElement(finder(), "keywords",
                Keywords.class));
        configuration.useFailureStrategy(configurationElement(finder(),
                "failureStrategy", FailureStrategy.class));
        configuration.usePendingStepStrategy(configurationElement(finder(),
                "pendingStepStrategy", PendingStepStrategy.class));
        configuration.useParanamer(configurationElement(finder(), "paranamer",
                Paranamer.class));
        configuration.useStoryControls(configurationElement(finder(),
                "storyControls", StoryControls.class));
        configuration.useStepCollector(configurationElement(finder(),
                "stepCollector", StepCollector.class,
                MarkUnmatchedStepsAsPending.class));
        configuration.useStepdocReporter(configurationElement(finder(),
                "stepdocReporter", StepdocReporter.class));
        configuration.useStepFinder(configurationElement(finder(),
                "stepFinder", StepFinder.class));
        configuration
                .useStepMonitor(configurationElement(finder(), "stepMonitor",
                        StepMonitor.class, PrintStreamStepMonitor.class));
        configuration.useStepPatternParser(configurationElement(finder(),
                "stepPatternParser", StepPatternParser.class));
        configuration
                .useStoryLoader(configurationElement(finder(), "storyLoader",
                        StoryLoader.class, LoadFromBundleClasspath.class));
        configuration.useStoryParser(configurationElement(finder(),
                "storyParser", StoryParser.class));
        configuration.useStoryPathResolver(configurationElement(finder(),
                "storyPathResolver", StoryPathResolver.class));
        configuration.useStoryReporterBuilder(configurationElement(finder(),
                "storyReporterBuilder", StoryReporterBuilder.class,
                StoryReporterBuilderOsgi.class));
        configuration.useViewGenerator(configurationElement(finder(),
                "viewGenerator", ViewGenerator.class));
        configuration.useParameterConverters(parameterConverters(finder()));
        configuration.useParameterControls(configurationElement(finder(),
                "parameterControls", ParameterControls.class));
        configuration.usePathCalculator(configurationElement(finder(),
                "pathCalculator", PathCalculator.class));
        return configuration;
    }

    protected <T, I extends T> T configurationElement(AnnotationFinder finder,
            String name, Class<T> returnType) {
        return configurationElement(finder, name, returnType, null);
    }

    protected <T, I extends T> T configurationElement(AnnotationFinder finder,
            String name, Class<T> returnType, Class<I> defaultImplementation) {
        Class<T> implementation = elementImplementation(finder, name);

        if (defaultImplementation != null) {
            if (isDefaultConfigureItemImplementation(name, implementation)) {
                return instanceOf(returnType, defaultImplementation);
            } else {
                return instanceOf(returnType, implementation);
            }
        } else {
            if (returnType.isAssignableFrom(implementation)) {
                return instanceOf(returnType, implementation);
            }
        }
        return null;
    }

    /**
     * Checks if an implementation class passes as parameter is the same as the
     * one hard-coded in {@link Configure} annotation.
     * 
     * @param configureClassProperty
     *            the {@link Configure} annotation property.
     * @param implementation
     *            The implementation class being checked.
     * @return the result of the check.
     */
    public static boolean isDefaultConfigureItemImplementation(
            String configureClassProperty, Class<?> implementation) {
        Method method;
        try {
            method = Configure.class.getDeclaredMethod(configureClassProperty);
            Class<?> value = (Class<?>) method.getDefaultValue();
            if (value.getName().equals(implementation.getName())) {
                return true;
            }
        } catch (NoSuchMethodException e) {
        } catch (SecurityException e2) {
        }
        return false;
    }

    @Override
    protected <T, V extends T> T instanceOf(Class<T> type, Class<V> ofClass) {
        try {
            try {
                Constructor<V> constructor = ofClass
                        .getConstructor(new Class<?>[] { Bundle.class,
                                Configuration.class });
                return constructor.newInstance(ownerBundle, configuration());
            } catch (NoSuchMethodException ns) {
            }
            try {
                Constructor<V> constructor = ofClass
                        .getConstructor(new Class<?>[] { Bundle.class });
                return constructor.newInstance(ownerBundle);
            } catch (NoSuchMethodException ns) {
            }
            try {
                Constructor<V> constructor = ofClass
                        .getConstructor(new Class<?>[] { Configuration.class });
                return constructor.newInstance(configuration());
            } catch (NoSuchMethodException ns) {
            }
            // by class instance
            return ofClass.newInstance();
        } catch (Exception e) {
            annotationMonitor().elementCreationFailed(ofClass, e);
            throw new InstantiationFailed(ofClass, type, e);
        }
    }

    private Configuration configuration() {
        return configuration;
    }
}
