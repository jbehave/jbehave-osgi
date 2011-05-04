package org.jbehave.osgi.services.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.osgi.services.EmbedderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Default implementation of the Jbehave OSGi Embedder Service {@link EmbedderService}
 * </p>
 * 
 * @author Cristiano Gavião
 */
public class EmbedderServiceImpl implements EmbedderService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(EmbedderServiceImpl.class);
	private Embedder embedder;
	private Boolean status = false;
	private List<String> excludeList = null;
	private List<String> includeList = null;

	/**
	 * Finds class names, using the {@link #newStoryFinder()}, in the
	 * {@link #searchDirectory()} given specified {@link #includes} and
	 * {@link #excludes}.
	 * 
	 * @return A List of class names found
	 */
	protected List<String> classNames() {
		LOGGER.debug("Searching for class names including " + getIncludeList()
				+ " and excluding " + getExcludeList());
		// TODO List<String> classNames =
		// newStoryFinder().findClassNames(searchDirectory(), getIncludeList(),
		// getExcludeList());
		List<String> classNames = getIncludeList();
		LOGGER.info("Found class names: " + classNames);
		return classNames;
	}

	public Embedder getEmbedder() {
		return embedder;
	}

	@Override
	public List<String> getExcludeList() {
		if (excludeList == null) {
			excludeList = new ArrayList<String>();
		}
		return excludeList;
	}

	@Override
	public List<String> getIncludeList() {
		if (includeList == null) {
			includeList = new ArrayList<String>();
		}
		return includeList;
	}

	@Override
	public boolean isStarted() {
		return status;
	}

	@Override
	public void runAsEmbeddables() {
		LOGGER.info("Running stories using embedder " + embedder);
		embedder.runAsEmbeddables(classNames());
	}

	@Override
	public void runStoriesWithAnnotatedEmbedderRunner() {
		LOGGER.info("Running stories with annotated embedder runner using this classes: " + classNames());
		embedder.runStoriesWithAnnotatedEmbedderRunner(classNames());
	}

	@Override
	public void runStoriesWithAnnotatedEmbedderRunner(List<String> includes) {

		LOGGER.info("Running stories with annotated embedder runner using this classes: " + includes);
		embedder.runStoriesWithAnnotatedEmbedderRunner(classNames());
		runStoriesWithAnnotatedEmbedderRunner();
	}

	public void setEmbedder(Embedder embedder) {
		this.embedder = embedder;
		LOGGER.debug("Injected Embedder " + embedder);
	}

	public void setExcludes(String excludes) {
		getExcludeList().clear();
		if (excludes != null) {
			String[] excludeItens = excludes.split(",");
			for (int i = 0; i < excludeItens.length; i++) {
				getExcludeList().add(excludeItens[i]);
			}
			LOGGER.debug("Injected Exclude List " + getExcludeList().toString());
		}
	}

	public void setIncludes(String includes) {
		getIncludeList().clear();
		if (includes != null) {
			String[] includeItens = includes.split(",");
			for (int i = 0; i < includeItens.length; i++) {
				getIncludeList().add(includeItens[i]);
			}
			LOGGER.debug("Injected Include List " + getIncludeList().toString());			
		}
	}

	@Override
	public void showStatus() {
		StringBuilder message = new StringBuilder(
				"JBehave OSGi EmbedderService is"
						+ (isStarted() ? " " : " not ") + "started");
		String eol = System.getProperty("line.separator");
		message.append(eol + "Include list(" + getIncludeList().size() + "):"
				+ getIncludeList().toString());
		message.append(eol + "Exclude list(" + getExcludeList().size() + "):"
				+ getExcludeList().toString());
		System.out.println(message);
		LOGGER.info(message.toString());
		// LOGGER.info("Include list: " + getIncludeList());
		// LOGGER.info("Exclude list: " + getExcludeList());
	}

	public void start() {

		LOGGER.info("Starting JBehave OSGi Embedder Service");
		status = true;
	}

	public void stop() {
		LOGGER.info("Stopping JBehave OSGi Embedder Service");
		status = false;
		embedder = null;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
