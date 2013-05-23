package org.jbehave.osgi.interactive.services;

import java.util.Dictionary;
import java.util.List;

import org.osgi.framework.Bundle;

/**
 * JBehave OSGi Story Runner Service Interface
 * 
 * @author Cristiano Gavião
 */
public interface StoryRunnerService {

	String getStoryBundleName();

	String getStoryClassName();

	String getStoryType();

	Dictionary<?,?> getConfigurationProperties();
	
	List<String> storyPaths();
	
	List<String> listStoriesIn(String searchIn, String[] includes, String[] excludes);
	
	void run() throws Throwable;
	
	void setStoryBundle(Bundle bundle);

}
