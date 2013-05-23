package org.jbehave.osgi.equinox.commands;

import java.util.Map;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.jbehave.osgi.interactive.services.StoryRunnerService;
import org.osgi.service.component.ComponentContext;

import com.google.common.collect.Maps;

/**
 * 
 * @author Cristiano Gavião
 *
 */
public abstract class AbstractEmbedderCommand {

	private ComponentContext componentContext;

	private final ConcurrentNavigableMap<String, StoryRunnerService> storyRunnerServices = new ConcurrentSkipListMap<String, StoryRunnerService>();

	protected NavigableMap<String,StoryRunnerService> getStoryRunnerServices() {
		return Maps.unmodifiableNavigableMap(storyRunnerServices);
	}

	protected void activate(ComponentContext context) {
		componentContext = context;
	}

	protected void deactivate(ComponentContext context) {
		componentContext = null;
	}

	public ComponentContext getComponentContext() {
		return componentContext;
	}

	protected void bindStoryRunnerService(
			StoryRunnerService storyRunnerService, Map<?, ?> properties) {
		String key = storyRunnerService.getStoryClassName();
		if (key != null && !key.isEmpty())
			storyRunnerServices.putIfAbsent(key, storyRunnerService);
	}

	protected void unbindStoryRunnerService(
			StoryRunnerService storyRunnerService, Map<?, ?> properties) {
		if (this.storyRunnerServices == storyRunnerService) {
			String key = storyRunnerService.getStoryClassName();
			if (key != null && !key.isEmpty())
				getStoryRunnerServices().remove(key);
		}
	}

}
