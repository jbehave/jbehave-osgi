package org.jbehave.osgi.embedder;

import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.embedder.EmbedderMonitor;
import org.jbehave.core.embedder.StoryMapper;
import org.jbehave.core.embedder.StoryRunner;
import org.jbehave.osgi.configuration.OsgiDefaultConfiguration;

public class OsgiEmbedder extends Embedder {
	
	public OsgiEmbedder() {
		super();
        useConfiguration(new OsgiDefaultConfiguration());
	}

	public OsgiEmbedder(StoryMapper storyMapper, StoryRunner storyRunner,
			EmbedderMonitor embedderMonitor) {
		super(storyMapper, storyRunner, embedderMonitor);
	}
}
