package org.jbehave.osgi.core.embedder;

import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.embedder.EmbedderMonitor;
import org.jbehave.core.embedder.PrintStreamEmbedderMonitor;
import org.jbehave.core.embedder.StoryMapper;
import org.jbehave.core.embedder.StoryRunner;
import org.jbehave.osgi.core.configuration.OsgiConfiguration;

public class OsgiEmbedder extends Embedder {
	
	public OsgiEmbedder() {
	    this(new StoryMapper(), new StoryRunner(), new PrintStreamEmbedderMonitor());
	}

	public OsgiEmbedder(StoryMapper storyMapper, StoryRunner storyRunner,
			EmbedderMonitor embedderMonitor) {
		super(storyMapper, storyRunner, embedderMonitor);
        useConfiguration(new OsgiConfiguration());
	}
}
