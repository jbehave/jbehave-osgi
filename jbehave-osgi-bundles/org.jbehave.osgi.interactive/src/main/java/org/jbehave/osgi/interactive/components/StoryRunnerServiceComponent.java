package org.jbehave.osgi.interactive.components;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;

import org.jbehave.core.ConfigurableEmbedder;
import org.jbehave.core.configuration.AnnotationBuilder.InstantiationFailed;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.Keywords;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.embedder.EmbedderControls;
import org.jbehave.core.embedder.EmbedderMonitor;
import org.jbehave.core.embedder.MetaFilter;
import org.jbehave.core.embedder.NullEmbedderMonitor;
import org.jbehave.core.embedder.StoryControls;
import org.jbehave.core.failures.BatchFailures;
import org.jbehave.core.failures.FailureStrategy;
import org.jbehave.core.failures.PendingStepStrategy;
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
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.jbehave.core.steps.ParameterControls;
import org.jbehave.core.steps.ParameterConverters;
import org.jbehave.core.steps.ParameterConverters.ParameterConverter;
import org.jbehave.core.steps.StepCollector;
import org.jbehave.core.steps.StepFinder;
import org.jbehave.core.steps.StepMonitor;
import org.jbehave.osgi.interactive.config.EmbedderPropertiesBuilder;
import org.jbehave.osgi.interactive.services.StoryRunnerService;
import org.osgi.framework.Bundle;
import org.osgi.service.component.ComponentContext;

import com.thoughtworks.paranamer.Paranamer;

/**
 * 
 * @author Cristiano Gavião
 *
 */
