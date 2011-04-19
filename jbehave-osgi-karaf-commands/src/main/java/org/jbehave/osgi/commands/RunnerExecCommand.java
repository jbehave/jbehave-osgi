package org.jbehave.osgi.commands;

import java.util.logging.Logger;

import org.apache.felix.gogo.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.jbehave.osgi.core.services.RunnerService;

/**
 * <p>
 * Command executeEmbedder of Jbehave OSGi Runner.
 * </p>
 * 
 * @author Cristiano Gavião
 */
@Command(scope = "jbehave", name = "runEmbedder", description = "Jbehave OSGi Runner execution")
public class RunnerExecCommand extends OsgiCommandSupport {

	private static final Logger m_logger = Logger
			.getLogger(RunnerExecCommand.class.getName());

	private RunnerService runnerService;

	public RunnerService getRunnerService() {
		return runnerService;
	}

	public void setRunnerService(RunnerService runnerService) {
		this.runnerService = runnerService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.felix.karaf.shell.console.OsgiCommandSupport#doExecute()
	 */
	public Object doExecute() throws Exception {
		if (getRunnerService().isStarted()) {
			System.out.println("Jbehave OSGi Story runner is executing.");
			// runnerService.runStoriesWithAnnotatedEmbedderRunner(runnerClass,
			// classNames);
		} else {
			m_logger.info("Jbehave OSGi Service isn't started so couldn't execute Story Runner.");
		}
		return null;
	}

}
