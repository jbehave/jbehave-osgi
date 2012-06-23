package org.jbehave.osgi.core.service;

public interface IExtensionControler {

	boolean startService();

	boolean terminateService();
	
	boolean isServiceStarted();
	
	boolean isFakeMode();
}