public class StoryRunnerServiceComponent extends AbstractComponent implements
		StoryRunnerService {

	private class OsgiEmbedderMonitor extends NullEmbedderMonitor {

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

	private List<InjectableStepsFactory> injectableStepsFactories = new CopyOnWriteArrayList<InjectableStepsFactory>();

	private Class<?> storyClass;

	private Bundle storyBundle;

	private volatile Embedder embedder;

	private StoryFinder storyFinder;

	private String searchIn;

	private List<String> includes;

	private List<String> excludes;

	@Override
	protected void activate(ComponentContext context) {
		super.activate(context);

		logDebug("An Story Runner Service was registered for "
				+ getStoryClassName());

	}

	protected void bindInjectableStepsFactory(
			InjectableStepsFactory injectableStepsFactory) {
		if (injectableStepsFactory != null) {
			injectableStepsFactories.add(injectableStepsFactory);
			refreshCompositeStepsFactory();
		}
	}

	private Configuration buildConfiguration() {
		Configuration configuration = configurationProperty(
				"configurationClass", Configuration.class);
		configuration.useKeywords(configurationProperty("keywords",
				Keywords.class));
		configuration.useFailureStrategy(configurationProperty(
				"failureStrategy", FailureStrategy.class));
		configuration.usePendingStepStrategy(configurationProperty(
				"pendingStepStrategy", PendingStepStrategy.class));
		configuration.useParanamer(configurationProperty("paranamer",
				Paranamer.class));
		configuration.useStoryControls(configurationProperty("storyControls",
				StoryControls.class));
		configuration.useStepCollector(configurationProperty("stepCollector",
				StepCollector.class));
		configuration.useStepdocReporter(configurationProperty(
				"stepdocReporter", StepdocReporter.class));
		configuration.useStepFinder(configurationProperty("stepFinder",
				StepFinder.class));
		configuration.useStepMonitor(configurationProperty("stepMonitor",
				StepMonitor.class));
		configuration.useStepPatternParser(configurationProperty(
				"stepPatternParser", StepPatternParser.class));
		configuration.useStoryLoader(configurationProperty("storyLoader",
				StoryLoader.class));
		configuration.useStoryParser(configurationProperty("storyParser",
				StoryParser.class));
		configuration.useStoryPathResolver(configurationProperty(
				"storyPathResolver", StoryPathResolver.class));
		configuration.useStoryReporterBuilder(configurationProperty(
				"storyReporterBuilder", StoryReporterBuilder.class));
		configuration.useViewGenerator(configurationProperty("viewGenerator",
				ViewGenerator.class));
		configuration.useParameterControls(configurationProperty(
				"parameterControls", ParameterControls.class));
		configuration.usePathCalculator(configurationProperty("pathCalculator",
				PathCalculator.class));
		configuration.useParameterConverters(buildParameterConverters());
		return configuration;
	}

	private ParameterConverters buildParameterConverters() {
		List<ParameterConverter> converters = new ArrayList<ParameterConverter>();
		List<String> modelConverters = getPropertyValues(String.class,
				EmbedderPropertiesBuilder.PREFIX_CONFIGURATION
						+ ".parameterConverters");
		for (String converterClassName : modelConverters) {
			Class<ParameterConverter> implementation = loadClass(
					converterClassName, ParameterConverter.class);
			converters
					.add(instanceOf(ParameterConverter.class, implementation));
		}
		return new ParameterConverters().addConverters(converters);
	}

	private Embedder buildEmbedder() {
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

		Embedder embedder = embedder();
		embedder.embedderControls().doBatch(batch).doSkip(skip)
				.doGenerateViewAfterStories(generateViewAfterStories)
				.doIgnoreFailureInStories(ignoreFailureInStories)
				.doIgnoreFailureInView(ignoreFailureInView)
				.doVerboseFailures(verboseFailures)
				.doVerboseFiltering(verboseFiltering)
				.useStoryTimeoutInSecs(storyTimeoutInSecs).useThreads(threads);
		Configuration configuration = buildConfiguration();

		buildStorySearchModel();

		embedder.useStepsFactory(buildStepFactories(configuration));
		embedder.useConfiguration(configuration);
		embedder.useEmbedderMonitor(embedderMonitor());
		return embedder;
	}

	private InjectableStepsFactory buildStepFactories(
			Configuration configuration) {
		List<Object> stepsInstances = new ArrayList<Object>();
		List<String> modelSteps = getPropertyValues(String.class,
				EmbedderPropertiesBuilder.PREFIX_EMBEDDER + ".stepClasses");
		for (String stepClassName : modelSteps) {
			Class<Object> implementation = loadClass(stepClassName,
					Object.class);
			stepsInstances.add(instanceOf(Object.class, implementation));
		}
		return new InstanceStepsFactory(configuration, stepsInstances);

	}

	private void buildStorySearchModel() {
		setSearchIn(getPropertyValue(String.class,
				EmbedderPropertiesBuilder.PREFIX_STORY_SEARCH + ".searchIn"));
		String storyFinderName = getPropertyValue(String.class,
				EmbedderPropertiesBuilder.PREFIX_STORY_SEARCH + ".storyFinder");
		setIncludes(getPropertyValues(String.class,
				EmbedderPropertiesBuilder.PREFIX_STORY_SEARCH + ".includes"));
		setExcludes(getPropertyValues(String.class,
				EmbedderPropertiesBuilder.PREFIX_STORY_SEARCH + ".excludes"));

		Class<StoryFinder> implementation = loadClass(storyFinderName,
				StoryFinder.class);
		storyFinder = instanceOf(StoryFinder.class, implementation);
	}

	private <T> T controlProperty(Class<T> type, String name) {
		return getPropertyValue(type,
				EmbedderPropertiesBuilder.PREFIX_EMBEDDER + "." + name);
	}

	private <T> T configurationProperty(String name, Class<T> type) {

		String className = getPropertyValue(String.class,
				EmbedderPropertiesBuilder.PREFIX_CONFIGURATION + "." + name);
		Class<T> implementation = loadClass(className, type);
		return instanceOf(type, implementation);
	}

	@Override
	protected void deactivate(ComponentContext context) {
		super.deactivate(context);
		embedder = null;
		storyBundle = null;
	}

	private Embedder embedder() {
		String className = getPropertyValue(String.class,
				EmbedderPropertiesBuilder.PREFIX_EMBEDDER + ".embedderClass");
		Class<Embedder> implementation = loadClass(className, Embedder.class);
		return instanceOf(Embedder.class, implementation);
	}

	private EmbedderMonitor embedderMonitor() {
		return new OsgiEmbedderMonitor();
	}

	@SuppressWarnings("unchecked")
	private <T> T getPropertyValue(Class<T> memberType, String memberName) {

		Object value = getProperties().get(memberName);
		return (T) value;
	}

	@SuppressWarnings("unchecked")
	private <T> List<T> getPropertyValues(Class<T> type, String memberName) {
		ArrayList<T> result = new ArrayList<T>();
		List<T> csvString = (List<T>) getProperties().get(memberName);
		if (csvString == null || csvString.isEmpty()) {
			logWarn("Could not find property with name '" + memberName + "'");
			return result;
		}
		return csvString;
	}

	@Override
	public Dictionary<?, ?> getConfigurationProperties() {
		return getComponentContext().getProperties();
	}

	private Bundle getStoryBundle() {
		return storyBundle;
	}

	@Override
	public String getStoryBundleName() {
		String extenderBundleProperty = (String) getProperties().get(
				"extender.bundleProperty");
		return (String) getProperties().get(extenderBundleProperty);
	}

	@Override
	public String getStoryClassName() {
		String extenderItemProperty = (String) getProperties().get(
				"extender.itemProperty");
		return (String) getProperties().get(extenderItemProperty);
	}

	@Override
	public String getStoryType() {
		String extenderItemClassifierProperty = (String) getProperties().get(
				"extender.itemClassifierProperty");
		return (String) getProperties().get(extenderItemClassifierProperty);
	}

	private <T, V extends T> T instanceOf(Class<T> type, Class<V> ofClass) {
		try {
			try {
				Constructor<V> constructor = ofClass
						.getConstructor(new Class<?>[] { ClassLoader.class });
				return constructor.newInstance(getStoryBundle().getClass()
						.getClassLoader());
			} catch (NoSuchMethodException ns) {
			}
			try {
				Constructor<V> constructor = ofClass
						.getConstructor(new Class<?>[] { Class.class });
				return constructor.newInstance(storyClass);
			} catch (NoSuchMethodException ns) {
			}

			return ofClass.newInstance();
		} catch (Exception e) {
			// annotationMonitor.elementCreationFailed(ofClass, e);
			throw new InstantiationFailed(ofClass, type, e);
		}

	}

	@SuppressWarnings("unchecked")
	private <T> Class<T> loadClass(String className, Class<T> memberType) {
		try {
			return (Class<T>) getStoryBundle().loadClass(className);
		} catch (ClassNotFoundException e) {
			logError("Error on loading class " + className, e);
		}
		throw new RuntimeException();
		// throw new AnnotationRequired(annotatedClass, annotationClass);
	}

	@Override
	protected void modified(ComponentContext context) {
		super.modified(context);
		logDebug("Modified Story Runner Service for " + getStoryBundleName());

		updateConfiguredEmbedder();
	}

	private void refreshCompositeStepsFactory() {

		logDebug("Refresing CompositeStepFactory");
		// useStepsFactory(new CompositeStepsFactory(
		// injectableStepsFactories
		// .toArray(new InjectableStepsFactory[injectableStepsFactories
		// .size()])));
	}

	@Override
	public void run() {
		logInfo("Executing " + getStoryClassName() + " from "
				+ getStoryBundleName() + "...");
		embedder.runStoriesAsPaths(storyPaths());
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

	@Override
	public void setStoryBundle(Bundle storyBundle) {
		this.storyBundle = storyBundle;

		storyClass = loadClass(getStoryClassName(), Object.class);
		embedder = buildEmbedder();

		logDebug("Activated Story Runner Service for " + getStoryClassName());
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

	protected void unbindInjectableStepsFactory(
			InjectableStepsFactory injectableStepsFactory) {
		if (injectableStepsFactory != null) {
			injectableStepsFactories.remove(injectableStepsFactory);
			refreshCompositeStepsFactory();
		}
	}

	private void updateConfiguredEmbedder() {
		if (getStoryBundle() != null) {
			embedder = buildEmbedder();
		} else {
			logWarn("No StoryBundle object was configured for Story Runner Service ("
					+ getStoryBundleName() + ")");
		}

	}

	private String getSearchIn() {
		return searchIn;
	}

	private void setSearchIn(String searchIn) {
		this.searchIn = searchIn;
	}

	private List<String> getIncludes() {
		return includes;
	}

	private void setIncludes(List<String> includes) {
		this.includes = includes;
	}

	private List<String> getExcludes() {
		return excludes;
	}

	private void setExcludes(List<String> excludes) {
		this.excludes = excludes;
	}

}
