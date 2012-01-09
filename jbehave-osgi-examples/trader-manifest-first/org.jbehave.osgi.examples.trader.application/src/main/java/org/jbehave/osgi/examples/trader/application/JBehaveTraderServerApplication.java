package org.jbehave.osgi.examples.trader.application;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;


public class JBehaveTraderServerApplication implements IApplication {


	@Override
	public Object start(final IApplicationContext context) throws Exception {


		System.out.println("Starting JBehave Equinox Server Example");

		return IApplication.EXIT_OK;
	}

	@Override
	public void stop() {
		System.out.println("Finalizing JBehave Equinox Server Example");
	}

}
