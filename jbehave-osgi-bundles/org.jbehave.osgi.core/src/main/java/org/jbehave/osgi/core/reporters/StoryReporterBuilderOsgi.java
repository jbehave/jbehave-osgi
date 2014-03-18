package org.jbehave.osgi.core.reporters;

import java.io.File;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.StoryLocation;
import org.jbehave.core.reporters.FilePrintStreamFactory.FilePathResolver;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.osgi.core.Constants;
import org.jbehave.osgi.core.io.CodeLocationsOsgi;
import org.osgi.framework.Bundle;

/**
 * 
 * @author Cristiano Gavião
 */
public class StoryReporterBuilderOsgi extends StoryReporterBuilder {

	public class AbsolutePathResolver implements FilePathResolver {

		@Override
		public String resolveDirectory(StoryLocation storyLocation,
				String relativeDirectory) {
			File root = new File(CodeLocations.getPathFromURL(storyLocation
					.getCodeLocation()));
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

	private final Bundle ownerBundle;

	public StoryReporterBuilderOsgi(Bundle ownerBundle,
			Configuration configuration) {
		super(configuration);
		this.ownerBundle = ownerBundle;
	}

	@Override
	public URL codeLocation() {
		if (codeLocation == null) {
			String outputLocation = System
					.getProperty(Constants.JBEHAVE_OSGI_REPORT_OUTPUT_DIR_PROPERTY);
			if (outputLocation != null && !outputLocation.isEmpty()) {
				try {
					codeLocation = CodeLocations
							.codeLocationFromPath(outputLocation);
				} catch (Exception e) {
					codeLocation = CodeLocationsOsgi
							.bundleDataFolder(ownerBundle);
				}
			} else {
				codeLocation = CodeLocationsOsgi.bundleDataFolder(ownerBundle);
			}
		}
		return codeLocation;
	}

	@Override
	public FilePathResolver pathResolver() {
		if (pathResolver == null) {
			return new AbsolutePathResolver();
		}
		return pathResolver;
	}
}
