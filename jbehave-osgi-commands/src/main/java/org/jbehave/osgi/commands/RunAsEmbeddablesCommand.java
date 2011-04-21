package org.jbehave.osgi.commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.felix.gogo.commands.Command;
import org.jbehave.osgi.services.EmbedderService;

/**
 * <p>
 * Command for {@link EmbedderService#runAsEmbeddables(List<String>)}
 * </p>
 * 
 * @author Cristiano Gavião
 */
@Command(scope = "jbehave", name = "runAsEmbeddables", description = "Run Embeddables via OSGi Command")
public class RunAsEmbeddablesCommand extends EmbedderCommand {

    public Object doExecute() throws Exception {
        EmbedderService embedderService = getEmbedderService();
        if (embedderService.isStarted()) {
            System.out.println("Using EmbedderService "+embedderService);
            List<String> classNames = new ArrayList<String>(); // how do I get this input? ResourcesService?            
            embedderService.runAsEmbeddables(classNames);
        } else {
            System.out.println("JBehave OSGi Embedder Service isn't started.");
        }
        return null;
    }

}
