package org.jbehave.osgi.core.junit;

import org.jbehave.core.configuration.AnnotationBuilder;
import org.jbehave.core.junit.AnnotatedEmbedderRunner;
import org.jbehave.osgi.core.configuration.AnnotationBuilderOsgi;
import org.junit.runners.model.InitializationError;

public class AnnotatedEmbedderRunnerOsgi extends AnnotatedEmbedderRunner {

	public AnnotatedEmbedderRunnerOsgi(Class<?> testClass)
			throws InitializationError {
		super(testClass);
	}

	@Override
	public AnnotationBuilder annotationBuilder() {
		return new AnnotationBuilderOsgi(testClass());
	}
}
