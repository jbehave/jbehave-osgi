package org.jbehave.osgi.core.itests.lib.embedders;

import static org.jbehave.core.reporters.Format.CONSOLE;

import org.jbehave.core.annotations.Configure;
import org.jbehave.core.annotations.UsingEmbedder;
import org.jbehave.core.annotations.UsingPaths;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.embedder.StoryControls;
import org.jbehave.core.parsers.RegexPrefixCapturingPatternParser;
import org.jbehave.core.steps.CompositeStepsFactory;
import org.jbehave.osgi.core.annotations.UsingStepsFactoryServiceFilter;
import org.jbehave.osgi.core.io.LoadFromBundleClasspath;
import org.jbehave.osgi.core.io.StoryFinderOsgiBundle;
import org.jbehave.osgi.core.itests.lib.embedders.AnnotatedPathRunnerOsgiWithStepFactoryServiceFilterExample2.MyRegexPrefixCapturingPatternParser;
import org.jbehave.osgi.core.itests.lib.embedders.AnnotatedPathRunnerOsgiWithStepFactoryServiceFilterExample2.MyReportBuilder;
import org.jbehave.osgi.core.itests.lib.embedders.AnnotatedPathRunnerOsgiWithStepFactoryServiceFilterExample2.MyStoryControls;
import org.jbehave.osgi.core.itests.lib.embedders.AnnotatedPathRunnerOsgiWithStepFactoryServiceFilterExample2.MyStoryLoader;
import org.jbehave.osgi.core.reporters.StoryReporterBuilderOsgi;
import org.osgi.framework.Bundle;

/**
 * This annotated class is using @UsingStepsFactoryServiceFilter containing a
 * LDAP based filter.
 * <p>
 * This way the StoryRunner service to be generated and registered will be bound
 * to an InjectableStepsFactory service found using the provided filter.<br>
 * Note that if more than one factory were found they will be "merged" into one
 * {@link CompositeStepsFactory}.
 * 
 * @author Cristiano Gavião
 *
 */
@Configure(stepPatternParser = MyRegexPrefixCapturingPatternParser.class, storyControls = MyStoryControls.class, storyLoader = MyStoryLoader.class, storyReporterBuilder = MyReportBuilder.class)
@UsingEmbedder(generateViewAfterStories = true, ignoreFailureInStories = true, ignoreFailureInView = true, storyTimeoutInSecs = 100, threads = 1, metaFilters = "-skip")
@UsingStepsFactoryServiceFilter(custom = "(&(objectClass=org.jbehave.osgi.core.services.InjectableStepsFactoryService)(stepFactoryId=stepFactory1)(stepFactoryId=stepFactory2))")
@UsingPaths(storyFinder = StoryFinderOsgiBundle.class, searchIn = "/org/jbehave/osgi/core/itests/stories", includes = { "*.story" }, excludes = { "unmatched_search_term.story" })
public class AnnotatedPathRunnerOsgiWithStepFactoryServiceFilterExample2 {

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
