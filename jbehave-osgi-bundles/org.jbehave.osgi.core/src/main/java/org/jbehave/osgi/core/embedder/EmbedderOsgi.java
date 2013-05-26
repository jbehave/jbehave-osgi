package org.jbehave.osgi.core.embedder;

import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.embedder.EmbedderClassLoader;
import org.jbehave.core.embedder.PerformableTree;
import org.jbehave.core.embedder.PrintStreamEmbedderMonitor;
import org.jbehave.core.embedder.StoryMapper;
import org.jbehave.osgi.core.configuration.ConfigurationOsgi;

/**
 * 
 * @author Cristiano Gavião
 * 
 */
public class EmbedderOsgi extends Embedder {

	public EmbedderOsgi(Class<?> loadFromBundleClass) {
		super(new StoryMapper(), new PerformableTree(),
				new PrintStreamEmbedderMonitor(), new ConfigurationOsgi(
						loadFromBundleClass));
		useClassLoader(new EmbedderClassLoader(
				loadFromBundleClass.getClassLoader()));
		useEmbedderFailureStrategy(new ThrowingRunningStoriesFailed());
	}

}
