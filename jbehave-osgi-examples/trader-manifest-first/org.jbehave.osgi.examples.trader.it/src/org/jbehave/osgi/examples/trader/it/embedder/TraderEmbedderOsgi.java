package org.jbehave.osgi.examples.trader.it.embedder;

import static org.jbehave.core.reporters.Format.CONSOLE;
import static org.jbehave.core.reporters.Format.HTML;
import static org.jbehave.core.reporters.Format.TXT;
import static org.jbehave.core.reporters.Format.XML;

import java.text.SimpleDateFormat;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.embedder.EmbedderControls;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.parsers.RegexPrefixCapturingPatternParser;
import org.jbehave.core.reporters.CrossReference;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.jbehave.core.steps.ParameterConverters;
import org.jbehave.core.steps.ParameterConverters.DateConverter;
import org.jbehave.core.steps.SilentStepMonitor;
import org.jbehave.osgi.core.configuration.ConfigurationOsgi;
import org.jbehave.osgi.core.embedder.EmbedderOsgi;
import org.jbehave.osgi.core.reporters.StoryReporterBuilderOsgi;
import org.jbehave.osgi.examples.trader.it.steps1.BeforeAfterSteps;
import org.jbehave.osgi.examples.trader.it.steps1.TraderSteps;
import org.jbehave.osgi.examples.trader.service.TradingService;

/**
 * Specifies the Embedder for the Trader example, providing the
 * Configuration and the CandidateSteps, using classpath story loading.
 */
public class TraderEmbedderOsgi extends EmbedderOsgi {

    public TraderEmbedderOsgi(Class<?> loadFromBundleClass) {
		super(loadFromBundleClass);
	}

	@Override
    public EmbedderControls embedderControls() {
        return new EmbedderControls().doIgnoreFailureInStories(true).doIgnoreFailureInView(true);
    }

    @Override
	public Configuration configuration() {
		Class<? extends TraderEmbedderOsgi> embedderClass = this.getClass();
		return new ConfigurationOsgi(this.getClass())
			.useStoryReporterBuilder(new StoryReporterBuilderOsgi()
        		.withCodeLocation(CodeLocations.codeLocationFromClass(embedderClass))
        		.withDefaultFormats()
				.withFormats(CONSOLE, TXT, HTML, XML)
				.withCrossReference(new CrossReference()))
            .useParameterConverters(new ParameterConverters()
                	.addConverters(new DateConverter(new SimpleDateFormat("yyyy-MM-dd")))) // use custom date pattern
            .useStepPatternParser(new RegexPrefixCapturingPatternParser(
							"%")) // use '%' instead of '$' to identify parameters
			.useStepMonitor(new SilentStepMonitor());								
	}

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(), new TraderSteps(new TradingService()), new BeforeAfterSteps());
    }

}