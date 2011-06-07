package org.jbehave.osgi.io;

import java.util.Comparator;

import org.jbehave.core.io.StoryFinder;

public class OsgiStoryFinder extends StoryFinder {

	public OsgiStoryFinder() {
	}

	public OsgiStoryFinder(String classNameExtension) {
		super(classNameExtension);
	}

	public OsgiStoryFinder(Comparator<? super String> sortingComparator) {
		super(sortingComparator);
	}

 
}
