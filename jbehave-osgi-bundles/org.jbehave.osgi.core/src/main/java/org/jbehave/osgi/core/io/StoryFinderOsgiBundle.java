package org.jbehave.osgi.core.io;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.jbehave.core.io.StoryFinder;
import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;

/**
 * 
 * @author Cristiano Gavião
 *
 */
public class StoryFinderOsgiBundle extends StoryFinder {

	private final Bundle ownerBundle;
	
    public StoryFinderOsgiBundle(Bundle ownerBundle) {
        super();
        this.ownerBundle = ownerBundle;
    }

    public StoryFinderOsgiBundle(Comparator<? super String> sortingComparator, Bundle ownerBundle) {
        super(sortingComparator);
        this.ownerBundle = ownerBundle;
    }


    @Override
    protected List<String> scan(String basedir, List<String> includes, List<String> excludes) {

        List<String> scanned = new ArrayList<String>();
        BundleWiring wiring = ownerBundle.adapt(BundleWiring.class);

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
