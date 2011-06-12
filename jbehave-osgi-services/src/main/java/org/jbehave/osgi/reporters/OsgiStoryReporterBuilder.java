package org.jbehave.osgi.reporters;

import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.osgi.io.OsgiCodeLocations;

public class OsgiStoryReporterBuilder extends StoryReporterBuilder {

	public OsgiStoryReporterBuilder() {
		withCodeLocation(OsgiCodeLocations.codeLocationFromPath("/"));
//		withPathResolver(null);
//		withRelativeDirectory(null);
	}

}
