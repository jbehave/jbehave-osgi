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
import org.jbehave.osgi.reporters.OsgiStoryReporterBuilder;

import com.thoughtworks.paranamer.NullParanamer;


/**
 * Because the classLoading specificities of OSGi we can't use the common Configuration of JBehave.
 *  
 * @author cvgaviao
 *
 */
public class OsgiConfiguration extends Configuration {

	public OsgiConfiguration() {
		useKeywords(new LocalizedKeywords());
		useStoryControls(new StoryControls());
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
		useStoryReporterBuilder(new OsgiStoryReporterBuilder());
	}

}
