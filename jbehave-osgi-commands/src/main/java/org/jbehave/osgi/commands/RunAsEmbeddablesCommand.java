package org.jbehave.osgi.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.felix.gogo.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.jbehave.osgi.services.EmbedderService;

/**
 * <p>
 * Command for {@link EmbedderService#runAsEmbeddables(List<String>)}
 * </p>
 * 
 * @author Cristiano Gavião
 */
@Command(scope = "jbehave", name = "runAsEmbeddables", description = "Run Embeddables via OSGi Command")
public class RunAsEmbeddablesCommand extends OsgiCommandSupport {

    private static final Logger logger = Logger.getLogger(RunAsEmbeddablesCommand.class.getName());

    private EmbedderService embedderService;

    public EmbedderService getEmbedderService() {
        return embedderService;
    }

    public void setEmbedderService(EmbedderService embedderService) {
        this.embedderService = embedderService;
    }

    public Object doExecute() throws Exception {
        if (embedderService.isStarted()) {
            System.out.println("JBehave OSGi Embedder Servicer is started.");
            List<String> classNames = new ArrayList<String>(); // how do I get this input? ResourcesService?
            embedderService.runAsEmbeddables(classNames);
        } else {
            logger.info("JBehave OSGi Embedder Service isn't started.");
        }
        return null;
    }

}
