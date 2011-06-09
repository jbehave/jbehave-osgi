package org.jbehave.osgi.io;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.jbehave.core.io.StoryFinder;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.wiring.BundleWiring;

public class OsgiStoryFinder extends StoryFinder {

	public OsgiStoryFinder() {
	}

	public OsgiStoryFinder(String classNameExtension) {
		super(classNameExtension);
	}

	public OsgiStoryFinder(Comparator<? super String> sortingComparator) {
		super(sortingComparator);
	}

	/**
	 * Finds paths from a base package, allowing for single include/exclude
	 * pattern. Paths found are normalised by {@link
	 * StoryFinder#normalise(List<String>)}.
	 * 
	 * @param searchInURL
	 *            the base URL to search in
	 * @param include
	 *            the include pattern, or <code>""</code> if none
	 * @param exclude
	 *            the exclude pattern, or <code>""</code> if none
	 * @return A List of paths found
	 */
	public List<String> findPaths(String searchPackage, String include,
			String exclude) {
		return sort(scan(searchPackage, asList(include), asList(exclude)));
	}

	@Override
	protected List<String> scan(String basedir, List<String> includes,
			List<String> excludes) {

		List<String> scannedItens = new ArrayList<String>();
		BundleContext ctx = FrameworkUtil.getBundle(getClass())
				.getBundleContext();
		BundleWiring wiring = ctx.getBundle().adapt(BundleWiring.class);

		if (includes != null) {
			for (String filePattern : includes) {
				Collection<String> foundIncludedFiles = wiring.listResources(
						basedir, filePattern,
						BundleWiring.LISTRESOURCES_RECURSE);
				if (foundIncludedFiles != null && !foundIncludedFiles.isEmpty()) {
					scannedItens.addAll(foundIncludedFiles);
				}
			}
		}

		if (excludes != null) {
			for (String filePattern : includes) {
				Collection<String> foundExcludeFiles = wiring.listResources(
						basedir, filePattern,
						BundleWiring.LISTRESOURCES_RECURSE);
				if (foundExcludeFiles != null && !foundExcludeFiles.isEmpty()) {
					scannedItens.removeAll(foundExcludeFiles);
				}
			}
		}

		return scannedItens;
	}

}
