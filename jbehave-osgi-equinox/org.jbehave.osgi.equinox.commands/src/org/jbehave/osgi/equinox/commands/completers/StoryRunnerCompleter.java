package org.jbehave.osgi.equinox.commands.completers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.equinox.console.completion.common.Completer;
import org.jbehave.osgi.core.components.AbstractComponent;
import org.jbehave.osgi.core.services.StoryRunnerService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.log.LogService;

/**
 * 
 * @author Cristiano Gavião
 */
@Component(immediate = true, service = Completer.class)
public class StoryRunnerCompleter extends AbstractComponent implements
		Completer {

	private final ConcurrentMap<String, StoryRunnerService> storyRunnerServices = new ConcurrentSkipListMap<String, StoryRunnerService>();

	private static final Pattern PATTERN = Pattern
			.compile("\\A(storyRunner|storyrunner)(?:\\s+)(\\-r|\\-d|\\-s)((?:\\s+\\S*)*)");

	@Reference
	@Override
	protected void bindLogService(LogService logService) {
		super.bindLogService(logService);
	}
	
	@Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MULTIPLE)
	protected void bindStoryRunnerService(
			StoryRunnerService storyRunnerService, Map<?, ?> properties) {
		getStoryRunnerServices().putIfAbsent(
				storyRunnerService.getStoryClassName(), storyRunnerService);
	}

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

	protected ConcurrentMap<String, StoryRunnerService> getStoryRunnerServices() {
		return storyRunnerServices;
	}
}