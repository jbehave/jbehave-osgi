package org.jbehave.osgi.core.services.impl;

import java.util.List;
import java.util.logging.Logger;

import org.jbehave.core.embedder.Embedder;
import org.jbehave.osgi.core.services.RunnerService;

public class RunnerServicesBasicImpl implements RunnerService {

	private static final Logger m_logger = Logger.getLogger( RunnerServicesBasicImpl.class.getName() );

	private Embedder embedder;
	private Boolean status = false;
	
	@Override
	public boolean isStarted() {
		return status;
	}

	@Override
	public void runAsEmbeddables(List<String> classNames) {
		m_logger.info("starting running the test using Embeddables:");
		
	}

	@Override
	public void runStoriesWithAnnotatedEmbedderRunner(String runnerClass,
			List<String> classNames) {
		m_logger.info("starting running the test using AnnotatedEmbedderRunner:");
		
	}

	@Override
	public String getStatus() {

		return status.toString();
	}

	@Override
	public void startUp() {
		
		m_logger.info("======>>> Starting Jbehave OSGi Runner Service");
		
		status = true;
	}

	protected void setEmbedder(Embedder embedder) {
		this.embedder = embedder;
	}

	protected Embedder getEmbedder() {
		return embedder;
	}

}
