package org.jbehave.osgi.core.services;

import java.util.Dictionary;
import java.util.List;

/**
 * JBehave OSGi Story Runner Service Interface
 * 
 * @author Cristiano Gavião
 */
public interface StoryRunnerService {

	List<String> boundStepFactories();

	Dictionary<String, ?> getConfigurationProperties();

	String getStoryBundleName();

	String getStoryBundleVersion();

	String getStoryClassName();

	String[] getStoryType();

	List<String> listStoriesIn(String searchIn, String[] includes,
			String[] excludes);

	String reportOutputLocation();

	void run() throws Throwable;

	void run(String reportOutputDir) throws Throwable;

	List<String> storyPaths();

	void useReportOutputLocation(String reportOutputLocation);
}
