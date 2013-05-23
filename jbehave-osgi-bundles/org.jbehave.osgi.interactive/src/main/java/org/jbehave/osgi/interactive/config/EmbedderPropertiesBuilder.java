package org.jbehave.osgi.interactive.config;

import java.beans.Introspector;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
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

/**
 * Allows to transform an annotated {@link Embedder} class into a Map<String,
 * String> of properties that will be used to set an OSGi Configuration for a
 * Story Runner Service. The map of properties will be then used later to create
 * a {@link Embedder} object.
 * 
 * @author Cristiano Gavião
 */
public class EmbedderPropertiesBuilder {

	public static final String PREFIX_EMBEDDER = "embedder";
	public static final String PREFIX_CONFIGURATION = "configuration";
	public static final String PREFIX_STORY_SEARCH = "story.search";

	private final AnnotationFinder finder;

	public EmbedderPropertiesBuilder(Class<?> annotatedClass) {
		this.finder = new AnnotationFinder(annotatedClass);
	}

	/**
	 * Builds a ConfigurationModel instance based on annotation
	 * {@link Configure} found in the annotated object instance
	 * 
	 * @return A ConfigurationModel instance
	 */
	private ConfigurationModel buildConfigurationModel()
			throws AnnotationRequired {

		if (!finder.isAnnotationPresent(Configure.class)) {
			return null;
		}

		ConfigurationModel configuration = new ConfigurationModel();
		configuration.setConfigurationClass(finder.getAnnotatedValue(
				Configure.class, Class.class, "using").getName());
		configuration.setKeywords(finder.getAnnotatedValue(Configure.class,
				Class.class, "keywords").getName());
		configuration.setFailureStrategy(finder.getAnnotatedValue(
				Configure.class, Class.class, "failureStrategy").getName());
		configuration.setPendingStepStrategy(finder.getAnnotatedValue(
				Configure.class, Class.class, "pendingStepStrategy").getName());
		configuration.setParanamer(finder.getAnnotatedValue(Configure.class,
				Class.class, "paranamer").getName());
		configuration.setStoryControls(finder.getAnnotatedValue(
				Configure.class, Class.class, "storyControls").getName());
		configuration.setStepCollector(finder.getAnnotatedValue(
				Configure.class, Class.class, "stepCollector").getName());
		configuration.setStepdocReporter(finder.getAnnotatedValue(
				Configure.class, Class.class, "stepdocReporter").getName());
		configuration.setStepFinder(finder.getAnnotatedValue(Configure.class,
				Class.class, "stepFinder").getName());
		configuration.setStepMonitor(finder.getAnnotatedValue(Configure.class,
				Class.class, "stepMonitor").getName());
		configuration.setStepPatternParser(finder.getAnnotatedValue(
				Configure.class, Class.class, "stepPatternParser").getName());
		configuration.setStoryLoader(finder.getAnnotatedValue(Configure.class,
				Class.class, "storyLoader").getName());
		configuration.setStoryParser(finder.getAnnotatedValue(Configure.class,
				Class.class, "storyParser").getName());
		configuration.setStoryPathResolver(finder.getAnnotatedValue(
				Configure.class, Class.class, "storyPathResolver").getName());
		configuration
				.setStoryReporterBuilder(finder.getAnnotatedValue(
						Configure.class, Class.class, "storyReporterBuilder")
						.getName());
		configuration.setViewGenerator(finder.getAnnotatedValue(
				Configure.class, Class.class, "viewGenerator").getName());
		configuration.setParameterConverters(parameterConverters());
		configuration.setParameterControls(finder.getAnnotatedValue(
				Configure.class, Class.class, "parameterControls").getName());
		configuration.setPathCalculator(finder.getAnnotatedValue(
				Configure.class, Class.class, "pathCalculator").getName());
		return configuration;
	}

