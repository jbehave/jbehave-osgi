package org.jbehave.osgi.examples.trader.pomfirst.paxexam.embedder;

import static org.jbehave.core.reporters.Format.CONSOLE;
import static org.jbehave.core.reporters.Format.HTML;
import static org.jbehave.core.reporters.Format.TXT;
import static org.jbehave.core.reporters.Format.XML;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.url;

import java.text.SimpleDateFormat;
import java.util.List;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.embedder.EmbedderControls;
import org.jbehave.core.parsers.RegexPrefixCapturingPatternParser;
import org.jbehave.core.reporters.CrossReference;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.jbehave.core.steps.ParameterConverters;
import org.jbehave.core.steps.ParameterConverters.DateConverter;
import org.jbehave.core.steps.SilentStepMonitor;
import org.jbehave.osgi.core.configuration.ConfigurationOsgi;
import org.jbehave.osgi.core.embedder.EmbedderOsgi;
import org.jbehave.osgi.core.io.StoryFinderOsgi;
import org.jbehave.osgi.core.reporters.StoryReporterBuilderOsgi;
import org.jbehave.osgi.examples.trader.pomfirst.bundle.service.TradingService;
import org.jbehave.osgi.examples.trader.pomfirst.paxexam.steps1.BeforeAfterSteps;
import org.jbehave.osgi.examples.trader.pomfirst.paxexam.steps1.TraderSteps;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.ops4j.pax.exam.util.PathUtils;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class TraderEmbedderOsgiForPaxExam {

	public TraderEmbedderOsgiForPaxExam() {
	}

	@org.ops4j.pax.exam.Configuration
	public Option[] config() {

		return options(
				mavenBundle("org.apache.servicemix.bundles",
						"org.apache.servicemix.bundles.xstream", "1.4.4_2"),
				mavenBundle("org.apache.servicemix.bundles",
						"org.apache.servicemix.bundles.xpp3", "1.1.4c_6"),
				mavenBundle("org.apache.servicemix.bundles",
						"org.apache.servicemix.bundles.xmlpull", "1.1.3.1_2"),
				mavenBundle("org.apache.servicemix.bundles",
						"org.apache.servicemix.bundles.freemarker", "2.3.19_1"),
				mavenBundle("org.apache.xmlcommons",
						"com.springsource.org.apache.xmlcommons", "1.3.4"),
//						mavenBundle("org.apache.servicemix.specs",
//								"org.apache.servicemix.specs.jaxp-api-1.4", "2.2.0"),
				mavenBundle("commons-lang", "commons-lang", "2.6"),
				mavenBundle("commons-collections", "commons-collections",
						"3.2.1"),
				mavenBundle("commons-io", "commons-io", "2.4"),
				mavenBundle("org.apache.servicemix.bundles",
						"org.apache.servicemix.bundles.paranamer", "2.5.2_1"),
				mavenBundle("com.google.guava", "guava", "14.0"),
				mavenBundle("org.jbehave.osgi", "org.jbehave.osgi.core",
						"1.0.0-SNAPSHOT"),
				url("reference:file:"
						+ PathUtils.getBaseDir()
						+ "/../org.jbehave.osgi.examples.trader.pomfirst.bundle/target/org.jbehave.osgi.examples.trader.pomfirst.bundle-1.0.0-SNAPSHOT.jar"),
				junitBundles());
	}

	@Test
	public void run() {

		Assert.assertTrue(true);

		List<String> storyPaths = new StoryFinderOsgi(this.getClass())
				.findPaths("/org/jbehave/osgi/examples/trader/pomfirst/paxexam/stories",
						new String[] { "*.story" }, new String[] {
								"examples_table_loaded*",
								"given_relative_path_story.story",
								"step_composition.story" });

		System.err.println(storyPaths);

		EmbedderOsgi embedderOsgi = new EmbedderOsgi(this.getClass());
		embedderOsgi.useConfiguration(configuration());
		embedderOsgi.useStepsFactory(stepsFactory());
		embedderOsgi.useEmbedderControls(embedderControls());

		embedderOsgi.runStoriesAsPaths(storyPaths);
	}

	public EmbedderControls embedderControls() {
		return new EmbedderControls().doIgnoreFailureInStories(true)
				.doIgnoreFailureInView(true);
	}

	public Configuration configuration() {
		Class<? extends TraderEmbedderOsgiForPaxExam> embedderClass = this
				.getClass();
		return new ConfigurationOsgi(this.getClass())
				.useStoryReporterBuilder(
						new StoryReporterBuilderOsgi(embedderClass)
								.withDefaultFormats()
								.withFormats(CONSOLE, TXT, HTML, XML)
								.withCrossReference(new CrossReference()))
				.useParameterConverters(
						new ParameterConverters()
								.addConverters(new DateConverter(
										new SimpleDateFormat("yyyy-MM-dd")))) // use
																				// custom
																				// date
																				// pattern
				.useStepPatternParser(
						new RegexPrefixCapturingPatternParser("%")) // use '%'
																	// instead
																	// of '$' to
																	// identify
																	// parameters
				.useStepMonitor(new SilentStepMonitor());
	}

	public InjectableStepsFactory stepsFactory() {
		return new InstanceStepsFactory(configuration(), new TraderSteps(
				new TradingService()), new BeforeAfterSteps());
	}

}