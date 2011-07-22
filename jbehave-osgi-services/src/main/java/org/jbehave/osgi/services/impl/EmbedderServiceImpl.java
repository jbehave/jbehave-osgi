package org.jbehave.osgi.services.impl;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.osgi.services.EmbedderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Default implementation of the OSGi Embedder Service {@link EmbedderService}
 * </p>
 * 
 * @author Cristiano Gavião
 */
public class EmbedderServiceImpl implements EmbedderService {


	private static final Logger LOGGER = LoggerFactory
			.getLogger(EmbedderServiceImpl.class);

	private Embedder embedder;
	private Boolean status = false;

	public Embedder getEmbedder() {
		if (embedder == null)
		{
			embedder = new Embedder();
		}
		return embedder;
	}

	@Override
	public boolean isStarted() {
		return status;
	}

	@Override
	public void runStoriesWithAnnotatedEmbedderRunner(List<String> classNames) {
		LOGGER.info("Running stories with annotated embedder runner using classes: '"
				+ classNames + "'");
		embedder.runStoriesWithAnnotatedEmbedderRunner(classNames);
	}

	public void setEmbedder(Embedder embedder) {
		this.embedder = embedder;
		LOGGER.debug("Injected Embedder " + embedder);
	}

	@Override
	public void showStatus() {
		System.out.println("OSGi Embedder Service is"
				+ (isStarted() ? " " : " not ") + "started.");
		LOGGER.info("OSGi Embedder Service is" + (isStarted() ? " " : " not ")
				+ "started.");
	}

	public void start() {
		LOGGER.info("Starting OSGi Embedder Service");
		status = true;
	}

	public void stop() {
		LOGGER.info("Stopping OSGi Embedder Service");
		status = false;
		embedder = null;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