	private EmbedderModel buildEmbedderModel() {
		if (!finder.isAnnotationPresent(UsingEmbedder.class)) {
			return null;
		}
		EmbedderModel embedderModel = new EmbedderModel();
		embedderModel.setEmbedderClass(finder.getAnnotatedValue(
				UsingEmbedder.class, Class.class, "embedder").getName());
		embedderModel.setBatch(finder.getAnnotatedValue(UsingEmbedder.class,
				Boolean.class, "batch"));
		embedderModel.setSkip(finder.getAnnotatedValue(UsingEmbedder.class,
				Boolean.class, "skip"));
		embedderModel
				.setGenerateViewAfterStories(finder.getAnnotatedValue(
						UsingEmbedder.class, Boolean.class,
						"generateViewAfterStories"));
		embedderModel.setIgnoreFailureInStories(finder.getAnnotatedValue(
				UsingEmbedder.class, Boolean.class, "ignoreFailureInStories"));
		embedderModel.setIgnoreFailureInView(finder.getAnnotatedValue(
				UsingEmbedder.class, Boolean.class, "ignoreFailureInView"));
		embedderModel.setVerboseFailures(finder.getAnnotatedValue(
				UsingEmbedder.class, Boolean.class, "verboseFailures"));
		embedderModel.setVerboseFiltering(finder.getAnnotatedValue(
				UsingEmbedder.class, Boolean.class, "verboseFiltering"));
		embedderModel.setStoryTimeoutInSecs(finder.getAnnotatedValue(
				UsingEmbedder.class, Long.class, "storyTimeoutInSecs"));
		embedderModel.setThreads(finder.getAnnotatedValue(UsingEmbedder.class,
				Integer.class, "threads"));
		embedderModel.setMetaFilters(finder.getAnnotatedValues(
				UsingEmbedder.class, String.class, "metaFilters"));
		embedderModel.setSystemProperties(finder.getAnnotatedValue(
				UsingEmbedder.class, String.class, "systemProperties"));
		embedderModel.setStepClasses(stepClasses());

		return embedderModel;
	}

	public Dictionary<String, Object> buildEmbedderProperties() {
		Dictionary<String, Object> properties = new Hashtable<String, Object>();
		transformToProperties(buildEmbedderModel(), PREFIX_EMBEDDER, properties);
		transformToProperties(buildStorySearchModel(), PREFIX_STORY_SEARCH,
				properties);
		transformToProperties(buildConfigurationModel(), PREFIX_CONFIGURATION,
				properties);
		return properties;
	}

	public Embedder buildEmbedder(Dictionary<String, Object> properties) {
		Embedder embedder = null;

		return embedder;
	}

	public Embedder buildEmbedder(Embedder embedder,
			Dictionary<String, Object> properties) {

		return embedder;
	}

	private StorySearchModel buildStorySearchModel() {
		if (!finder.isAnnotationPresent(UsingPaths.class)) {
			return null;
		}
		StorySearchModel storySearchModel = new StorySearchModel();
		storySearchModel.setSearchIn(finder.getAnnotatedValue(UsingPaths.class,
				String.class, "searchIn"));
		storySearchModel.setStoryFinder(finder.getAnnotatedValue(
				UsingPaths.class, Class.class, "storyFinder").getName());
		storySearchModel.setIncludes(finder.getAnnotatedValues(
				UsingPaths.class, String.class, "includes"));
		storySearchModel.setExcludes(finder.getAnnotatedValues(
				UsingPaths.class, String.class, "excludes"));

		return storySearchModel;
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

	private List<String> stepClasses() {
		List<String> stepsClassNames = new ArrayList<String>();
		if (finder.isAnnotationPresent(UsingSteps.class)) {
			List<Class<Object>> stepsClasses = finder.getAnnotatedClasses(
					UsingSteps.class, Object.class, "instances");
			for (Class<Object> stepsClass : stepsClasses) {
				stepsClassNames.add(stepsClass.getName());
			}
		}
		return stepsClassNames;
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
					mapResult.put(prefix + "." + name, value);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}
	}
}
