package org.jbehave.osgi.core.io;

import java.io.File;
import java.net.URL;

import org.osgi.framework.FrameworkUtil;

/**
 * 
 * @author Cristiano Gavião
 * 
 */
public class CodeLocationsOsgi {

	/**
	 * Creates a code location URL from a class
	 * 
	 * @param codeLocationClass
	 *            the class
	 * @return A URL created from Class
	 * @throws InvalidCodeLocation
	 *             if URL creation fails
	 */
	public static URL codeLocationFromClass(Class<?> codeLocationClass) {
		File file = null;
		try {
			
			file = FrameworkUtil.getBundle(
					codeLocationClass).getBundleContext().getDataFile("");
			return file.toURI().toURL();
		} catch (Exception e) {
			throw new InvalidCodeLocation(file.getAbsolutePath(), e);
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
