package org.jbehave.osgi.commands;

import org.apache.felix.gogo.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.jbehave.osgi.core.services.RunnerService;

/**
 * <p>
 * Command to get Status of Jbehave OSGi Runner.
 * </p>
 * 
 * @author Cristiano Gavião
 */
@Command(scope = "jbehave", name = "status", description = "Jbehave OSGi Runner status")
public class RunnerStatusCommand extends OsgiCommandSupport {

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

		System.out.println("Jbehave OSGi Runner is " +  runnerService.getStatus());

		return null;
	}

}
