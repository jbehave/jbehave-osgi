package org.jbehave.osgi.core.components;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

import org.jbehave.core.ConfigurableEmbedder;
import org.jbehave.core.annotations.Configure;
import org.jbehave.core.annotations.UsingEmbedder;
import org.jbehave.core.configuration.Keywords;
import org.jbehave.core.embedder.EmbedderControls;
import org.jbehave.core.embedder.MetaFilter;
import org.jbehave.core.embedder.NullEmbedderMonitor;
import org.jbehave.core.embedder.StoryControls;
import org.jbehave.core.failures.BatchFailures;
import org.jbehave.core.failures.FailureStrategy;
import org.jbehave.core.failures.PendingStepStrategy;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.PathCalculator;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.io.StoryLoader;
import org.jbehave.core.io.StoryPathResolver;
import org.jbehave.core.model.Meta;
import org.jbehave.core.model.Story;
import org.jbehave.core.model.StoryDuration;
import org.jbehave.core.model.StoryMaps;
import org.jbehave.core.parsers.StepPatternParser;
import org.jbehave.core.parsers.StoryParser;
import org.jbehave.core.reporters.ReportsCount;
import org.jbehave.core.reporters.StepdocReporter;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.reporters.ViewGenerator;
import org.jbehave.core.steps.ParameterControls;
import org.jbehave.core.steps.ParameterConverters;
import org.jbehave.core.steps.ParameterConverters.ParameterConverter;
import org.jbehave.core.steps.PrintStreamStepMonitor;
import org.jbehave.core.steps.StepCollector;
import org.jbehave.core.steps.StepFinder;
import org.jbehave.core.steps.StepMonitor;
import org.jbehave.osgi.core.Constants;
import org.jbehave.osgi.core.configuration.AnnotationBuilderOsgi;
import org.jbehave.osgi.core.configuration.ConfigurationOsgi;
import org.jbehave.osgi.core.embedder.EmbedderOsgi;
import org.jbehave.osgi.core.io.LoadFromBundleClasspath;
import org.jbehave.osgi.core.reporters.StoryReporterBuilderOsgi;
import org.jbehave.osgi.core.services.InjectableStepsFactoryService;
import org.jbehave.osgi.core.services.StoryRunnerService;
import org.osgi.framework.Bundle;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.log.LogService;

import com.thoughtworks.paranamer.Paranamer;

/**
 * Represents a runner service that will be associated to a JBehave
 * {@link EmbedderOsgi}.
 * 
 * @author Cristiano Gavião
 */
