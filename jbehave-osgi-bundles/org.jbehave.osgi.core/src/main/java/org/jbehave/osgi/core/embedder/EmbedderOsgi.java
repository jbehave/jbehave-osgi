package org.jbehave.osgi.core.embedder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.embedder.EmbedderClassLoader;
import org.jbehave.core.embedder.PerformableTree;
import org.jbehave.core.embedder.PrintStreamEmbedderMonitor;
import org.jbehave.core.embedder.StoryMapper;
import org.jbehave.core.steps.AbstractStepsFactory.StepsInstanceNotFound;
import org.jbehave.core.steps.CandidateSteps;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.osgi.core.configuration.ConfigurationOsgi;
import org.jbehave.osgi.core.services.InjectableStepsFactoryService;
import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;

/**
 * An specific Embedder aim to be used in an OSGi environment.
 * <p>
 * This class is needed due to class loading issues that occurs when using the
 * original one.<br>
 * The embedder object is set with another specific OSGi objects.
 * 
 * @see {@link ConfigurationOsgi}, {@link EmbedderClassLoader},
 *      {@link ThrowingRunningStoriesFailed}
 * 
 * @author Cristiano Gavião
 */
public class EmbedderOsgi extends Embedder {

	private class OsgiCompositeStepsFactory implements InjectableStepsFactory {

		final Configuration configuration;

		public OsgiCompositeStepsFactory(Configuration configuration) {
			this.configuration = configuration;
		}

		public List<CandidateSteps> createCandidateSteps() {
			List<CandidateSteps> steps = new ArrayList<CandidateSteps>();
			synchronized (injectableStepsFactories) {
				for (InjectableStepsFactoryService factory : injectableStepsFactories) {
					factory.setConfiguration(configuration);
					steps.addAll(factory.createCandidateSteps());
				}
			}
			return steps;
		}

		public Object createInstanceOfType(Class<?> type) {
			Object instance = null;
			synchronized (injectableStepsFactories) {
				for (InjectableStepsFactory factory : injectableStepsFactories) {
					try {
						instance = factory.createInstanceOfType(type);
					} catch (RuntimeException e) {
						// creation failed on given factory, carry on
					}
				}
				if (instance == null) {
					throw new StepsInstanceNotFound(type, this);
				}
			}
			return instance;
		}
	}

	private final Bundle ownerBundle;

	private final List<InjectableStepsFactoryService> injectableStepsFactories = new CopyOnWriteArrayList<InjectableStepsFactoryService>();

	/**
	 * The default constructor.
	 * 
	 * @param ownerBundle
	 *            the bundle that contains the artifacts to be executed by this
	 *            embedder.
	 */
	public EmbedderOsgi(Bundle ownerBundle) {
		super(new StoryMapper(), new PerformableTree(),
				new PrintStreamEmbedderMonitor());

		this.ownerBundle = ownerBundle;
		// useEmbedderFailureStrategy(new ThrowingRunningStoriesFailed());
	}

	public void addInjectableStepsFactoryService(
			InjectableStepsFactoryService injectableStepsFactoryService) {
		injectableStepsFactories.add(injectableStepsFactoryService);
	}

	public EmbedderClassLoader classLoader() {
		if (classLoader == null) {
			ClassLoader cl = ownerBundle.adapt(BundleWiring.class)
					.getClassLoader();
			this.classLoader = new EmbedderClassLoader(cl);
		}
		return classLoader;
	}

	public Configuration configuration() {
		if (configuration == null) {
			configuration = new ConfigurationOsgi(ownerBundle);
			configureThreads(configuration, embedderControls().threads());
		}
		return configuration;
	}

	public Bundle getOwnerBundle() {
		return ownerBundle;
	}

	public List<InjectableStepsFactoryService> injectableStepsFactories() {
		return injectableStepsFactories;
	}

	public void removeInjectableStepsFactoryService(
			InjectableStepsFactoryService injectableStepsFactoryService) {
		injectableStepsFactories.remove(injectableStepsFactoryService);
	}

	public InjectableStepsFactory stepsFactory() {
		if (stepsFactory == null) {
			stepsFactory = new OsgiCompositeStepsFactory(configuration());
		}
		return stepsFactory;
	}
}
