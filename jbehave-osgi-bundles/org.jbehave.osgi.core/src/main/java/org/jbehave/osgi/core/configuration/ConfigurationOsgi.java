package org.jbehave.osgi.core.configuration;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.Keywords;
import org.jbehave.core.embedder.StoryControls;
import org.jbehave.core.failures.FailureStrategy;
import org.jbehave.core.failures.PassingUponPendingStep;
import org.jbehave.core.failures.PendingStepStrategy;
import org.jbehave.core.failures.RethrowingFailure;
import org.jbehave.core.i18n.LocalizedKeywords;
import org.jbehave.core.io.AbsolutePathCalculator;
import org.jbehave.core.io.PathCalculator;
import org.jbehave.core.io.StoryLoader;
import org.jbehave.core.io.StoryPathResolver;
import org.jbehave.core.io.UnderscoredCamelCaseResolver;
import org.jbehave.core.parsers.RegexPrefixCapturingPatternParser;
import org.jbehave.core.parsers.RegexStoryParser;
import org.jbehave.core.parsers.StepPatternParser;
import org.jbehave.core.parsers.StoryParser;
import org.jbehave.core.reporters.FreemarkerViewGenerator;
import org.jbehave.core.reporters.PrintStreamStepdocReporter;
import org.jbehave.core.reporters.StepdocReporter;
import org.jbehave.core.reporters.StoryReporter;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.reporters.ViewGenerator;
import org.jbehave.core.steps.MarkUnmatchedStepsAsPending;
import org.jbehave.core.steps.ParameterControls;
import org.jbehave.core.steps.ParameterConverters;
import org.jbehave.core.steps.PrintStreamStepMonitor;
import org.jbehave.core.steps.StepCollector;
import org.jbehave.core.steps.StepFinder;
import org.jbehave.core.steps.StepMonitor;
import org.jbehave.osgi.core.io.LoadFromBundleClasspath;
import org.jbehave.osgi.core.reporters.StoryReporterBuilderOsgi;
import org.osgi.framework.Bundle;

import com.thoughtworks.paranamer.NullParanamer;
import com.thoughtworks.paranamer.Paranamer;

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

	private Bundle ownerBundle;

	public ConfigurationOsgi(Bundle ownerBundle) {
		this.ownerBundle = ownerBundle;
	}

	@Override
	public FailureStrategy failureStrategy() {
		if (failureStrategy == null) {
			failureStrategy = new RethrowingFailure();
		}
		return failureStrategy;
	}

	@Override
	public Keywords keywords() {
		if (keywords == null) {
			keywords = new LocalizedKeywords();
		}
		return keywords;
	}

	@Override
	public ParameterControls parameterControls() {
		if (parameterControls == null) {
			parameterControls = new ParameterControls();
		}
		return parameterControls;
	}

	@Override
	public ParameterConverters parameterConverters() {
		if (parameterConverters == null) {
			parameterConverters = new ParameterConverters();
		}
		return parameterConverters;
	}

	@Override
	public Paranamer paranamer() {
		if (paranamer == null) {
			paranamer = new NullParanamer();
		}
		return paranamer;
	}

	@Override
	public PathCalculator pathCalculator() {
		if (pathCalculator == null) {
			pathCalculator = new AbsolutePathCalculator();
		}
		return pathCalculator;
	}

	@Override
	public PendingStepStrategy pendingStepStrategy() {
		if (pendingStepStrategy == null) {
			pendingStepStrategy = new PassingUponPendingStep();
		}
		return pendingStepStrategy;
	}

	@Override
	public StepCollector stepCollector() {
		if (stepCollector == null) {
			stepCollector = new MarkUnmatchedStepsAsPending();
		}
		return stepCollector;
	}

	@Override
	public StepdocReporter stepdocReporter() {
		if (stepdocReporter == null) {
			stepdocReporter = new PrintStreamStepdocReporter();
		}
		return stepdocReporter;
	}

	@Override
	public StepFinder stepFinder() {
		if (stepFinder == null) {
			stepFinder = new StepFinder();
		}
		return stepFinder;
	}

	@Override
	public StepMonitor stepMonitor() {
		if (stepMonitor == null) {
			stepMonitor = new PrintStreamStepMonitor();
		}
		return stepMonitor;
	}

	@Override
	public StepPatternParser stepPatternParser() {
		if (stepPatternParser == null) {
			stepPatternParser = new RegexPrefixCapturingPatternParser();
		}
		return stepPatternParser;
	}

	@Override
	public StoryControls storyControls() {
		if (storyControls == null) {
			storyControls = new StoryControls();
		}
		return storyControls;
	}

	@Override
	public StoryLoader storyLoader() {
		if (storyLoader == null) {
			storyLoader = new LoadFromBundleClasspath(ownerBundle);
		}
		return storyLoader;
	}

	@Override
	public StoryParser storyParser() {
		if (storyParser == null) {
			storyParser = new RegexStoryParser(this);
		}
		return storyParser;
	}

	@Override
	public StoryPathResolver storyPathResolver() {
		if (storyPathResolver == null) {
			storyPathResolver = new UnderscoredCamelCaseResolver();
		}
		return storyPathResolver;
	}

	@Override
	public StoryReporter storyReporter(String storyPath) {
        return storyReporterBuilder().build(storyPath);
    }

	@Override
	public StoryReporterBuilder storyReporterBuilder() {
		if (storyReporterBuilder == null) {
			storyReporterBuilder = new StoryReporterBuilderOsgi(ownerBundle, this);
		}
		return storyReporterBuilder;
	}

	@Override
	public ViewGenerator viewGenerator() {
		if (viewGenerator == null) {
			viewGenerator = new FreemarkerViewGenerator();
		}
		return viewGenerator;
	}
}
