package org.jbehave.osgi.examples.rcpmail.stories.embedder;

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
import org.jbehave.core.parsers.RegexPrefixCapturingPatternParser;
import org.jbehave.core.steps.ParameterConverters.DateConverter;
import org.jbehave.osgi.core.configuration.ConfigurationOsgi;
import org.jbehave.osgi.core.embedder.EmbedderOsgi;
import org.jbehave.osgi.core.io.LoadFromBundleClasspath;
import org.jbehave.osgi.core.io.StoryFinderOsgi;
import org.jbehave.osgi.core.reporters.StoryReporterBuilderOsgi;
import org.jbehave.osgi.equinox.swtbot.junit.SwtbotAnnotatedEmbedderRunner;
import org.jbehave.osgi.examples.rcpmail.stories.embedder.RCPmailAnnotatedEmbedder.MyDateConverter;
import org.jbehave.osgi.examples.rcpmail.stories.embedder.RCPmailAnnotatedEmbedder.MyEmbedder;
import org.jbehave.osgi.examples.rcpmail.stories.embedder.RCPmailAnnotatedEmbedder.MyRegexPrefixCapturingPatternParser;
import org.jbehave.osgi.examples.rcpmail.stories.embedder.RCPmailAnnotatedEmbedder.MyReportBuilder;
import org.jbehave.osgi.examples.rcpmail.stories.embedder.RCPmailAnnotatedEmbedder.MyStoryControls;
import org.jbehave.osgi.examples.rcpmail.stories.embedder.RCPmailAnnotatedEmbedder.MyStoryLoader;
import org.jbehave.osgi.examples.rcpmail.stories.steps.RCPmailCoreSteps;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This embedder was created to run the JBehave stories that tests a Eclipse RCP
 * application using the SWTBot test framework.
 * <p>
 * I needed to use specifics OSGi story finder {@link OsgiStoryFinder} and report builder {@link OsgiStoryReporterBuilder}. 
 * <p>
 * To to be able to capture screens when errors happens in test I needed to create a specific EmbedderRunner, {@link SwtbotAnnotatedEmbedderRunner}.
 * 
 * @author Cristiano Gavião
 * 
 */

@RunWith(SwtbotAnnotatedEmbedderRunner.class)
@Configure(using = ConfigurationOsgi.class, stepPatternParser = MyRegexPrefixCapturingPatternParser.class, storyControls = MyStoryControls.class, storyLoader = MyStoryLoader.class, storyReporterBuilder = MyReportBuilder.class, parameterConverters = { MyDateConverter.class })
@UsingEmbedder(embedder = MyEmbedder.class, generateViewAfterStories = false, ignoreFailureInStories = false, ignoreFailureInView = true, storyTimeoutInSecs = 100, threads = 1, metaFilters = "-skip")
@UsingSteps(instances = { RCPmailCoreSteps.class })
public class RCPmailAnnotatedEmbedder extends InjectableEmbedder {

	@Test
	public void run() {
		List<String> storyPaths = new StoryFinderOsgi(this.getClass()).findPaths(
				"/stories/rcpmail", "*.story", "");
		injectedEmbedder().runStoriesAsPaths(storyPaths);
	}

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
