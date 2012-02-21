package org.jbehave.osgi.examples.taskweb.integration_tests.embedders;

import static org.jbehave.core.reporters.Format.HTML;

import java.text.SimpleDateFormat;
import java.util.List;

import org.jbehave.core.InjectableEmbedder;
import org.jbehave.core.annotations.Configure;
import org.jbehave.core.annotations.UsingEmbedder;
import org.jbehave.core.annotations.UsingSteps;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.embedder.StoryControls;
import org.jbehave.core.failures.FailingUponPendingStep;
import org.jbehave.core.failures.RethrowingFailure;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.junit.AnnotatedEmbedderRunner;
import org.jbehave.core.parsers.RegexPrefixCapturingPatternParser;
import org.jbehave.core.steps.ParameterConverters.DateConverter;
import org.jbehave.osgi.configuration.OsgiConfiguration;
import org.jbehave.osgi.examples.taskweb.integration_tests.embedders.EquinoxServerAnnotatedEmbedder.MyDateConverter;
import org.jbehave.osgi.examples.taskweb.integration_tests.embedders.EquinoxServerAnnotatedEmbedder.MyEmbedder;
import org.jbehave.osgi.examples.taskweb.integration_tests.embedders.EquinoxServerAnnotatedEmbedder.MyOsgiConfiguration;
import org.jbehave.osgi.examples.taskweb.integration_tests.embedders.EquinoxServerAnnotatedEmbedder.MyRegexPrefixCapturingPatternParser;
import org.jbehave.osgi.examples.taskweb.integration_tests.embedders.EquinoxServerAnnotatedEmbedder.MyReportBuilder;
import org.jbehave.osgi.examples.taskweb.integration_tests.embedders.EquinoxServerAnnotatedEmbedder.MyStoryControls;
import org.jbehave.osgi.examples.taskweb.integration_tests.embedders.EquinoxServerAnnotatedEmbedder.MyStoryLoader;
import org.jbehave.osgi.examples.taskweb.integration_tests.steps.EquinoxVerificationSteps;
import org.jbehave.osgi.io.OsgiStoryFinder;
import org.jbehave.osgi.reporters.OsgiStoryReporterBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AnnotatedEmbedderRunner.class)
@Configure(using = MyOsgiConfiguration.class, stepPatternParser = MyRegexPrefixCapturingPatternParser.class, storyControls = MyStoryControls.class, storyLoader = MyStoryLoader.class, storyReporterBuilder = MyReportBuilder.class, parameterConverters = { MyDateConverter.class })
@UsingEmbedder(embedder = MyEmbedder.class, generateViewAfterStories = true, verboseFailures = true, ignoreFailureInStories = false, ignoreFailureInView = true, storyTimeoutInSecs = 100, threads = 1, metaFilters = "-skip")
// @UsingPaths(storyFinder=OsgiStoryFinder.class, includes="*.story", searchIn =
// "/stories/server_product")
@UsingSteps(instances = { EquinoxVerificationSteps.class })
public class EquinoxServerAnnotatedEmbedder extends InjectableEmbedder {

	@Test
	public void run() {
		List<String> storyPaths = new OsgiStoryFinder().findPaths(
				"/stories/server_product", "*.story", "");
		injectedEmbedder().runStoriesAsPaths(storyPaths);
	}

	public static class MyEmbedder extends Embedder {
		public MyEmbedder() {
		}
	}

	public static class MyOsgiConfiguration extends OsgiConfiguration {
		public MyOsgiConfiguration() {
			useFailureStrategy(new RethrowingFailure());
			usePendingStepStrategy(new FailingUponPendingStep());

		}
	}

	public static class MyStoryControls extends StoryControls {
		public MyStoryControls() {
			doDryRun(false);
			doSkipScenariosAfterFailure(false);
		}
	}

	public static class MyStoryLoader extends LoadFromClasspath {
		public MyStoryLoader() {
			super();
		}
	}

	public static class MyReportBuilder extends OsgiStoryReporterBuilder {
		public MyReportBuilder() {
			this.withFormats(HTML).withFailureTrace(true)
					.withFailureTraceCompression(true);
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
