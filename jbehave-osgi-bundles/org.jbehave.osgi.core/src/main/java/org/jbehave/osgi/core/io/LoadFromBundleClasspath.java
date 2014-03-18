package org.jbehave.osgi.core.io;

import java.io.InputStream;

import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryResourceNotFound;
import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;

/**
 * An specialized {@link LoadFromClasspath} that uses the owner bundle's
 * classloader to load the artifacts.
 * 
 * @author Cristiano Gavião
 *
 */
public class LoadFromBundleClasspath extends LoadFromClasspath {

	public LoadFromBundleClasspath(Bundle ownerBundle) {

		this(ownerBundle.adapt(BundleWiring.class).getClassLoader());
	}

	private LoadFromBundleClasspath(ClassLoader classLoader) {
		super(classLoader);
	}

	protected InputStream resourceAsStream(String resourcePath) {

		InputStream stream = classLoader.getResourceAsStream(resourcePath);

		if (stream == null) {
			throw new StoryResourceNotFound(resourcePath, classLoader);
		}
		return stream;
	}

}
