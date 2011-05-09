package org.jbehave.osgi.commands;

import java.util.List;

import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.jbehave.osgi.services.EmbedderService;

/**
 * <p>
 * Command for {@link EmbedderService#runStoriesWithAnnotatedEmbedderRunner()}
 * </p>
 * 
 * @author Cristiano Gavião
 */
@Command(scope = "jbehave", name = "runStoriesWithAnnotatedEmbedder", description = "Run Stories via Annotated Embedder on Karaf")
public class RunAnnotaddedEmbedderCommand extends EmbedderCommand {

	@Argument(index = 0, name = "includes", description = "One or more annotated embedders to be run", required = false, multiValued = true)
	private List<String> includes = null;

	public Object doExecute() throws Exception {
		EmbedderService embedderService = getEmbedderService();
		if (embedderService.isStarted()) {
			if (includes != null && !includes.isEmpty()) {
				embedderService.runStoriesWithAnnotatedEmbedderRunner(includes);
			} else {
				embedderService.runStoriesWithAnnotatedEmbedderRunner();
			}
		} else {
			System.out.println("JBehave OSGi Embedder Service isn't started.");
		}
		return null;
	}

}
