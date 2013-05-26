package org.jbehave.osgi.examples.rcpmail.stories.embedder;

import static org.jbehave.core.reporters.Format.CONSOLE;
import static org.jbehave.core.reporters.Format.TXT;

import java.text.SimpleDateFormat;

import org.jbehave.core.annotations.Configure;
import org.jbehave.core.annotations.UsingEmbedder;
import org.jbehave.core.annotations.UsingPaths;
import org.jbehave.core.annotations.UsingSteps;
import org.jbehave.core.embedder.StoryControls;
import org.jbehave.core.parsers.RegexPrefixCapturingPatternParser;
import org.jbehave.core.steps.ParameterConverters.DateConverter;
import org.jbehave.osgi.core.configuration.ConfigurationOsgi;
import org.jbehave.osgi.core.embedder.EmbedderOsgi;
import org.jbehave.osgi.core.io.LoadFromBundleClasspath;
import org.jbehave.osgi.core.io.StoryFinderOsgi;
import org.jbehave.osgi.core.reporters.StoryReporterBuilderOsgi;
import org.jbehave.osgi.equinox.swtbot.junit.SwtbotAnnotatedPathRunner;
import org.jbehave.osgi.examples.rcpmail.stories.embedder.RCPmailAnnotatedPathRunner.MyDateConverter;
import org.jbehave.osgi.examples.rcpmail.stories.embedder.RCPmailAnnotatedPathRunner.MyEmbedder;
import org.jbehave.osgi.examples.rcpmail.stories.embedder.RCPmailAnnotatedPathRunner.MyRegexPrefixCapturingPatternParser;
import org.jbehave.osgi.examples.rcpmail.stories.embedder.RCPmailAnnotatedPathRunner.MyReportBuilder;
import org.jbehave.osgi.examples.rcpmail.stories.embedder.RCPmailAnnotatedPathRunner.MyStoryControls;
import org.jbehave.osgi.examples.rcpmail.stories.embedder.RCPmailAnnotatedPathRunner.MyStoryLoader;
import org.jbehave.osgi.examples.rcpmail.stories.steps.RCPmailCoreSteps;
import org.junit.runner.RunWith;

/**
 * This JBehave embedder was created to run the BDD stories that tests a Eclipse
 * RCP application using the Eclipse SWTBot test framework.
 * <p>
 * As this test should run inside an OSGi container it was needed to use
 * specifics setup classes as {@link ConfigurationOsgi}, {@link EmbedderOsgi},
 * {@link StoryFinderOsgi}, {@link LoadFromBundleClasspath} and
 * {@link StoryReporterBuilderOsgi}.
 * <p>
 * In order to be able to capture screens when errors happen in test was needed
 * to use a specific Junit runner for SWTBot see
 * {@link SwtbotAnnotatedPathRunner}.
 * 
 * @author Cristiano Gavião
 * 
 */

@RunWith(SwtbotAnnotatedPathRunner.class)
@Configure(using = ConfigurationOsgi.class, stepPatternParser = MyRegexPrefixCapturingPatternParser.class, storyControls = MyStoryControls.class, storyLoader = MyStoryLoader.class, storyReporterBuilder = MyReportBuilder.class, parameterConverters = { MyDateConverter.class })
@UsingEmbedder(embedder = MyEmbedder.class, generateViewAfterStories = false, ignoreFailureInStories = false, ignoreFailureInView = true, storyTimeoutInSecs = 100, threads = 1, metaFilters = "-skip")
@UsingSteps(instances = { RCPmailCoreSteps.class })
@UsingPaths(storyFinder = StoryFinderOsgi.class, searchIn = "/stories/rcpmail", includes = { "*.story" })
public class RCPmailAnnotatedPathRunner {

	public static class MyEmbedder extends EmbedderOsgi {
		public MyEmbedder(Class<?> loadFromBundleClass) {
			super(loadFromBundleClass);
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
		public MyReportBuilder(Class<?> loadFromBundleClass) {
			super(loadFromBundleClass);
			this.withFormats(CONSOLE, TXT).withDefaultFormats();
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
