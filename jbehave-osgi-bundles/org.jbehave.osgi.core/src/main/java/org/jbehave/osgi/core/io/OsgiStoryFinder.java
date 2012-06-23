package org.jbehave.osgi.core.io;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.jbehave.core.io.StoryFinder;
import org.osgi.framework.wiring.BundleWiring;

import static java.util.Arrays.asList;
import static org.osgi.framework.FrameworkUtil.getBundle;

public class OsgiStoryFinder extends StoryFinder {

    public OsgiStoryFinder() {
        super();
    }

    public OsgiStoryFinder(String classNameExtension) {
        super(classNameExtension);
    }

    public OsgiStoryFinder(Comparator<? super String> sortingComparator) {
        super(sortingComparator);
    }

    /**
     * Finds paths from a base package, allowing for single include/exclude
     * pattern. Paths found are sorted by {@link StoryFinder#sort(List<String>)}
     * .
     * 
     * @param searchInURL the base URL to search in
     * @param include the include pattern, or <code>""</code> if none
     * @param exclude the exclude pattern, or <code>""</code> if none
     * @return A List of paths found
     */
    public List<String> findPaths(String searchPackage, String include, String exclude) {
        return sort(scan(searchPackage, asList(include), asList(exclude)));
    }

    @Override
    protected List<String> scan(String basedir, List<String> includes, List<String> excludes) {

        List<String> scanned = new ArrayList<String>();

        BundleWiring wiring = getBundle(getClass()).getBundleContext().getBundle().adapt(BundleWiring.class);

        if (includes != null) {
            for (String filePattern : includes) {
                Collection<String> files = wiring.listResources(basedir, filePattern,
                        BundleWiring.LISTRESOURCES_RECURSE);
                if (files != null && !files.isEmpty()) {
                    scanned.addAll(files);
                }
            }
        }

        if (excludes != null) {
            for (String filePattern : excludes) {
                Collection<String> files = wiring.listResources(basedir, filePattern,
                        BundleWiring.LISTRESOURCES_RECURSE);
                if (files != null && !files.isEmpty()) {
                    scanned.removeAll(files);
                }
            }
        }

        return scanned;
    }

}
