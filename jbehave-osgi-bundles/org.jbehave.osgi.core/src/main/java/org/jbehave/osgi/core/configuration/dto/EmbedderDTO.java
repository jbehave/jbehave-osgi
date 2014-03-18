package org.jbehave.osgi.core.configuration.dto;

import java.util.List;

/**
 * 
 * @author Cristiano Gavião
 *
 */
public class EmbedderDTO {

	private String embedderClass;
	private boolean batch;
	private boolean skip;
	private boolean generateViewAfterStories;
	private boolean ignoreFailureInStories;
	private boolean ignoreFailureInView;
	private boolean verboseFailures;
	private boolean verboseFiltering;
	private long storyTimeoutInSecs;
	private int threads;
	private List<String> metaFilters;
	private String systemProperties;

	public EmbedderDTO() {
	}

	public String getEmbedderClass() {
		return embedderClass;
	}

	public List<String> getMetaFilters() {
		return metaFilters;
	}

	public long getStoryTimeoutInSecs() {
		return storyTimeoutInSecs;
	}

	public String getSystemProperties() {
		return systemProperties;
	}

	public int getThreads() {
		return threads;
	}

	public boolean isBatch() {
		return batch;
	}

	public boolean isGenerateViewAfterStories() {
		return generateViewAfterStories;
	}

	public boolean isIgnoreFailureInStories() {
		return ignoreFailureInStories;
	}

	public boolean isIgnoreFailureInView() {
		return ignoreFailureInView;
	}

	public boolean isSkip() {
		return skip;
	}

	public boolean isVerboseFailures() {
		return verboseFailures;
	}

	public boolean isVerboseFiltering() {
		return verboseFiltering;
	}

	public void setBatch(boolean batch) {
		this.batch = batch;
	}

	public void setEmbedderClass(String embedder) {
		this.embedderClass = embedder;
	}

	public void setGenerateViewAfterStories(boolean generateViewAfterStories) {
		this.generateViewAfterStories = generateViewAfterStories;
	}
	public void setIgnoreFailureInStories(boolean ignoreFailureInStories) {
		this.ignoreFailureInStories = ignoreFailureInStories;
	}
	public void setIgnoreFailureInView(boolean ignoreFailureInView) {
		this.ignoreFailureInView = ignoreFailureInView;
	}
    public void setMetaFilters(List<String> metaFilters) {
		this.metaFilters = metaFilters;
	}
    public void setSkip(boolean skip) {
		this.skip = skip;
	}
    public void setStoryTimeoutInSecs(long storyTimeoutInSecs) {
		this.storyTimeoutInSecs = storyTimeoutInSecs;
	}
    public void setSystemProperties(String systemProperties) {
		this.systemProperties = systemProperties;
	}
	public void setThreads(int threads) {
		this.threads = threads;
	}
	public void setVerboseFailures(boolean verboseFailures) {
		this.verboseFailures = verboseFailures;
	}
	public void setVerboseFiltering(boolean verboseFiltering) {
		this.verboseFiltering = verboseFiltering;
	}
}
