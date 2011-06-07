package org.jbehave.osgi.io;

import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.ResourceLoader;
import org.jbehave.core.io.StoryLoader;

public class OsgiLoadFromClasspath extends LoadFromClasspath implements
		ResourceLoader, StoryLoader {

	public OsgiLoadFromClasspath()
	{
		super();
	}
	public OsgiLoadFromClasspath(ClassLoader classLoader) {
		super(classLoader);
	}
}
