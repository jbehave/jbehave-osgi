package org.jbehave.osgi.commands;

import java.util.List;

import org.apache.karaf.shell.console.Completer;
import org.apache.karaf.shell.console.completer.StringsCompleter;
import org.jbehave.osgi.services.EmbedderService;

public class FindAnnotatedEmbeddersCompleter implements Completer {

	private EmbedderService embedderService;

	/**
	 * @param buffer
	 *            it's the beginning string typed by the user
	 * @param cursor
	 *            it's the position of the cursor
	 * @param candidates
	 *            the list of completions proposed to the user
	 */
	@Override
	public int complete(String buffer, int cursor, List<String> candidates) {		
		StringsCompleter delegate = new StringsCompleter();
		delegate.getStrings().addAll(embedderService.findClassNames());
		return delegate.complete(buffer, cursor, candidates);
	}


	public EmbedderService getEmbedderService() {
		return embedderService;
	}

	public void setEmbedderService(EmbedderService embedderService) {
		this.embedderService = embedderService;
	}

}
