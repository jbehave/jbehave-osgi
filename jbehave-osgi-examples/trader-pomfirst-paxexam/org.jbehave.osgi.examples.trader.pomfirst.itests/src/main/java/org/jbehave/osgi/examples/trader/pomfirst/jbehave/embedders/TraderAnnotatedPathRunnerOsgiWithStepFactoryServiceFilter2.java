package org.jbehave.osgi.examples.trader.pomfirst.jbehave.embedders;

import static org.jbehave.core.reporters.Format.CONSOLE;
import static org.jbehave.core.reporters.Format.XML;

import java.text.SimpleDateFormat;

import org.jbehave.core.annotations.Configure;
import org.jbehave.core.annotations.UsingEmbedder;
import org.jbehave.core.annotations.UsingPaths;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.embedder.StoryControls;
import org.jbehave.core.parsers.RegexPrefixCapturingPatternParser;
import org.jbehave.core.steps.ParameterConverters.DateConverter;
import org.jbehave.osgi.core.annotations.UsingStepsFactoryServiceFilter;
import org.jbehave.osgi.core.configuration.ConfigurationOsgi;
import org.jbehave.osgi.core.embedder.EmbedderOsgi;
import org.jbehave.osgi.core.io.LoadFromBundleClasspath;
import org.jbehave.osgi.core.io.StoryFinderOsgiBundle;
import org.jbehave.osgi.core.reporters.StoryReporterBuilderOsgi;
import org.jbehave.osgi.examples.trader.pomfirst.jbehave.embedders.TraderAnnotatedPathRunnerOsgiWithStepFactoryServiceFilter2.MyDateConverter;
import org.jbehave.osgi.examples.trader.pomfirst.jbehave.embedders.TraderAnnotatedPathRunnerOsgiWithStepFactoryServiceFilter2.MyRegexPrefixCapturingPatternParser;
import org.jbehave.osgi.examples.trader.pomfirst.jbehave.embedders.TraderAnnotatedPathRunnerOsgiWithStepFactoryServiceFilter2.MyReportBuilder;
import org.jbehave.osgi.examples.trader.pomfirst.jbehave.embedders.TraderAnnotatedPathRunnerOsgiWithStepFactoryServiceFilter2.MyStoryControls;
import org.jbehave.osgi.examples.trader.pomfirst.jbehave.embedders.TraderAnnotatedPathRunnerOsgiWithStepFactoryServiceFilter2.MyStoryLoader;
import org.osgi.framework.Bundle;

/**
 * 
 * @author Cristiano Gavião
 *
 */
@Configure(using = ConfigurationOsgi.class, stepPatternParser = MyRegexPrefixCapturingPatternParser.class, storyControls = MyStoryControls.class, storyLoader = MyStoryLoader.class, storyReporterBuilder = MyReportBuilder.class, parameterConverters = { MyDateConverter.class })
@UsingEmbedder(embedder = EmbedderOsgi.class, generateViewAfterStories = true, ignoreFailureInStories = true, ignoreFailureInView = true, storyTimeoutInSecs = 100, threads = 1, metaFilters = "-skip", systemProperties = "java.awt.headless=true")
@UsingStepsFactoryServiceFilter(custom = "(&(objectClass=org.jbehave.osgi.core.services.InjectableStepsFactoryService)(stepFactoryId=stepFactory1)(stepFactoryId=stepFactory2))")
@UsingPaths(storyFinder = StoryFinderOsgiBundle.class, searchIn = "/org/jbehave/osgi/examples/trader/pomfirst/itests/stories", includes = { "examples_table_loaded_from_classpath.story" })
public class TraderAnnotatedPathRunnerOsgiWithStepFactoryServiceFilter2 {

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
			this.withDefaultFormats().withFormats(CONSOLE, XML);
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
