package org.jbehave.osgi.core.reporters;

import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.osgi.core.io.OsgiCodeLocations;

public class OsgiStoryReporterBuilder extends StoryReporterBuilder {

	public OsgiStoryReporterBuilder() {
		withCodeLocation(OsgiCodeLocations.codeLocationFromPath("/"));
//		withPathResolver(null);
//		withRelativeDirectory(null);
	}

}
