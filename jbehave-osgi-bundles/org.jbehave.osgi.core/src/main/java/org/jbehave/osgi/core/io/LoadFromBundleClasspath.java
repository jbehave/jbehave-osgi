package org.jbehave.osgi.core.io;

import java.io.InputStream;

import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryResourceNotFound;

/**
 * 
 * @author Cristiano Gavião
 *
 */
public class LoadFromBundleClasspath extends LoadFromClasspath {

	public LoadFromBundleClasspath(Class<?> loadFromBundleClass) {
		super(loadFromBundleClass);
	}

	public LoadFromBundleClasspath(ClassLoader classLoader) {
		super(classLoader);
	}
	    
    protected InputStream resourceAsStream(String resourcePath) {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream(resourcePath);
        if (stream == null) {
            throw new StoryResourceNotFound(resourcePath, classLoader);
        }
        return stream;
    }

}
