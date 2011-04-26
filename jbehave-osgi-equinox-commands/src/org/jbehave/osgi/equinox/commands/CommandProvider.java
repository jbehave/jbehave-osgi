package org.jbehave.osgi.equinox.commands;

import java.net.URL;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.osgi.framework.Bundle;

public class CommandProvider implements
		org.eclipse.osgi.framework.console.CommandProvider {

  //  private EmbedderService embedderService;

	
	@Override
	public String getHelp() {
		StringBuffer help = new StringBuffer();
		help.append("--- Jbehave Equinox Commands ---\r\n");
		help.append("\tstatus - Verify the status of the Embedder Service");
		help.append("\trunAsEmbeddables - Starts the Embedder");
		help.append("\r\n\r\n");
		return help.toString();
	}

	public void _status(CommandInterpreter intp) throws Exception {
		intp.println("jb-status");
//		intp.println(TestRunner.run());
	}

	public void _runAsEmbeddables(CommandInterpreter intp) throws Exception {
		intp.println("jb-run");
	}

	public void _whatami(CommandInterpreter ci) throws Exception {
		try {
			long id = Long.parseLong(ci.nextArgument());
			Bundle bundle = Activator.getContext().getBundle(id);
			URL url = bundle.getEntry("plugin.xml");
			if (url != null) {
				System.out.println("\n I'm " + bundle.getSymbolicName()
						+ ") a plug-in");
			} else {
				System.out.println("\n I'm " + bundle.getSymbolicName()
						+ ") not a plug-in");
			}
		} catch (NumberFormatException nfe) {
			System.out.println("\n Error processing command");
		}
	}
}
