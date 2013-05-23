package org.jbehave.osgi.interactive.components;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;

/**
 * 
 * @author Cristiano Gavião
 *
 */
class DefaultExtensionTracker extends BundleTracker<Object> {

	private final ExtendableComponent extendableComponent;
	private final List<String> contributions = new CopyOnWriteArrayList<String>();

	public DefaultExtensionTracker(ExtendableComponent extendableComponent,
			BundleContext context, int stateMask,
			BundleTrackerCustomizer<Object> customizer) {
		super(context, stateMask, customizer);
		this.extendableComponent = extendableComponent;
	}

	@Override
	public final Object addingBundle(Bundle bundle, BundleEvent event) {

		String[] headerValues = getExtendableComponent().isValidContribution(
				bundle);
		if (headerValues != null) {

			String msg = "Adding "
					+ getExtendableComponent().getExtenderManifestHeader()
					+ " Contribution:" + bundle.getSymbolicName();
			if (event != null)
				msg.concat("/n Event:" + event.toString());

			getExtendableComponent().logDebug(msg);

			contributions.add(bundle.getSymbolicName());

			getExtendableComponent().onExtensionAddition(bundle, headerValues);
			return bundle;
		}
		return null;
	}

	@Override
	public final void modifiedBundle(Bundle bundle, BundleEvent event,
			Object fragmentObject) {
	}

	@Override
	public final void removedBundle(Bundle bundle, BundleEvent event,
			Object object) {
		if (contributions.contains(bundle.getSymbolicName())) {

			String msg = "Removing "
					+ getExtendableComponent().getExtenderManifestHeader()
					+ "  Contribution:" + bundle.getSymbolicName();
			if (event != null)
				msg.concat("/n Event:" + event.toString());
			getExtendableComponent().logDebug(msg);

			getExtendableComponent().onExtensionRemoval(bundle, object);
		}
	}

	public ExtendableComponent getExtendableComponent() {
		return extendableComponent;
	}
}