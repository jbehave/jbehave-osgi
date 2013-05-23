package org.jbehave.osgi.equinox.commands.completers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.equinox.console.completion.common.Completer;
import org.jbehave.osgi.equinox.commands.AbstractEmbedderCommand;

/**
 * 
 * @author Cristiano Gavião
 */
public class StoryRunnerCompleter extends AbstractEmbedderCommand implements
		Completer {

	private static final Pattern PATTERN = Pattern
			.compile("\\A(storyRunner|storyrunner)(?:\\s+)(\\-r|\\-d|\\-s)((?:\\s+\\S*)*)");

	@Override
	public Map<String, Integer> getCandidates(String buffer, int cursor) {

		Map<String, Integer> result = new HashMap<String, Integer>();
		Matcher matcher = PATTERN.matcher(buffer);
		if (matcher.matches()) {

			Set<String> set = getStoryRunnerServices().keySet();

			if (cursor == matcher.end(2) + 1) {

				for (Iterator<String> iterator = set.iterator(); iterator
						.hasNext();) {
					String string = iterator.next();
					result.put(string, cursor);
				}
			} else if (cursor == matcher.end(3)) {
				String group3 = matcher.group(3);
				for (Iterator<String> iterator = set.iterator(); iterator
						.hasNext();) {
					String string = iterator.next();
					if (group3.contains(string)) {
						continue;
					} else
						result.put(string, cursor);
				}
			}
		}
		return result;
	}
}