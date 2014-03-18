package org.jbehave.osgi.examples.trader.pomfirst.paxexam.embedder;

import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.url;

import javax.inject.Inject;

import org.jbehave.osgi.core.services.StoryRunnerService;
import org.jbehave.osgi.paxexam.junit.ProbeOptions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.ops4j.pax.exam.util.Filter;
import org.ops4j.pax.exam.util.PathUtils;

/**
 * This example uses the PAX-Exam test framework to instantiate an OSGi
 * environment, install the test bundles and execute the required stories. <br>
 * 
 * This option uses of the {@link PaxExam}'s default Junit runner.
 * <p>
 * Note that we are using the PaxExam service injection feature to bind only
 * one StoryRunner service that will be called in the run() test method.
 * 
 * 
 * @author cvgaviao
 *
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class PaxExamLauncherForJBehaveOption2 {

	@Inject
	@Filter("")
	StoryRunnerService storyRunnerService;

	public PaxExamLauncherForJBehaveOption2() {
	}

	@org.ops4j.pax.exam.Configuration
	public Option[] config() {

		return options(
				ProbeOptions.jbehaveCoreAndDeps(),
				url("reference:file:"
						+ PathUtils.getBaseDir()
						+ "/../org.jbehave.osgi.examples.trader.pomfirst.application/target/classes"),
				url("reference:file:"
						+ PathUtils.getBaseDir()
						+ "/../org.jbehave.osgi.examples.trader.pomfirst.itests/target/classes"));
	}

	@Test
	public void run() throws Throwable {
		storyRunnerService.run();
	}
}