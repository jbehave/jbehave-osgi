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
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.parsers.RegexPrefixCapturingPatternParser;
import org.jbehave.core.steps.ParameterConverters.DateConverter;
import org.jbehave.osgi.configuration.OsgiConfiguration;
import org.jbehave.osgi.embedder.OsgiEmbedder;
import org.jbehave.osgi.examples.rcpmail.runner.SwtbotAnnotatedEmbedderRunner;
import org.jbehave.osgi.examples.rcpmail.stories.embedder.RCPmailAnnotatedEmbedder.MyDateConverter;
import org.jbehave.osgi.examples.rcpmail.stories.embedder.RCPmailAnnotatedEmbedder.MyEmbedder;
import org.jbehave.osgi.examples.rcpmail.stories.embedder.RCPmailAnnotatedEmbedder.MyRegexPrefixCapturingPatternParser;
import org.jbehave.osgi.examples.rcpmail.stories.embedder.RCPmailAnnotatedEmbedder.MyReportBuilder;
import org.jbehave.osgi.examples.rcpmail.stories.embedder.RCPmailAnnotatedEmbedder.MyStoryControls;
import org.jbehave.osgi.examples.rcpmail.stories.embedder.RCPmailAnnotatedEmbedder.MyStoryLoader;
import org.jbehave.osgi.examples.rcpmail.stories.steps.RCPmailCoreSteps;
import org.jbehave.osgi.io.OsgiStoryFinder;
import org.jbehave.osgi.reporters.OsgiStoryReporterBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SwtbotAnnotatedEmbedderRunner.class)
@Configure(using = OsgiConfiguration.class, stepPatternParser = MyRegexPrefixCapturingPatternParser.class, storyControls = MyStoryControls.class, storyLoader = MyStoryLoader.class, storyReporterBuilder = MyReportBuilder.class, parameterConverters = { MyDateConverter.class })
@UsingEmbedder(embedder = MyEmbedder.class, generateViewAfterStories = true, ignoreFailureInStories = true, ignoreFailureInView = true, storyTimeoutInSecs = 100, threads = 1, metaFilters = "-skip")
@UsingSteps(instances = { RCPmailCoreSteps.class })
public class RCPmailAnnotatedEmbedder extends InjectableEmbedder {

    @Test
    public void run() {
        List<String> storyPaths = new OsgiStoryFinder().findPaths("/stories/rcpmail", "*.story", "");
        injectedEmbedder().runStoriesAsPaths(storyPaths);
    }

    public static class MyEmbedder extends OsgiEmbedder {
        public MyEmbedder() {
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

    public static class MyRegexPrefixCapturingPatternParser extends RegexPrefixCapturingPatternParser {
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
