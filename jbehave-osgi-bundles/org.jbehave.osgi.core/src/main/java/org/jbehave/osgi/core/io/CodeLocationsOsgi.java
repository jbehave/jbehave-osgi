package org.jbehave.osgi.core.io;

import java.net.URL;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

/**
 * 
 * @author Cristiano Gavião
 *
 */
public class CodeLocationsOsgi {

	/**
	 * Creates a code location URL from a file path
	 * 
	 * @param path
	 *            the File path
	 * @return A URL created from File
	 * @throws InvalidCodeLocation
	 *             if URL creation fails
	 */
	public static URL codeLocationFromPath(String path) {
		try {
			
			BundleContext bundleContext = FrameworkUtil.getBundle(CodeLocationsOsgi.class).getBundleContext();
			return bundleContext.getDataFile(path).toURI().toURL();
		} catch (Exception e) {
			throw new InvalidCodeLocation(path, e);
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
			throw new InvalidCodeLocation(url, e);
		}
	}

	@SuppressWarnings("serial")
	public static class InvalidCodeLocation extends RuntimeException {

		public InvalidCodeLocation(String path, Throwable cause) {
			super(path, cause);
		}

	}
}
