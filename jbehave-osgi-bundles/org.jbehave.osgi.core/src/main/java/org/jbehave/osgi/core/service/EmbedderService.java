package org.jbehave.osgi.core.service;

import java.util.List;

/**
 * <p>
 * OSGi Embedder Service
 * </p>
 * 
 * @author Cristiano Gavião
 */
public interface EmbedderService {

	boolean isStarted();

	void showStatus();
    	
	void runStoriesWithAnnotatedEmbedderRunner(List<String> classNames);

}
