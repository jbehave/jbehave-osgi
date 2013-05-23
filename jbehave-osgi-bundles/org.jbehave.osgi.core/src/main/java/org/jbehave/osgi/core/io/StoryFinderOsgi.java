package org.jbehave.osgi.core.io;

import static org.osgi.framework.FrameworkUtil.getBundle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.jbehave.core.io.StoryFinder;
import org.osgi.framework.wiring.BundleWiring;

/**
 * 
 * @author Cristiano Gavião
 *
 */
public class StoryFinderOsgi extends StoryFinder {

	private final Class<?> testClass;
	
    public StoryFinderOsgi(Class<?> testClass) {
        super();
        this.testClass = testClass;
    }

    public StoryFinderOsgi(String classNameExtension, Class<?> testClass) {
        super(classNameExtension);
        this.testClass = testClass;
    }

    public StoryFinderOsgi(Comparator<? super String> sortingComparator, Class<?> testClass) {
        super(sortingComparator);
        this.testClass = testClass;
    }

    @Override
    protected List<String> scan(String basedir, List<String> includes, List<String> excludes) {

        List<String> scanned = new ArrayList<String>();
        BundleWiring wiring = getBundle(testClass).adapt(BundleWiring.class);

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
