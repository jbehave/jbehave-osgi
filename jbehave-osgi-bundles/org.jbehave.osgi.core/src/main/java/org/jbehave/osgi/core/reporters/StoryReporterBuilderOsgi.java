package org.jbehave.osgi.core.reporters;

import org.jbehave.core.i18n.LocalizedKeywords;
import org.jbehave.core.reporters.FreemarkerViewGenerator;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.reporters.FilePrintStreamFactory.FileConfiguration;
import org.jbehave.osgi.core.io.CodeLocationsOsgi;

/**
 * 
 * @author Cristiano Gavião
 */
public class StoryReporterBuilderOsgi extends StoryReporterBuilder {

	public StoryReporterBuilderOsgi() {
		withCodeLocation(CodeLocationsOsgi.codeLocationFromPath("/"));
		withPathResolver(new FileConfiguration().getPathResolver());
		withRelativeDirectory(new FileConfiguration().getRelativeDirectory());
		withViewResources(new FreemarkerViewGenerator().defaultViewProperties());
		withKeywords(new LocalizedKeywords());
	}

}
