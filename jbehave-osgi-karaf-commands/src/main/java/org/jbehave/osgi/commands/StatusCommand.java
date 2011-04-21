package org.jbehave.osgi.commands;

import org.apache.felix.gogo.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.jbehave.osgi.services.EmbedderService;

/**
 * <p>
 * Command to get Status of JBehave OSGi EmbedderService.
 * </p>
 * 
 * @author Cristiano Gavião
 */
@Command(scope = "jbehave", name = "status", description = "JBehave OSGi EmbedderService status")
public class StatusCommand extends OsgiCommandSupport {
    
    private EmbedderService embedderService;

    public EmbedderService getEmbedderService() {
        return embedderService;
    }

    public void setEmbedderService(EmbedderService embedderService) {
        this.embedderService = embedderService;
    }

	public Object doExecute() throws Exception {

		System.out.println("JBehave OSGi EmbedderService is " +  embedderService.getStatus());

		return null;
	}

}
