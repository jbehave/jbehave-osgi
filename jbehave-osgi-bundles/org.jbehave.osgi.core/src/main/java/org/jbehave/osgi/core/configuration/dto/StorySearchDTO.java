package org.jbehave.osgi.core.configuration.dto;

import java.util.List;

/**
 * 
 * @author Cristiano Gavião
 *
 */
public class StorySearchDTO {

	private String searchIn;
	private List<String> includes;
	private List<String> excludes;
	private String storyFinder;

	public StorySearchDTO() {
	}

	public List<String> getExcludes() {
		return excludes;
	}

	public List<String> getIncludes() {
		return includes;
	}

	public String getSearchIn() {
		return searchIn;
	}

	public String getStoryFinder() {
		return storyFinder;
	}

	public void setExcludes(List<String> excludes) {
		this.excludes = excludes;
	}

	public void setIncludes(List<String> includes) {
		this.includes = includes;
	}

	public void setSearchIn(String searchIn) {
		this.searchIn = searchIn;
	}

	public void setStoryFinder(String storyFinder) {
		this.storyFinder = storyFinder;
	}

}
