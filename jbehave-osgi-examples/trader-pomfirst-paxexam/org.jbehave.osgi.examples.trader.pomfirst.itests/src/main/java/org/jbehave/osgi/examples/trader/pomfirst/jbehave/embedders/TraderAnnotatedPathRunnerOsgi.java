package org.jbehave.osgi.examples.trader.pomfirst.jbehave.embedders;

import static org.jbehave.core.reporters.Format.CONSOLE;
import static org.jbehave.core.reporters.Format.HTML;
import static org.jbehave.core.reporters.Format.TXT;
import static org.jbehave.core.reporters.Format.XML;

import java.text.SimpleDateFormat;

import org.jbehave.core.annotations.Configure;
import org.jbehave.core.annotations.UsingEmbedder;
import org.jbehave.core.annotations.UsingPaths;
import org.jbehave.core.annotations.UsingSteps;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.embedder.StoryControls;
import org.jbehave.core.parsers.RegexPrefixCapturingPatternParser;
import org.jbehave.core.steps.ParameterConverters.DateConverter;
import org.jbehave.osgi.core.configuration.ConfigurationOsgi;
import org.jbehave.osgi.core.embedder.EmbedderOsgi;
import org.jbehave.osgi.core.io.LoadFromBundleClasspath;
import org.jbehave.osgi.core.io.StoryFinderOsgiBundle;
import org.jbehave.osgi.core.reporters.StoryReporterBuilderOsgi;
import org.jbehave.osgi.examples.trader.pomfirst.jbehave.embedders.TraderAnnotatedPathRunnerOsgi.MyDateConverter;
import org.jbehave.osgi.examples.trader.pomfirst.jbehave.embedders.TraderAnnotatedPathRunnerOsgi.MyRegexPrefixCapturingPatternParser;
import org.jbehave.osgi.examples.trader.pomfirst.jbehave.embedders.TraderAnnotatedPathRunnerOsgi.MyReportBuilder;
import org.jbehave.osgi.examples.trader.pomfirst.jbehave.embedders.TraderAnnotatedPathRunnerOsgi.MyStoryControls;
import org.jbehave.osgi.examples.trader.pomfirst.jbehave.embedders.TraderAnnotatedPathRunnerOsgi.MyStoryLoader;
import org.jbehave.osgi.examples.trader.pomfirst.jbehave.steps1.AndSteps;
import org.jbehave.osgi.examples.trader.pomfirst.jbehave.steps1.BeforeAfterSteps;
import org.jbehave.osgi.examples.trader.pomfirst.jbehave.steps1.CalendarSteps;
import org.jbehave.osgi.examples.trader.pomfirst.jbehave.steps1.PriorityMatchingSteps;
import org.jbehave.osgi.examples.trader.pomfirst.jbehave.steps1.SandpitSteps;
import org.jbehave.osgi.examples.trader.pomfirst.jbehave.steps1.SearchSteps;
import org.jbehave.osgi.examples.trader.pomfirst.jbehave.steps1.TraderSteps;
import org.osgi.framework.Bundle;

/**
 * 
 * @author Cristiano Gavião
 *
 */
@Configure(using = ConfigurationOsgi.class, stepPatternParser = MyRegexPrefixCapturingPatternParser.class, storyControls = MyStoryControls.class, storyLoader = MyStoryLoader.class, storyReporterBuilder = MyReportBuilder.class, parameterConverters = { MyDateConverter.class })
@UsingEmbedder(embedder = EmbedderOsgi.class, generateViewAfterStories = true, ignoreFailureInStories = true, ignoreFailureInView = true, storyTimeoutInSecs = 100, threads = 1, metaFilters = "-skip", systemProperties = "java.awt.headless=true")
@UsingSteps(instances = { TraderSteps.class, BeforeAfterSteps.class,
		AndSteps.class, CalendarSteps.class, PriorityMatchingSteps.class,
		SandpitSteps.class, SearchSteps.class })
@UsingPaths(storyFinder = StoryFinderOsgiBundle.class, searchIn = "/org/jbehave/osgi/examples/trader/pomfirst/itests/stories", includes = { "*.story" }, excludes = {
		"examples_table*.story", "given_relative_path_story.story",
		"step_composition.story" })
public class TraderAnnotatedPathRunnerOsgi {

	public static class MyStoryControls extends StoryControls {
		public MyStoryControls() {
			doDryRun(false);
			doSkipScenariosAfterFailure(false);
		}
	}

	public static class MyStoryLoader extends LoadFromBundleClasspath {
		public MyStoryLoader(Bundle ownerBundle) {
			super(ownerBundle);
		}
	}

	public static class MyReportBuilder extends StoryReporterBuilderOsgi {
		public MyReportBuilder(Bundle ownerBundle, Configuration configuration) {
			super(ownerBundle, configuration);
			this.withFormats(CONSOLE, TXT, HTML, XML).withDefaultFormats();
		}
	}

	public static class MyRegexPrefixCapturingPatternParser extends
			RegexPrefixCapturingPatternParser {
		public MyRegexPrefixCapturingPatternParser() {
			super("%");
		}
	}

	public static class MyDateConverter extends DateConverter {
		public MyDateConverter() {
			super(new SimpleDateFormat("yyyy-MM-dd"));
		}
	}

}
