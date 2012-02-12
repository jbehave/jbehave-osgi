package org.jbehave.osgi.examples.taskweb.integration_tests.embedders;

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
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.embedder.StoryControls;
import org.jbehave.core.failures.FailingUponPendingStep;
import org.jbehave.core.failures.RethrowingFailure;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.junit.AnnotatedEmbedderRunner;
import org.jbehave.core.parsers.RegexPrefixCapturingPatternParser;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.ParameterConverters.DateConverter;
import org.jbehave.core.steps.SilentStepMonitor;
import org.jbehave.osgi.examples.taskweb.integration_tests.embedders.TaskManagerApplicationAnnotatedEmbedder.MyDateConverter;
import org.jbehave.osgi.examples.taskweb.integration_tests.embedders.TaskManagerApplicationAnnotatedEmbedder.MyEmbedder;
import org.jbehave.osgi.examples.taskweb.integration_tests.embedders.TaskManagerApplicationAnnotatedEmbedder.MyInjectedAuthenticationSteps;
import org.jbehave.osgi.examples.taskweb.integration_tests.embedders.TaskManagerApplicationAnnotatedEmbedder.MyLifeCycleSteps;
import org.jbehave.osgi.examples.taskweb.integration_tests.embedders.TaskManagerApplicationAnnotatedEmbedder.MyOsgiConfiguration;
import org.jbehave.osgi.examples.taskweb.integration_tests.embedders.TaskManagerApplicationAnnotatedEmbedder.MyRegexPrefixCapturingPatternParser;
import org.jbehave.osgi.examples.taskweb.integration_tests.embedders.TaskManagerApplicationAnnotatedEmbedder.MyReportBuilder;
import org.jbehave.osgi.examples.taskweb.integration_tests.embedders.TaskManagerApplicationAnnotatedEmbedder.MyScreenshotOnFailureSteps;
import org.jbehave.osgi.examples.taskweb.integration_tests.embedders.TaskManagerApplicationAnnotatedEmbedder.MyStoryControls;
import org.jbehave.osgi.examples.taskweb.integration_tests.embedders.TaskManagerApplicationAnnotatedEmbedder.MyStoryLoader;
import org.jbehave.osgi.examples.taskweb.integration_tests.pages.TaskWebPageFactory;
import org.jbehave.osgi.examples.taskweb.integration_tests.steps.AuthenticationSteps;
import org.jbehave.osgi.io.OsgiStoryFinder;
import org.jbehave.osgi.reporters.OsgiStoryReporterBuilder;
import org.jbehave.osgi.web.SeleniumOsgiConfiguration;
import org.jbehave.web.selenium.ContextView;
import org.jbehave.web.selenium.LocalFrameContextView;
import org.jbehave.web.selenium.PerStoriesWebDriverSteps;
import org.jbehave.web.selenium.PropertyWebDriverProvider;
import org.jbehave.web.selenium.SeleniumContext;
import org.jbehave.web.selenium.SeleniumStepMonitor;
import org.jbehave.web.selenium.WebDriverProvider;
import org.jbehave.web.selenium.WebDriverScreenshotOnFailure;
import org.jbehave.web.selenium.WebDriverSteps;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.util.concurrent.MoreExecutors;

@RunWith(AnnotatedEmbedderRunner.class)
@Configure(using = MyOsgiConfiguration.class, stepPatternParser = MyRegexPrefixCapturingPatternParser.class, storyControls = MyStoryControls.class, storyLoader = MyStoryLoader.class, storyReporterBuilder = MyReportBuilder.class, parameterConverters = { MyDateConverter.class })
@UsingEmbedder(embedder = MyEmbedder.class, generateViewAfterStories = true, ignoreFailureInStories = false, ignoreFailureInView = true, verboseFailures = true, storyTimeoutInSecs = 100, threads = 1, metaFilters = "-skip")
//@UsingPaths("")
@UsingSteps(instances = { MyLifeCycleSteps.class,
		MyInjectedAuthenticationSteps.class, MyScreenshotOnFailureSteps.class })
public class TaskManagerApplicationAnnotatedEmbedder extends InjectableEmbedder {

	private static final WebDriverProvider staticDriverProvider = new PropertyWebDriverProvider();
	private static WebDriverSteps lifecycleSteps = new PerStoriesWebDriverSteps(
			staticDriverProvider);
	private static final TaskWebPageFactory pages = new TaskWebPageFactory(
			staticDriverProvider);
	private static SeleniumContext context = new SeleniumContext();
	private static StoryReporterBuilder staticStoryReporterBuilder = new OsgiStoryReporterBuilder();
	private static ContextView contextView = new LocalFrameContextView().sized(
			500, 100);

	@Test
	public void run() {
		List<String> storyPaths = new OsgiStoryFinder().findPaths(
				"/stories/application", "*.story", "");
		injectedEmbedder().runStoriesAsPaths(storyPaths);
	}

	public static class MyOsgiConfiguration extends SeleniumOsgiConfiguration {
		public MyOsgiConfiguration() {
			useFailureStrategy(new RethrowingFailure());
			usePendingStepStrategy(new FailingUponPendingStep());
			useSeleniumContext(context);
			useWebDriverProvider(staticDriverProvider);
			useStepMonitor(new SeleniumStepMonitor(contextView, context,
					new SilentStepMonitor()));
			useStoryReporterBuilder(staticStoryReporterBuilder);
		}
	}

	public static class MyEmbedder extends Embedder {
		public MyEmbedder() {

			if (lifecycleSteps instanceof PerStoriesWebDriverSteps) {
				useExecutorService(MoreExecutors.sameThreadExecutor());
			}
		}
	}

	public static class MyScreenshotOnFailureSteps extends
			WebDriverScreenshotOnFailure {
		public MyScreenshotOnFailureSteps() {
			super(staticDriverProvider, staticStoryReporterBuilder);
		}
	}

	public static class MyLifeCycleSteps extends PerStoriesWebDriverSteps {
		public MyLifeCycleSteps() {

			super(staticDriverProvider);
		}
	}

	public static class MyInjectedAuthenticationSteps extends
			AuthenticationSteps {

		public MyInjectedAuthenticationSteps() {
			super(staticDriverProvider, pages);
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