@Component(enabled = true, configurationPid = Constants.STORY_RUNNER_FACTORY_FPID, service = { StoryRunnerService.class }, configurationPolicy = ConfigurationPolicy.REQUIRE)
public class StoryRunnerServiceFactoryComponent extends
        AbstractServiceComponent implements StoryRunnerService {

    private class OsgiLogEmbedderMonitor extends NullEmbedderMonitor {

        public void annotatedInstanceNotOfType(Object annotatedInstance,
                Class<?> type) {
            logWarn("Annotated instance " + annotatedInstance + " not of type "
                    + type);
        }

        public void batchFailed(BatchFailures failures) {
            logWarn("Failed to run batch " + failures);
        }

        public void beforeOrAfterStoriesFailed() {
            logWarn("Failed to run before or after stories steps");
        }

        public void embeddableFailed(String name, Throwable cause) {
            logError("Failed to run embeddable " + name, cause);
        }

        public void embeddableNotConfigurable(String name) {
            logWarn("Embeddable " + name + " must be an instance of "
                    + ConfigurableEmbedder.class);
        }

        public void embeddablesSkipped(List<String> classNames) {
            logInfo("Skipped embeddables " + classNames);
        }

        public void generatingMapsView(File outputDirectory,
                StoryMaps storyMaps, Properties viewProperties) {
            logInfo("Generating maps view to '" + outputDirectory
                    + "' using story maps '" + storyMaps + "'"
                    + " and view properties '" + viewProperties + "'");
        }

        public void generatingNavigatorView(File outputDirectory,
                Properties viewProperties) {
            logInfo("Generating navigator view to '" + outputDirectory
                    + "' using view properties '" + viewProperties + "'");
        }

        public void generatingReportsView(File outputDirectory,
                List<String> formats, Properties viewProperties) {
            logInfo("Generating reports view to '" + outputDirectory
                    + "' using formats '" + formats + "'"
                    + " and view properties '" + viewProperties + "'");
        }

        public void mappingStory(String storyPath, List<String> metaFilters) {
            logInfo("Mapping story " + storyPath + " with meta filters "
                    + metaFilters);
        }

        public void mapsViewGenerationFailed(File outputDirectory,
                StoryMaps storyMaps, Properties viewProperties, Throwable cause) {
            logError("Failed to generate maps view to '" + outputDirectory
                    + "' using story maps '" + storyMaps + "'"
                    + " and view properties '" + viewProperties + "'", cause);
        }

        public void metaNotAllowed(Meta meta, MetaFilter filter) {
            logDebug(meta + " excluded by filter '" + filter.asString() + "'");
        }

        public void navigatorViewGenerationFailed(File outputDirectory,
                Properties viewProperties, Throwable cause) {
            logError("Failed to generate navigator view to '" + outputDirectory
                    + "' using view properties '" + viewProperties + "'", cause);
        }

        public void navigatorViewNotGenerated() {
            logWarn("Navigator view not generated, as the CrossReference has not been declared in the StoryReporterBuilder");
        }

        public void processingSystemProperties(Properties properties) {
            logInfo("Processing system properties " + properties);
        }

        public void reportsViewFailures(ReportsCount count) {
            logWarn("Failures in reports view: " + count.getScenariosFailed()
                    + " scenarios failed");
        }

        public void reportsViewGenerated(ReportsCount count) {
            logInfo("Reports view generated with " + count.getStories()
                    + " stories (of which " + count.getStoriesPending()
                    + " pending) containing " + count.getScenarios()
                    + " scenarios (of which " + count.getScenariosPending()
                    + " pending)");
            if (count.getStoriesNotAllowed() > 0
                    || count.getScenariosNotAllowed() > 0) {
                logInfo("Meta filters excluded " + count.getStoriesNotAllowed()
                        + " stories and  " + count.getScenariosNotAllowed()
                        + " scenarios");
            }
        }

        public void reportsViewGenerationFailed(File outputDirectory,
                List<String> formats, Properties viewProperties, Throwable cause) {
            String message = "Failed to generate reports view to '"
                    + outputDirectory + "' using formats '" + formats + "'"
                    + " and view properties '" + viewProperties + "'";
            logError(message, cause);
        }

        public void reportsViewNotGenerated() {
            logInfo("Reports view not generated");
        }

        public void runningEmbeddable(String name) {
            logInfo("Running embeddable " + name);
        }

        public void runningStory(String path) {
            logInfo("Running story " + path);
        }

        public void runningWithAnnotatedEmbedderRunner(String className) {
            logInfo("Running with AnnotatedEmbedderRunner '" + className + "'");
        }

        public void storiesNotAllowed(List<Story> stories, MetaFilter filter,
                boolean verbose) {
            StringBuffer sb = new StringBuffer();
            sb.append(stories.size() + " stories excluded by filter: "
                    + filter.asString() + "\n");
            if (verbose) {
                for (Story story : stories) {
                    sb.append(story.getPath()).append("\n");
                }
            }
            logInfo(sb.toString());
        }

        public void storiesSkipped(List<String> storyPaths) {
            logInfo("Skipped stories " + storyPaths);
        }

        public void storyFailed(String path, Throwable cause) {
            logError("Failed to run story " + path, cause);
        }

        public void storyTimeout(Story story, StoryDuration storyDuration) {
            logWarn("Story " + story.getPath() + " duration of "
                    + storyDuration.getDurationInSecs()
                    + " seconds has exceeded timeout of "
                    + storyDuration.getTimeoutInSecs() + " seconds");
        }

        public void systemPropertySet(String name, String value) {
            logInfo("System property '" + name + "' set to '" + value + "'");
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }

        public void usingControls(EmbedderControls embedderControls) {
            logInfo("Using controls " + embedderControls);
        }

        public void usingExecutorService(ExecutorService executorService) {
            logInfo("Using executor service " + executorService);
        }

        public void usingThreads(int threads) {
            logInfo("Using " + threads + " threads");
        }

    }

    private Class<?> storyClass;

    private volatile EmbedderOsgi embedder;

    private StoryFinder storyFinder;

    private String searchIn;

    private List<String> includes;

    private List<String> excludes;

    private String[] storyType = null;

    private final Queue<InjectableStepsFactoryService> injectableStepsFactoriesQueue = new ConcurrentLinkedQueue<InjectableStepsFactoryService>();

    @Activate
    @Override
    protected void activate(ComponentContext context) {
        super.activate(context);

        try {
            initializeEmbedder(context);
        } catch (Exception e) {
            logError("Could not register Story Runner Service for: "
                    + getStoryClassName() + " from bundle: "
                    + getExtendeeBundleName(), e);
        }
        if (embedder != null) {
            logDebug("An Story Runner Service instance was activated for: "
                    + getStoryClassName() + " from bundle: "
                    + getExtendeeBundleName());
        }
    }

    @Reference
    @Override
    protected void bindLogService(LogService logService) {
        super.bindLogService(logService);
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MULTIPLE)
    protected void bindInjectableStepsFactoryService(
            InjectableStepsFactoryService injectableStepsFactoryService) {

        if (injectableStepsFactoryService != null) {
            // save the object since this method is called before than the
            // activate() and embedder was not created yet.
            if (embedder == null) {
                injectableStepsFactoriesQueue
                        .add(injectableStepsFactoryService);
            } else {
                embedder.addInjectableStepsFactoryService(injectableStepsFactoryService);
            }
        }
    }

    @Override
    public List<String> boundStepFactories() {
        List<String> services = new ArrayList<String>();
        for (InjectableStepsFactoryService service : embedder
                .injectableStepsFactories()) {
            services.add(service.getStepFactoryId());
        }
        return services;
    }

    private void buildConfiguration(EmbedderOsgi embedder) throws Exception {
        setConfiguration(configurationProperty("configurationClass",
                ConfigurationOsgi.class));
        embedder.useConfiguration(configuration());
        configuration().useKeywords(
                configurationProperty("keywords", Keywords.class));
        configuration().useStoryLoader(
                configurationProperty("storyLoader", StoryLoader.class,
                        LoadFromBundleClasspath.class));
        configuration()
                .useFailureStrategy(
                        configurationProperty("failureStrategy",
                                FailureStrategy.class));
        configuration().usePendingStepStrategy(
                configurationProperty("pendingStepStrategy",
                        PendingStepStrategy.class));
        configuration().useParanamer(
                configurationProperty("paranamer", Paranamer.class));
        configuration().useStoryControls(
                configurationProperty("storyControls", StoryControls.class));
        configuration().useStepCollector(
                configurationProperty("stepCollector", StepCollector.class));
        configuration()
                .useStepdocReporter(
                        configurationProperty("stepdocReporter",
                                StepdocReporter.class));
        configuration().useStepFinder(
                configurationProperty("stepFinder", StepFinder.class));
        configuration().useStepMonitor(
                configurationProperty("stepMonitor", StepMonitor.class,
                        PrintStreamStepMonitor.class));
        configuration().useStepPatternParser(
                configurationProperty("stepPatternParser",
                        StepPatternParser.class));
        configuration().useStoryParser(
                configurationProperty("storyParser", StoryParser.class));
        configuration().useStoryPathResolver(
                configurationProperty("storyPathResolver",
                        StoryPathResolver.class));
        configuration().useStoryReporterBuilder(
                configurationProperty("storyReporterBuilder",
                        StoryReporterBuilder.class,
                        StoryReporterBuilderOsgi.class));
        configuration().useViewGenerator(
                configurationProperty("viewGenerator", ViewGenerator.class));
        configuration().useParameterControls(
                configurationProperty("parameterControls",
                        ParameterControls.class));
        configuration().usePathCalculator(
                configurationProperty("pathCalculator", PathCalculator.class));
        configuration().useParameterConverters(buildParameterConverters());
    }

    private EmbedderOsgi buildEmbedder(Bundle ownerBundle) throws Exception {

        if (ownerBundle == null) {
            return null;
        }
        boolean batch = controlProperty(Boolean.class, "batch");
        boolean skip = controlProperty(Boolean.class, "skip");
        boolean generateViewAfterStories = controlProperty(Boolean.class,
                "generateViewAfterStories");
        boolean ignoreFailureInStories = controlProperty(Boolean.class,
                "ignoreFailureInStories");
        boolean ignoreFailureInView = controlProperty(Boolean.class,
                "ignoreFailureInView");
        boolean verboseFailures = controlProperty(Boolean.class,
                "verboseFailures");
        boolean verboseFiltering = controlProperty(Boolean.class,
                "verboseFiltering");
        long storyTimeoutInSecs = controlProperty(Long.class,
                "storyTimeoutInSecs");
        int threads = controlProperty(Integer.class, "threads");

        String className = getPropertyValue(String.class,
                Constants.DTO_PREFIX_EMBEDDER + ".embedderClass");
        Class<EmbedderOsgi> implementation = loadClass(className,
                EmbedderOsgi.class);
        EmbedderOsgi embedder = instanceOf(EmbedderOsgi.class, implementation);
        embedder.useEmbedderMonitor(new OsgiLogEmbedderMonitor());
        embedder.embedderControls().doBatch(batch).doSkip(skip)
                .doGenerateViewAfterStories(generateViewAfterStories)
                .doIgnoreFailureInStories(ignoreFailureInStories)
                .doIgnoreFailureInView(ignoreFailureInView)
                .doVerboseFailures(verboseFailures)
                .doVerboseFiltering(verboseFiltering)
                .useStoryTimeoutInSecs(storyTimeoutInSecs).useThreads(threads);

        buildConfiguration(embedder);

        buildStorySearchModel();

        return embedder;
    }

    private ParameterConverters buildParameterConverters() throws Exception {
        List<ParameterConverter> converters = new ArrayList<ParameterConverter>();
        List<String> modelConverters = getPropertyValues(String.class,
                Constants.DTO_PREFIX_CONFIGURATION + ".parameterConverters");
        for (String converterClassName : modelConverters) {
            Class<ParameterConverter> implementation = loadClass(
                    converterClassName, ParameterConverter.class);
            converters
                    .add(instanceOf(ParameterConverter.class, implementation));
        }
        return new ParameterConverters().addConverters(converters);
    }

    private void buildStorySearchModel() throws ClassNotFoundException {
        setSearchIn(getPropertyValue(String.class,
                Constants.DTO_PREFIX_STORY_SEARCH + ".searchIn"));
        String storyFinderName = getPropertyValue(String.class,
                Constants.DTO_PREFIX_STORY_SEARCH + ".storyFinder");
        setIncludes(getPropertyValues(String.class,
                Constants.DTO_PREFIX_STORY_SEARCH + ".includes"));
        setExcludes(getPropertyValues(String.class,
                Constants.DTO_PREFIX_STORY_SEARCH + ".excludes"));
        Class<StoryFinder> implementation = loadClass(storyFinderName,
                StoryFinder.class);
        storyFinder = instanceOf(StoryFinder.class, implementation);
    }

    private <T> T configurationProperty(String name, Class<T> type)
            throws ClassNotFoundException {
        return configurationProperty(name, type, null);
    }

    /**
     * Added in order to provide a way to use a default class other than one
     * hard coded in @ {@link UsingEmbedder}.
     * 
     * @param classProperty
     *            The class name specified in the annotation or in the service
     *            property.
     * @param returnType
     *            The required class type.
     * @param newDefaultImplementation
     *            The default implementation to use instead of the ones defined
     *            in the JBehave core.
     * @return
     * @throws ClassNotFoundException
     */
    private <T, I extends T> T configurationProperty(String classProperty,
            Class<T> returnType, Class<I> newDefaultImplementation)
            throws ClassNotFoundException {
        String className = getPropertyValue(String.class,
                Constants.DTO_PREFIX_CONFIGURATION + "." + classProperty);
        Class<T> implementation = loadClass(className, returnType);
        if (newDefaultImplementation != null) {
            if (AnnotationBuilderOsgi.isDefaultConfigureItemImplementation(
                    classProperty, implementation) == false) {
                return instanceOf(returnType, implementation);
            } else {
                return instanceOf(returnType, newDefaultImplementation);
            }
        } else {
            return instanceOf(returnType, implementation);
        }
    }

    private <T> T controlProperty(Class<T> type, String name) {
        return getPropertyValue(type, Constants.DTO_PREFIX_EMBEDDER + "."
                + name);
    }

    @Override
    protected void deactivate(ComponentContext context) {
        super.deactivate(context);
        embedder = null;
    }

    @Override
    public Dictionary<String, ?> getConfigurationProperties() {
        return getComponentContext().getProperties();
    }

    private List<String> getExcludes() {
        return excludes;
    }

    private List<String> getIncludes() {
        return includes;
    }

    private String getSearchIn() {
        return searchIn;
    }

    @Override
    public String getStoryBundleName() {
        return getExtendeeBundleName();
    }

    @Override
    public String getStoryBundleVersion() {
        return getExtendeeBundleVersion();
    }

    public Class<?> getStoryClass() {
        return storyClass;
    }

    @Override
    public String getStoryClassName() {
        return getExtendeeID();
    }

    @Override
    public String[] getStoryType() {
        return storyType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((getStoryClassName() == null) ? 0 : getStoryClassName()
                        .hashCode());
        return result;
    }

    private void initializeEmbedder(ComponentContext context) throws Exception {

        Object storyClassNameObj = getProperties().get(
                Constants.STORY_RUNNER_EXTENDER_PROPERTY_ITEM);

        setExtendeeID(storyClassNameObj != null ? (String) storyClassNameObj
                : "no defined");

        Object storyTypeObj = getProperties().get(
                Constants.STORY_RUNNER_EXTENDER_PROPERTY_ITEM_CLASSIFIER);
        storyType = (storyTypeObj != null ? (String[]) storyTypeObj : null);

        embedder = buildEmbedder(getOwnerBundle());

        if (embedder == null) {
            logError("Couldn't build an embedder representing the class: "
                    + getStoryClassName(), null);
            return;
        }
        storyClass = loadClass(getStoryClassName(), Object.class);

        do {
            InjectableStepsFactoryService stepsFactoryService = injectableStepsFactoriesQueue
                    .poll();
            embedder.addInjectableStepsFactoryService(stepsFactoryService);
        } while (!injectableStepsFactoriesQueue.isEmpty());

    }

    protected boolean isDefaultImplementation(String configurationProperty,
            Class<?> implementation) {
        Method method;
        try {
            method = Configure.class.getDeclaredMethod(configurationProperty);
            String value = (String) method.getDefaultValue();
            if (value.equals(implementation.getClass().getName())) {
                return true;
            }
        } catch (NoSuchMethodException e) {
        } catch (SecurityException e2) {
        }
        return false;
    }

    @Override
    public List<String> listStoriesIn(String searchIn, String[] includes,
            String[] excludes) {
        List<String> result = null;
        if (embedder != null && storyFinder != null) {
            result = storyFinder.findPaths(searchIn, includes, excludes);
        }
        return result;
    }

    protected void modified(ComponentContext context) {

        super.modified(context);
        try {
            initializeEmbedder(context);
            if (embedder != null) {
                logDebug("The Story Runner Service for " + getStoryClassName()
                        + " was reinitialized.");
            }
        } catch (Exception e) {
            logError("Problem occurred when initializing StoryRunner Service",
                    e);
        }
    }

    @Override
    public String reportOutputLocation() {
        if (embedder != null) {
            return embedder.configuration().storyReporterBuilder()
                    .codeLocation().getPath();
        }
        return "";
    }

    @Override
    public void run() {
        logInfo("Executing " + getStoryClassName() + " from "
                + getStoryBundleName() + ".");
        embedder.runStoriesAsPaths(storyPaths());
    }

    @Override
    public void run(String reportOutputDir) throws Throwable {
        // Save the output location for this run.
        URL location = embedder.configuration().storyReporterBuilder()
                .codeLocation();
        embedder.configuration()
                .storyReporterBuilder()
                .withCodeLocation(
                        CodeLocations.codeLocationFromPath(reportOutputDir));
        run();
        // return to the old state.
        embedder.configuration().storyReporterBuilder()
                .withCodeLocation(location);
    }

    private void setExcludes(List<String> excludes) {
        this.excludes = excludes;
    }

    private void setIncludes(List<String> includes) {
        this.includes = includes;
    }

    private void setSearchIn(String searchIn) {
        this.searchIn = searchIn;
    }

    @Override
    public List<String> storyPaths() {
        List<String> result = null;
        if (embedder != null && storyFinder != null) {
            result = storyFinder.findPaths(getSearchIn(), getIncludes(),
                    getExcludes());
        }
        return result;
    }

    protected void unbindInjectableStepsFactoryService(
            InjectableStepsFactoryService injectableStepsFactoryService) {
        if (injectableStepsFactoryService != null && embedder != null) {
            embedder.removeInjectableStepsFactoryService(injectableStepsFactoryService);
        }
    }

    @Override
    public void useReportOutputLocation(String reportOutputLocation) {
        if (embedder != null) {
            embedder.configuration()
                    .storyReporterBuilder()
                    .withCodeLocation(
                            CodeLocations
                                    .codeLocationFromPath(reportOutputLocation));
        }
    }
}
