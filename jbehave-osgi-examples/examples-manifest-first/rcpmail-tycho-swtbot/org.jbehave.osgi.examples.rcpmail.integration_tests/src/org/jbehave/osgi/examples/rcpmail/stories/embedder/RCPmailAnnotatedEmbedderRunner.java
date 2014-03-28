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
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.embedder.StoryControls;
import org.jbehave.core.parsers.RegexPrefixCapturingPatternParser;
import org.jbehave.core.steps.ParameterConverters.DateConverter;
import org.jbehave.osgi.core.configuration.ConfigurationOsgi;
import org.jbehave.osgi.core.embedder.EmbedderOsgi;
import org.jbehave.osgi.core.io.LoadFromBundleClasspath;
import org.jbehave.osgi.core.io.StoryFinderOsgiBundle;
import org.jbehave.osgi.core.reporters.StoryReporterBuilderOsgi;
import org.jbehave.osgi.equinox.swtbot.junit.SwtbotAnnotatedEmbedderRunner;
import org.jbehave.osgi.examples.rcpmail.stories.embedder.RCPmailAnnotatedEmbedderRunner.MyDateConverter;
import org.jbehave.osgi.examples.rcpmail.stories.embedder.RCPmailAnnotatedEmbedderRunner.MyEmbedder;
import org.jbehave.osgi.examples.rcpmail.stories.embedder.RCPmailAnnotatedEmbedderRunner.MyRegexPrefixCapturingPatternParser;
import org.jbehave.osgi.examples.rcpmail.stories.embedder.RCPmailAnnotatedEmbedderRunner.MyReportBuilder;
import org.jbehave.osgi.examples.rcpmail.stories.embedder.RCPmailAnnotatedEmbedderRunner.MyStoryControls;
import org.jbehave.osgi.examples.rcpmail.stories.embedder.RCPmailAnnotatedEmbedderRunner.MyStoryLoader;
import org.jbehave.osgi.examples.rcpmail.stories.steps.RCPmailCoreSteps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * This JBehave embedder was created to run the BDD stories that tests a Eclipse
 * RCP application using the Eclipse SWTBot test framework.
 * <p>
 * It can be executed by PDE's Junit Plugin Test or inside a Tycho buiding.<br>
 * As is required to run inside an OSGi container it was needed to use specific
 * setup classes as {@link ConfigurationOsgi}, {@link EmbedderOsgi},
 * {@link StoryFinderOsgi}, {@link LoadFromBundleClasspath} and
 * {@link StoryReporterBuilderOsgi}.
 * <p>
 * In order to be able to capture screens when exceptions to happen while
 * testing application was needed to use a specific Junit runner for SWTBot. see
 * {@link SwtbotAnnotatedEmbedderRunner}.
 * 
 * @author Cristiano Gavião
 * 
 */

@RunWith(SwtbotAnnotatedEmbedderRunner.class)
@Configure(using = ConfigurationOsgi.class, stepPatternParser = MyRegexPrefixCapturingPatternParser.class, storyControls = MyStoryControls.class, storyLoader = MyStoryLoader.class, storyReporterBuilder = MyReportBuilder.class, parameterConverters = { MyDateConverter.class })
@UsingEmbedder(embedder = MyEmbedder.class, generateViewAfterStories = true, ignoreFailureInStories = false, ignoreFailureInView = false, storyTimeoutInSecs = 100, threads = 1, metaFilters = "-skip")
@UsingSteps(instances = { RCPmailCoreSteps.class })
public class RCPmailAnnotatedEmbedderRunner extends InjectableEmbedder {

	@Test
	public void run() {
		
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		List<String> storyPaths = new StoryFinderOsgiBundle(bundle)
				.findPaths("/stories/rcpmail", "*.story", "");
		injectedEmbedder().runStoriesAsPaths(storyPaths);
	}

	public static class MyEmbedder extends EmbedderOsgi {

		public MyEmbedder(Bundle ownerBundle) {
			super(ownerBundle);
		}
	}

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
