package org.jbehave.osgi.core.itests.lib.embedders;

import static org.jbehave.core.reporters.Format.CONSOLE;

import org.jbehave.core.annotations.Configure;
import org.jbehave.core.annotations.UsingEmbedder;
import org.jbehave.core.annotations.UsingPaths;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.embedder.StoryControls;
import org.jbehave.core.parsers.RegexPrefixCapturingPatternParser;
import org.jbehave.osgi.core.annotations.UsingStepsFactoryServiceFilter;
import org.jbehave.osgi.core.io.LoadFromBundleClasspath;
import org.jbehave.osgi.core.io.StoryFinderOsgiBundle;
import org.jbehave.osgi.core.itests.lib.embedders.AnnotatedPathRunnerOsgiWithStepFactoryServiceFilterExample1.MyRegexPrefixCapturingPatternParser;
import org.jbehave.osgi.core.itests.lib.embedders.AnnotatedPathRunnerOsgiWithStepFactoryServiceFilterExample1.MyReportBuilder;
import org.jbehave.osgi.core.itests.lib.embedders.AnnotatedPathRunnerOsgiWithStepFactoryServiceFilterExample1.MyStoryControls;
import org.jbehave.osgi.core.itests.lib.embedders.AnnotatedPathRunnerOsgiWithStepFactoryServiceFilterExample1.MyStoryLoader;
import org.jbehave.osgi.core.reporters.StoryReporterBuilderOsgi;
import org.osgi.framework.Bundle;

/**
 * This annotated class is using @UsingStepsFactoryServiceFilter containing a
 * step factory ID.
 * <p>
 * This way the StoryRunner service to be generated and registered will be bound
 * to an InjectableStepsFactory service found using a fixed filter build using
 * the provided id value.
 * 
 * @author Cristiano Gavião
 *
 */
@Configure(stepPatternParser = MyRegexPrefixCapturingPatternParser.class, storyControls = MyStoryControls.class, storyLoader = MyStoryLoader.class, storyReporterBuilder = MyReportBuilder.class)
@UsingEmbedder(generateViewAfterStories = false, ignoreFailureInStories = true, ignoreFailureInView = true, storyTimeoutInSecs = 100, threads = 1, metaFilters = "-skip")
@UsingStepsFactoryServiceFilter("stepFactory1")
@UsingPaths(storyFinder = StoryFinderOsgiBundle.class, searchIn = "/org/jbehave/osgi/core/itests/stories", includes = { "*.story" }, excludes = { "named_parameters.story" })
public class AnnotatedPathRunnerOsgiWithStepFactoryServiceFilterExample1 {

    public static class MyStoryControls extends StoryControls {
        public MyStoryControls() {
            doDryRun(true);
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
            this.withFormats(CONSOLE);
        }
    }

    public static class MyRegexPrefixCapturingPatternParser extends
            RegexPrefixCapturingPatternParser {
        public MyRegexPrefixCapturingPatternParser() {
            super("%");
        }
    }
}
