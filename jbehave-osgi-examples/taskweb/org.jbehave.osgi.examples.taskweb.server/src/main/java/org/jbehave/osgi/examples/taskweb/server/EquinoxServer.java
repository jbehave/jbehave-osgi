package org.jbehave.osgi.examples.taskweb.server;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

public class EquinoxServer implements IApplication {

	@Override
	public Object start(final IApplicationContext context) throws Exception {

		System.out.println("Starting Application...");

		return IApplication.EXIT_OK;
	}

	@Override
	public void stop() {
		System.out.println("Stopping Application...");
	}

}
