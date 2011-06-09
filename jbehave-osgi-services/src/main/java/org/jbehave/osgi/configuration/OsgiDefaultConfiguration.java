package org.jbehave.osgi.configuration;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.embedder.StoryControls;
import org.jbehave.core.failures.PassingUponPendingStep;
import org.jbehave.core.failures.RethrowingFailure;
import org.jbehave.core.i18n.LocalizedKeywords;
import org.jbehave.core.parsers.RegexPrefixCapturingPatternParser;
import org.jbehave.core.parsers.RegexStoryParser;
import org.jbehave.core.reporters.FreemarkerViewGenerator;
import org.jbehave.core.reporters.PrintStreamStepdocReporter;
import org.jbehave.core.steps.MarkUnmatchedStepsAsPending;
import org.jbehave.core.steps.SilentStepMonitor;
import org.jbehave.core.steps.StepFinder;
import org.jbehave.osgi.io.OsgiLoadFromClasspath;

import com.thoughtworks.paranamer.NullParanamer;

public class OsgiDefaultConfiguration extends Configuration {

	public OsgiDefaultConfiguration() {
		
        useKeywords(new LocalizedKeywords());
        useStoryControls(new StoryControls());
        useStoryLoader(new OsgiLoadFromClasspath());
        useStoryParser(new RegexStoryParser(keywords()));
        useFailureStrategy(new RethrowingFailure());
        usePendingStepStrategy(new PassingUponPendingStep());
        useStepCollector(new MarkUnmatchedStepsAsPending());
        useStepFinder(new StepFinder());
        useStepPatternParser(new RegexPrefixCapturingPatternParser());
        useStepMonitor(new SilentStepMonitor());
        useStepdocReporter(new PrintStreamStepdocReporter());
        useParanamer(new NullParanamer());
        useViewGenerator(new FreemarkerViewGenerator());
	}

}
