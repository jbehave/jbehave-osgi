package org.jbehave.osgi.core.configuration;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.embedder.StoryControls;
import org.jbehave.core.failures.PassingUponPendingStep;
import org.jbehave.core.failures.RethrowingFailure;
import org.jbehave.core.i18n.LocalizedKeywords;
import org.jbehave.core.io.AbsolutePathCalculator;
import org.jbehave.core.parsers.RegexPrefixCapturingPatternParser;
import org.jbehave.core.parsers.RegexStoryParser;
import org.jbehave.core.reporters.ConsoleOutput;
import org.jbehave.core.reporters.FreemarkerViewGenerator;
import org.jbehave.core.reporters.PrintStreamStepdocReporter;
import org.jbehave.core.steps.MarkUnmatchedStepsAsPending;
import org.jbehave.core.steps.ParameterControls;
import org.jbehave.core.steps.ParameterConverters;
import org.jbehave.core.steps.SilentStepMonitor;
import org.jbehave.core.steps.StepFinder;
import org.jbehave.osgi.core.io.LoadFromBundleClasspath;
import org.jbehave.osgi.core.reporters.StoryReporterBuilderOsgi;

import com.thoughtworks.paranamer.NullParanamer;

/**
 * Because the classLoading specificities of OSGi we can't use the common
 * Configuration of JBehave.<br>
 * We need to set the StoryLoader with {@link LoadFromBundleClasspath} and pass
 * the test class as parameter.
 * 
 * @author Cristiano Gavião
 * 
 */
public class ConfigurationOsgi extends Configuration {

	public ConfigurationOsgi(Class<?> loadFromBundleClass) {

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
		useStoryReporterBuilder(new StoryReporterBuilderOsgi(loadFromBundleClass));
		
		useStepPatternParser(new RegexPrefixCapturingPatternParser());
		useParameterControls(new ParameterControls());
		useParameterConverters(new ParameterConverters());
		useDefaultStoryReporter(new ConsoleOutput());
		usePathCalculator(new AbsolutePathCalculator());
		useStoryLoader(new LoadFromBundleClasspath(loadFromBundleClass.getClassLoader()));
	}
}
