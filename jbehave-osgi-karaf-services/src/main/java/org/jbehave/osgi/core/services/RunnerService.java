package org.jbehave.osgi.core.services;

import java.util.List;

public interface RunnerService {

	boolean isStarted();

	public void runAsEmbeddables(List<String> classNames);

	public void runStoriesWithAnnotatedEmbedderRunner(String runnerClass,
			List<String> classNames);

	String getStatus();
	
	public void startUp();
}
