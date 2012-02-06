package com.c4biz.osgiutils.examples.vaadin_shiro.it.embedders;

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
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.junit.AnnotatedEmbedderRunner;
import org.jbehave.core.parsers.RegexPrefixCapturingPatternParser;
import org.jbehave.core.steps.ParameterConverters.DateConverter;
import org.jbehave.osgi.configuration.OsgiConfiguration;
import org.jbehave.osgi.io.OsgiStoryFinder;
import org.jbehave.osgi.reporters.OsgiStoryReporterBuilder;
import org.jbehave.web.selenium.ContextView;
import org.jbehave.web.selenium.LocalFrameContextView;
import org.jbehave.web.selenium.PerStoriesWebDriverSteps;
import org.jbehave.web.selenium.PropertyWebDriverProvider;
import org.jbehave.web.selenium.SeleniumContext;
import org.jbehave.web.selenium.WebDriverProvider;
import org.jbehave.web.selenium.WebDriverSteps;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.c4biz.osgiutils.examples.vaadin_shiro.it.embedders.TaskManagerApplicationAnnotatedEmbedder.MyDateConverter;
import com.c4biz.osgiutils.examples.vaadin_shiro.it.embedders.TaskManagerApplicationAnnotatedEmbedder.MyEmbedder;
import com.c4biz.osgiutils.examples.vaadin_shiro.it.embedders.TaskManagerApplicationAnnotatedEmbedder.MyRegexPrefixCapturingPatternParser;
import com.c4biz.osgiutils.examples.vaadin_shiro.it.embedders.TaskManagerApplicationAnnotatedEmbedder.MyReportBuilder;
import com.c4biz.osgiutils.examples.vaadin_shiro.it.embedders.TaskManagerApplicationAnnotatedEmbedder.MyStoryControls;
import com.c4biz.osgiutils.examples.vaadin_shiro.it.embedders.TaskManagerApplicationAnnotatedEmbedder.MyStoryLoader;
import com.c4biz.osgiutils.examples.vaadin_shiro.it.pages.PageFactory;
import com.c4biz.osgiutils.examples.vaadin_shiro.it.steps.AuthenticationSteps;

@RunWith(AnnotatedEmbedderRunner.class)
@Configure(using = OsgiConfiguration.class, stepPatternParser = MyRegexPrefixCapturingPatternParser.class, storyControls = MyStoryControls.class, storyLoader = MyStoryLoader.class, storyReporterBuilder = MyReportBuilder.class, parameterConverters = { MyDateConverter.class })
@UsingEmbedder(embedder = MyEmbedder.class, generateViewAfterStories = true, ignoreFailureInStories = true, ignoreFailureInView = true, storyTimeoutInSecs = 100, threads = 1, metaFilters = "-skip")
@UsingSteps(instances = { AuthenticationSteps.class })
public class TaskManagerApplicationAnnotatedEmbedder extends InjectableEmbedder {

	private WebDriverProvider driverProvider = new PropertyWebDriverProvider();
	private WebDriverSteps lifecycleSteps = new PerStoriesWebDriverSteps(
			driverProvider);
	private PageFactory pages = new PageFactory(driverProvider);
	private SeleniumContext context = new SeleniumContext();
	private ContextView contextView = new LocalFrameContextView().sized(500,
			100);

	@Test
	public void run() {
		List<String> storyPaths = new OsgiStoryFinder().findPaths(
				"/stories/application", "*.story", "");
		injectedEmbedder().runStoriesAsPaths(storyPaths);
	}

	public static class MyEmbedder extends Embedder {
		public MyEmbedder() {
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
