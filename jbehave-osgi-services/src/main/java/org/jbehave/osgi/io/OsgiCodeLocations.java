package org.jbehave.osgi.io;

import java.io.File;
import java.net.URL;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

public class OsgiCodeLocations {


	/**
	 * Creates a code location URL from a file path
	 * 
	 * @param filePath
	 *            the file path
	 * @return A URL created from File
	 * @throws InvalidCodeLocation
	 *             if URL creation fails
	 */
	public static URL codeLocationFromPath(String filePath) {
		try {
			
			BundleContext bundleContext = FrameworkUtil.getBundle(OsgiCodeLocations.class).getBundleContext();
			File base0 = bundleContext.getDataFile(filePath);
			return base0.toURI().toURL();
		} catch (Exception e) {
			throw new InvalidCodeLocation(filePath);
		}
	}

	/**
	 * Creates a code location URL from a URL
	 * 
	 * @param url
	 *            the URL external form
	 * @return A URL created from URL
	 * @throws InvalidCodeLocation
	 *             if URL creation fails
	 */
	public static URL codeLocationFromURL(String url) {
		try {
			return new URL(url);
		} catch (Exception e) {
			throw new InvalidCodeLocation(url);
		}
	}

	@SuppressWarnings("serial")
	public static class InvalidCodeLocation extends RuntimeException {

		public InvalidCodeLocation(String path) {
			super(path);
		}

	}
}
