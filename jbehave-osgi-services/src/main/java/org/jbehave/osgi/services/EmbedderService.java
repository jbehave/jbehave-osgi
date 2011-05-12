package org.jbehave.osgi.services;

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

    List<String> findClassNames();
    
	void runStoriesWithAnnotatedEmbedderRunner();
	
	void runStoriesWithAnnotatedEmbedderRunner(List<String> classNames);

}
