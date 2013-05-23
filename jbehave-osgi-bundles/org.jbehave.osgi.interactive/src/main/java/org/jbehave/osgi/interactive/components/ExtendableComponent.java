package org.jbehave.osgi.interactive.components;

import org.osgi.framework.Bundle;


/**
 * 
 * @author Cristiano Gavião
 *
 */
public interface ExtendableComponent {

	public String getExtenderManifestHeader();
	
	public void logDebug(String msg);

	public void logError(String msg, Throwable e);

	public void logInfo(String msg);

	public void logWarn(String msg);

	public void onExtensionAddition(Bundle bundle, String[] headerValues);

	public void onExtensionRemoval(Bundle bundle, Object object);

	public String[] isValidContribution(Bundle bundle);
}
