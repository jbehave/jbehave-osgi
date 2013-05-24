package org.jbehave.osgi.examples.trader.it.embedder;

import static org.jbehave.core.reporters.Format.CONSOLE;
import static org.jbehave.core.reporters.Format.HTML;
import static org.jbehave.core.reporters.Format.TXT;
import static org.jbehave.core.reporters.Format.XML;

import java.text.SimpleDateFormat;
import java.util.List;

import org.jbehave.core.InjectableEmbedder;
import org.jbehave.core.annotations.Configure;
import org.jbehave.core.annotations.UsingEmbedder;
import org.jbehave.core.annotations.UsingSteps;
import org.jbehave.core.embedder.StoryControls;
import org.jbehave.core.junit.AnnotatedEmbedderRunner;
import org.jbehave.core.parsers.RegexPrefixCapturingPatternParser;
import org.jbehave.core.steps.ParameterConverters.DateConverter;
import org.jbehave.osgi.core.configuration.ConfigurationOsgi;
import org.jbehave.osgi.core.embedder.EmbedderOsgi;
import org.jbehave.osgi.core.io.LoadFromBundleClasspath;
import org.jbehave.osgi.core.io.StoryFinderOsgi;
import org.jbehave.osgi.core.reporters.StoryReporterBuilderOsgi;
import org.jbehave.osgi.examples.trader.it.embedder.TraderAnnotatedEmbedderRunnerOsgi.MyDateConverter;
import org.jbehave.osgi.examples.trader.it.embedder.TraderAnnotatedEmbedderRunnerOsgi.MyEmbedder;
import org.jbehave.osgi.examples.trader.it.embedder.TraderAnnotatedEmbedderRunnerOsgi.MyRegexPrefixCapturingPatternParser;
import org.jbehave.osgi.examples.trader.it.embedder.TraderAnnotatedEmbedderRunnerOsgi.MyReportBuilder;
import org.jbehave.osgi.examples.trader.it.embedder.TraderAnnotatedEmbedderRunnerOsgi.MyStoryControls;
import org.jbehave.osgi.examples.trader.it.embedder.TraderAnnotatedEmbedderRunnerOsgi.MyStoryLoader;
import org.jbehave.osgi.examples.trader.it.steps1.AndSteps;
import org.jbehave.osgi.examples.trader.it.steps1.BeforeAfterSteps;
import org.jbehave.osgi.examples.trader.it.steps1.CalendarSteps;
import org.jbehave.osgi.examples.trader.it.steps1.PriorityMatchingSteps;
import org.jbehave.osgi.examples.trader.it.steps1.SandpitSteps;
import org.jbehave.osgi.examples.trader.it.steps1.SearchSteps;
import org.jbehave.osgi.examples.trader.it.steps1.TraderSteps;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author Cristiano Gavião
 *
 */
@RunWith(AnnotatedEmbedderRunner.class)
@Configure(using = ConfigurationOsgi.class, stepPatternParser = MyRegexPrefixCapturingPatternParser.class, storyControls = MyStoryControls.class, storyLoader = MyStoryLoader.class, storyReporterBuilder = MyReportBuilder.class, parameterConverters = { MyDateConverter.class })
@UsingEmbedder(embedder = MyEmbedder.class, generateViewAfterStories = true, ignoreFailureInStories = true, ignoreFailureInView = true, storyTimeoutInSecs = 100, threads = 1, metaFilters = "-skip")
@UsingSteps(instances = { TraderSteps.class, BeforeAfterSteps.class,
		AndSteps.class, CalendarSteps.class, PriorityMatchingSteps.class,
		SandpitSteps.class, SearchSteps.class })
public class TraderAnnotatedEmbedderRunnerOsgi extends InjectableEmbedder {

	@Test
	public void run() {
		List<String> storyPaths = new StoryFinderOsgi(this.getClass()).findPaths(
				"/org/jbehave/osgi/examples/trader/it/stories", new String[]{"*.story"},new String[]
				{"examples_table_loaded*","given_relative_path_story.story","step_composition.story"});
		injectedEmbedder().runStoriesAsPaths(storyPaths);
	}

	public static class MyEmbedder extends EmbedderOsgi {
		public MyEmbedder(Class<?> loadFromBundleClass) {
			super(loadFromBundleClass);
			// Properties properties = new Properties();
			// properties.setProperty("project.dir",
			// System.getProperty("project.dir", "N/A"));
			// useSystemProperties(properties);
		}
	}

	public static class MyStoryControls extends StoryControls {
		public MyStoryControls() {
			doDryRun(false);
			doSkipScenariosAfterFailure(false);
		}
	}

	public static class MyStoryLoader extends LoadFromBundleClasspath {
		public MyStoryLoader(Class<?> loadFromBundleClass) {
			super(loadFromBundleClass);
		}
	}

	public static class MyReportBuilder extends StoryReporterBuilderOsgi {
		public MyReportBuilder() {
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
