package org.jbehave.osgi.services;

import java.util.List;

/**
 * <p>
 * Jbehave OSGi Embedder Service
 * </p>
 * 
 * @author Cristiano Gavião
 */
public interface EmbedderService {

	boolean isStarted();

	void showStatus();

	void runAsEmbeddables();

	void runStoriesWithAnnotatedEmbedderRunner();
	
	List<String> getIncludeList();
	
	List<String> getExcludeList();

	void runStoriesWithAnnotatedEmbedderRunner(List<String> includes);

}
