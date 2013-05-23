package org.jbehave.osgi.interactive.config;

import java.util.List;

/**
 * 
 * @author Cristiano Gavião
 *
 */
public class ConfigurationModel {

	private String configurationClass;

	private String keywords;

	private String storyControls;

	private String stepCollector;

	private String storyParser;

	private String storyLoader;

	private String storyPathResolver;

	private String stepdocReporter;

	private String failureStrategy;

	private String pendingStepStrategy;

	private String stepPatternParser;

	private String stepFinder;

	private String stepMonitor;

	private List<String> parameterConverters;

	private boolean inheritParameterConverters;

	private String parameterControls;

	private String paranamer;

	private String viewGenerator;

	private String pathCalculator;

	private String storyReporterBuilder;

	public ConfigurationModel() {
	}

	public String getConfigurationClass() {
		return configurationClass;
	}

	public void setConfigurationClass(String using) {
		this.configurationClass = using;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getStoryControls() {
		return storyControls;
	}

	public void setStoryControls(String storyControls) {
		this.storyControls = storyControls;
	}

	public String getStepCollector() {
		return stepCollector;
	}

	public void setStepCollector(String stepCollector) {
		this.stepCollector = stepCollector;
	}

	public String getStoryParser() {
		return storyParser;
	}

	public void setStoryParser(String storyParser) {
		this.storyParser = storyParser;
	}

	public String getStoryLoader() {
		return storyLoader;
	}

	public void setStoryLoader(String storyLoader) {
		this.storyLoader = storyLoader;
	}

	public String getStoryPathResolver() {
		return storyPathResolver;
	}

	public void setStoryPathResolver(String storyPathResolver) {
		this.storyPathResolver = storyPathResolver;
	}

	public String getStepdocReporter() {
		return stepdocReporter;
	}

	public void setStepdocReporter(String stepdocReporter) {
		this.stepdocReporter = stepdocReporter;
	}

	public String getFailureStrategy() {
		return failureStrategy;
	}

	public void setFailureStrategy(String failureStrategy) {
		this.failureStrategy = failureStrategy;
	}

	public String getPendingStepStrategy() {
		return pendingStepStrategy;
	}

	public void setPendingStepStrategy(String pendingStepStrategy) {
		this.pendingStepStrategy = pendingStepStrategy;
	}

	public String getStepPatternParser() {
		return stepPatternParser;
	}

	public void setStepPatternParser(String stepPatternParser) {
		this.stepPatternParser = stepPatternParser;
	}

	public String getStepFinder() {
		return stepFinder;
	}

	public void setStepFinder(String stepFinder) {
		this.stepFinder = stepFinder;
	}

	public String getStepMonitor() {
		return stepMonitor;
	}

	public void setStepMonitor(String stepMonitor) {
		this.stepMonitor = stepMonitor;
	}

	public List<String> getParameterConverters() {
		return parameterConverters;
	}

	public void setParameterConverters(List<String> parameterConverters) {
		this.parameterConverters = parameterConverters;
	}

	public boolean isInheritParameterConverters() {
		return inheritParameterConverters;
	}

	public void setInheritParameterConverters(boolean inheritParameterConverters) {
		this.inheritParameterConverters = inheritParameterConverters;
	}

	public String getParameterControls() {
		return parameterControls;
	}

	public void setParameterControls(String parameterControls) {
		this.parameterControls = parameterControls;
	}

	public String getParanamer() {
		return paranamer;
	}

	public void setParanamer(String paranamer) {
		this.paranamer = paranamer;
	}

	public String getViewGenerator() {
		return viewGenerator;
	}

	public void setViewGenerator(String viewGenerator) {
		this.viewGenerator = viewGenerator;
	}

	public String getPathCalculator() {
		return pathCalculator;
	}

	public void setPathCalculator(String pathCalculator) {
		this.pathCalculator = pathCalculator;
	}

	public String getStoryReporterBuilder() {
		return storyReporterBuilder;
	}

	public void setStoryReporterBuilder(String storyReporterBuilder) {
		this.storyReporterBuilder = storyReporterBuilder;
	}

}
