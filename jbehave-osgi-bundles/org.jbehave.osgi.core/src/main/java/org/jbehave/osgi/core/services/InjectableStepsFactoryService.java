package org.jbehave.osgi.core.services;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.steps.InjectableStepsFactory;

public interface InjectableStepsFactoryService extends InjectableStepsFactory {

	String[] getStepClassesNames();

	String getStepFactoryBundleName();

	String getStepFactoryBundleVersion();
	
	String getStepFactoryId();
	
	void setConfiguration(Configuration configuration);
}
