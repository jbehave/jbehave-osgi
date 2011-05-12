package org.jbehave.osgi.commands;

import java.util.List;

import org.apache.karaf.shell.console.Completer;
import org.apache.karaf.shell.console.completer.StringsCompleter;
import org.jbehave.osgi.services.EmbedderService;

public class ClassNamesCompleter implements Completer {

	private EmbedderService embedderService;

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
