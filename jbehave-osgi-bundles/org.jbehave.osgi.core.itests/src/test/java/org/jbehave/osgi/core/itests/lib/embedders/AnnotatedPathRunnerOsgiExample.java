package org.jbehave.osgi.core.itests.lib.embedders;

import static org.jbehave.core.reporters.Format.CONSOLE;

import org.jbehave.core.annotations.Configure;
import org.jbehave.core.annotations.UsingEmbedder;
import org.jbehave.core.annotations.UsingPaths;
import org.jbehave.core.annotations.UsingSteps;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.embedder.StoryControls;
import org.jbehave.core.parsers.RegexPrefixCapturingPatternParser;
import org.jbehave.osgi.core.io.LoadFromBundleClasspath;
import org.jbehave.osgi.core.io.StoryFinderOsgiBundle;
import org.jbehave.osgi.core.itests.lib.embedders.AnnotatedPathRunnerOsgiExample.MyRegexPrefixCapturingPatternParser;
import org.jbehave.osgi.core.itests.lib.embedders.AnnotatedPathRunnerOsgiExample.MyReportBuilder;
import org.jbehave.osgi.core.itests.lib.embedders.AnnotatedPathRunnerOsgiExample.MyStoryControls;
import org.jbehave.osgi.core.itests.lib.embedders.AnnotatedPathRunnerOsgiExample.MyStoryLoader;
import org.jbehave.osgi.core.itests.lib.steps.NamedParametersSteps;
import org.jbehave.osgi.core.reporters.StoryReporterBuilderOsgi;
import org.osgi.framework.Bundle;

import com.thoughtworks.paranamer.NullParanamer;

/**
 * This annotated class is using @UsingSteps, so the StoryRunner service to be
 * generated and registered will be bound to an InjectableStepsFactory service
 * providing this Step class.
 * 
 * @author Cristiano Gavião
 *
 */
@Configure(paranamer=NullParanamer.class, stepPatternParser = MyRegexPrefixCapturingPatternParser.class, storyControls = MyStoryControls.class, storyLoader = MyStoryLoader.class, storyReporterBuilder = MyReportBuilder.class)
@UsingEmbedder(generateViewAfterStories = true, ignoreFailureInStories = true, ignoreFailureInView = true, storyTimeoutInSecs = 100, threads = 1, metaFilters = "-skip")
@UsingSteps(instances = { NamedParametersSteps.class })
@UsingPaths(storyFinder = StoryFinderOsgiBundle.class, searchIn = "/org/jbehave/osgi/core/itests/stories", includes = { "*.story" })
public class AnnotatedPathRunnerOsgiExample {

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
