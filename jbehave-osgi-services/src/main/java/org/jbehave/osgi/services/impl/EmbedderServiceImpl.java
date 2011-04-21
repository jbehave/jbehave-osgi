package org.jbehave.osgi.services.impl;

import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.osgi.services.EmbedderService;

public class EmbedderServiceImpl implements EmbedderService {

	private static final Logger logger = Logger.getLogger( EmbedderServiceImpl.class.getName() );

	private Embedder embedder;
	private Boolean status = false;
	
    @Override
    public void startUp() {
        logger.info("Starting JBehave OSGi Embedder Service");        
        status = true;
    }

    @Override
    public String getStatus() {
        return status.toString();
    }

	@Override
	public boolean isStarted() {
		return status;
	}

	@Override
	public void runAsEmbeddables(List<String> classNames) {
		logger.info("Running Embeddables "+classNames);
		embedder.runAsEmbeddables(classNames);		
	}

	@Override
	public void runStoriesWithAnnotatedEmbedderRunner(String runnerClass,
			List<String> classNames) {
		logger.info("Running stories with AnnotatedEmbedderRunner "+runnerClass+": "+classNames);
		embedder.runStoriesWithAnnotatedEmbedderRunner(runnerClass, classNames);
	}

	public void setEmbedder(Embedder embedder) {
		this.embedder = embedder;
		logger.info("Injected Embedder "+embedder);
	}

	public Embedder getEmbedder() {
		return embedder;
	}

	@Override
	public String toString() {
	    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
