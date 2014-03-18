package org.jbehave.osgi.core.configuration.dto;

import java.util.List;

/**
 * 
 * @author Cristiano Gavião
 *
 */
public class ConfigurationDTO {

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

	public ConfigurationDTO() {
	}

	public String getConfigurationClass() {
		return configurationClass;
	}

	public String getFailureStrategy() {
		return failureStrategy;
	}

	public String getKeywords() {
		return keywords;
	}

	public String getParameterControls() {
		return parameterControls;
	}

	public List<String> getParameterConverters() {
		return parameterConverters;
	}

	public String getParanamer() {
		return paranamer;
	}

	public String getPathCalculator() {
		return pathCalculator;
	}

	public String getPendingStepStrategy() {
		return pendingStepStrategy;
	}

	public String getStepCollector() {
		return stepCollector;
	}

	public String getStepdocReporter() {
		return stepdocReporter;
	}

	public String getStepFinder() {
		return stepFinder;
	}

	public String getStepMonitor() {
		return stepMonitor;
	}

	public String getStepPatternParser() {
		return stepPatternParser;
	}

	public String getStoryControls() {
		return storyControls;
	}

	public String getStoryLoader() {
		return storyLoader;
	}

	public String getStoryParser() {
		return storyParser;
	}

	public String getStoryPathResolver() {
		return storyPathResolver;
	}

	public String getStoryReporterBuilder() {
		return storyReporterBuilder;
	}

	public String getViewGenerator() {
		return viewGenerator;
	}

	public boolean isInheritParameterConverters() {
		return inheritParameterConverters;
	}

	public void setConfigurationClass(String using) {
		this.configurationClass = using;
	}

	public void setFailureStrategy(String failureStrategy) {
		this.failureStrategy = failureStrategy;
	}

	public void setInheritParameterConverters(boolean inheritParameterConverters) {
		this.inheritParameterConverters = inheritParameterConverters;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public void setParameterControls(String parameterControls) {
		this.parameterControls = parameterControls;
	}

	public void setParameterConverters(List<String> parameterConverters) {
		this.parameterConverters = parameterConverters;
	}

	public void setParanamer(String paranamer) {
		this.paranamer = paranamer;
	}

	public void setPathCalculator(String pathCalculator) {
		this.pathCalculator = pathCalculator;
	}

	public void setPendingStepStrategy(String pendingStepStrategy) {
		this.pendingStepStrategy = pendingStepStrategy;
	}

	public void setStepCollector(String stepCollector) {
		this.stepCollector = stepCollector;
	}

	public void setStepdocReporter(String stepdocReporter) {
		this.stepdocReporter = stepdocReporter;
	}

	public void setStepFinder(String stepFinder) {
		this.stepFinder = stepFinder;
	}

	public void setStepMonitor(String stepMonitor) {
		this.stepMonitor = stepMonitor;
	}

	public void setStepPatternParser(String stepPatternParser) {
		this.stepPatternParser = stepPatternParser;
	}

	public void setStoryControls(String storyControls) {
		this.storyControls = storyControls;
	}

	public void setStoryLoader(String storyLoader) {
		this.storyLoader = storyLoader;
	}

	public void setStoryParser(String storyParser) {
		this.storyParser = storyParser;
	}

	public void setStoryPathResolver(String storyPathResolver) {
		this.storyPathResolver = storyPathResolver;
	}

	public void setStoryReporterBuilder(String storyReporterBuilder) {
		this.storyReporterBuilder = storyReporterBuilder;
	}

	public void setViewGenerator(String viewGenerator) {
		this.viewGenerator = viewGenerator;
	}

}
