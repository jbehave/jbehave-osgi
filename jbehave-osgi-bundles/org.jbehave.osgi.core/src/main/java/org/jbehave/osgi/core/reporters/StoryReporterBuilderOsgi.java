package org.jbehave.osgi.core.reporters;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.jbehave.core.i18n.LocalizedKeywords;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.StoryLocation;
import org.jbehave.core.reporters.FilePrintStreamFactory.FilePathResolver;
import org.jbehave.core.reporters.FreemarkerViewGenerator;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.osgi.core.io.CodeLocationsOsgi;

/**
 * 
 * @author Cristiano Gavião
 */
public class StoryReporterBuilderOsgi extends StoryReporterBuilder {

	public final static String JBEHAVE_OSGI_CODELOCATION_PROPERTY = "jbehave.osgi.outputdir";

	public StoryReporterBuilderOsgi(Class<?> loadFromBundleClass) {

		String outputLocation = System
				.getProperty(JBEHAVE_OSGI_CODELOCATION_PROPERTY);
		if (outputLocation != null && !outputLocation.isEmpty()) {
			withCodeLocation(CodeLocations.codeLocationFromPath(outputLocation));
		} else {
			withCodeLocation(CodeLocationsOsgi
					.codeLocationFromClass(loadFromBundleClass));
		}
		withPathResolver(new AbsolutePathResolver());
		withRelativeDirectory("jbehave");
		withViewResources(new FreemarkerViewGenerator().defaultViewProperties());
		withKeywords(new LocalizedKeywords());
	}

	public class AbsolutePathResolver implements FilePathResolver {

		@Override
		public String resolveDirectory(StoryLocation storyLocation,
				String relativeDirectory) {
			File root = new File(CodeLocations.getPathFromURL(storyLocation.getCodeLocation()));
			return root.getPath().replace('\\', '/') + "/" + relativeDirectory;
		}

		@Override
		public String resolveName(StoryLocation storyLocation, String extension) {
			String name = storyLocation.getPath().replace('/', '.');
            if (name.startsWith(".")) {
                name = name.substring(1);
            }
            return StringUtils.substringBeforeLast(name, ".") + "." + extension;
		}

	}
}
