package org.jbehave.osgi.paxexam;

import static org.ops4j.pax.exam.Constants.START_LEVEL_SYSTEM_BUNDLES;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;

import org.ops4j.pax.exam.options.AbstractDelegateProvisionOption;
import org.ops4j.pax.exam.options.MavenArtifactProvisionOption;

public class OrbitJUnitBundlesOption
	extends AbstractDelegateProvisionOption<OrbitJUnitBundlesOption> {

	    /**
	     * Constructor.
	     */
	    public OrbitJUnitBundlesOption() {
	        super(mavenBundle("org.lunifera.osgi", "org.junit")
					.versionAsInProject());
	        noUpdate();
	        startLevel(START_LEVEL_SYSTEM_BUNDLES);
	    }

	    /**
	     * Sets the junit version.
	     * 
	     * @param version
	     *            junit version.
	     * 
	     * @return itself, for fluent api usage
	     */
	    public OrbitJUnitBundlesOption version(final String version) {
	        ((MavenArtifactProvisionOption) getDelegate()).version(version);
	        return this;
	    }

	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    public String toString() {
	        final StringBuilder sb = new StringBuilder();
	        sb.append("OrbitJUnitBundlesOption");
	        sb.append("{url=").append(getURL());
	        sb.append('}');
	        return sb.toString();
	    }

	    protected OrbitJUnitBundlesOption itself() {
	        return this;
	    }

}
