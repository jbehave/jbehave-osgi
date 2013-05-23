package org.jbehave.osgi.equinox.commands.converters;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;

import org.apache.felix.service.command.Converter;
import org.jbehave.osgi.equinox.commands.AbstractEmbedderCommand;
import org.jbehave.osgi.interactive.services.StoryRunnerService;

/**
 * 
 * @author Cristiano Gavião
 */
public class StoryRunnerArgumentConverter extends AbstractEmbedderCommand
		implements Converter {

	@Override
	public Object convert(Class<?> desiredType, Object inArg) throws Exception {

		if (desiredType == StoryRunnerService.class) {
			if (inArg instanceof String) {
				StoryRunnerService runnerService = getStoryRunnerServices().get(inArg);
				if (runnerService == null){
					System.err.println("StoryRunner '" + inArg + "' doesn't exists");
				}
				return runnerService;
			}
			if (inArg instanceof Long) {
				StoryRunnerService[] allrunners = getStoryRunnerServices()
						.values().toArray(
								new StoryRunnerService[getStoryRunnerServices()
								                       .values().size()]);
				int id = ((Long) inArg).intValue();
				if (id < 0 || id > allrunners.length){
					throw new IllegalArgumentException("StoryRunner with id '" + id + "' doesn't exists");
				}
				return allrunners[id - 1];
			}
		}
		if (desiredType == Boolean.TYPE) {
			if (inArg == Boolean.TYPE) {
				return inArg;
			} else if (inArg instanceof String) {
				return Boolean.parseBoolean((String) inArg);
			}
		}

		return null;
	}

	public CharSequence format(Object target, int level, Converter escape)
			throws Exception {
		if (target instanceof Dictionary<?, ?>) {
			Dictionary<?, ?> dic = (Dictionary<?, ?>) target;
			return printDictionary(dic);
		}

		if (target instanceof List<?>) {
			List<?> list = (List<?>) target;
			if (checkDictionaryElements(list)) {
				StringBuilder builder = new StringBuilder();
				for (Object dic : list) {
					builder.append("StoryRunner:\r\n");
					builder.append(printDictionary((Dictionary<?, ?>) dic));
					builder.append("\r\n");
					builder.append("\r\n");
				}
				return builder.toString();
			}
		}

		return null;
	}

	private boolean checkDictionaryElements(List<?> list) {
		for (Object element : list) {
			if (!(element instanceof Dictionary<?, ?>)) {
				return false;
			}
		}

		return true;
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
