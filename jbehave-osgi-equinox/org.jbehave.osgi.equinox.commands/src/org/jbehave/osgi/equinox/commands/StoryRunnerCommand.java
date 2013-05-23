package org.jbehave.osgi.equinox.commands;

import java.util.Dictionary;
import java.util.Enumeration;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.jbehave.osgi.equinox.commands.converters.Util;
import org.jbehave.osgi.interactive.services.StoryRunnerService;

/**
 * 
 * @author Cristiano Gavião
 */
public class StoryRunnerCommand extends AbstractEmbedderCommand {

	@Descriptor("Manage registered JBehave StoryRunner services.")
	public void storyRunner(
			@Descriptor("list all registered services") @Parameter(names = {
					"-l", "--list" }, presentValue = "true", absentValue = "false") boolean list,
			@Descriptor("display StoryRunner services details") @Parameter(names = {
					"-d", "--details" }, presentValue = "true", absentValue = "false") boolean display,
			@Descriptor("run stories") @Parameter(names = { "-r", "--run" }, presentValue = "true", absentValue = "false") boolean run,
			@Descriptor("list associated stories") @Parameter(names = { "-s",
					"--stories" }, presentValue = "true", absentValue = "false") boolean stories,
			@Descriptor("One or more registered StoryRunner services") StoryRunnerService... storyRunnerServices) {

		if (list && !display && !run && !stories) {
			StoryRunnerService[] allrunners = getStoryRunnerServices().values()
					.toArray(
							new StoryRunnerService[getStoryRunnerServices()
									.values().size()]);
			for (int i = 0; i < allrunners.length; i++) {
				StoryRunnerService storyRunnerService = allrunners[i];
				System.out.println("\t" + (i + 1) + ") "
						+ storyRunnerService.getStoryClassName() + " <"
						+ storyRunnerService.getStoryBundleName() + ">");

			}
			return;
		}

		if (storyRunnerServices == null || storyRunnerServices.length == 0) {
			System.err
					.println("You must inform at least one registered StoryRunner service.");
			return;
		}
		if (display && !run && !stories) {
			for (int i = 0; i < storyRunnerServices.length; i++) {
				StoryRunnerService storyRunnerService = storyRunnerServices[i];
				System.out.println();
				System.out.println("StoryRunner: '"
						+ storyRunnerService.getStoryClassName()
						+ "'\n\tparameters: ");
				Dictionary<?, ?> properties = storyRunnerService
						.getConfigurationProperties();
				System.out.println(printDictionary(properties));

			}
			return;
		} else if (run && !display && !stories) {
			for (int i = 0; i < storyRunnerServices.length; i++) {
				StoryRunnerService storyRunnerService = storyRunnerServices[i];
				try {
					storyRunnerService.run();
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return;
		} else if (!run && !display && stories) {
			// for (int i = 0; i < storyRunnerServices.length; i++) {
			// StoryRunnerService storyRunnerService = storyRunnerServices[i];
			// storyRunnerService.();
			// }

		} else if ((run && display) || (run && stories) || (display && stories)
				|| (run && display && stories)) {
			System.err.println("can't use more than on parameter by time");
		}

	}

	private String printDictionary(Dictionary<?, ?> dic) {
		int count = dic.size();
		String[] keys = new String[count];
		Enumeration<?> keysEnum = dic.keys();
		int i = 0;
		while (keysEnum.hasMoreElements()) {
			keys[i++] = (String) keysEnum.nextElement();
		}
		Util.sortByString(keys);

		StringBuilder builder = new StringBuilder();
		for (i = 0; i < count; i++) {
			builder.append(" " + keys[i] + " = " + dic.get(keys[i]));
			builder.append("\r\n");
		}
		builder.append("\r\n");
		return builder.toString();
	}

}
