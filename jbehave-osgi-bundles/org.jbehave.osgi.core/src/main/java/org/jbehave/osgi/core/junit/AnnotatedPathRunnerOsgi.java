package org.jbehave.osgi.core.junit;

import org.jbehave.core.configuration.AnnotationBuilder;
import org.jbehave.core.junit.AnnotatedPathRunner;
import org.jbehave.osgi.core.configuration.AnnotationBuilderOsgi;
import org.junit.runners.model.InitializationError;

public class AnnotatedPathRunnerOsgi extends AnnotatedPathRunner {

	public AnnotatedPathRunnerOsgi(Class<?> annotatedClass)
			throws InitializationError {
		super(annotatedClass);
	}

	@Override
	public AnnotationBuilder annotationBuilder() {
		return new AnnotationBuilderOsgi(testClass());
	}
}
