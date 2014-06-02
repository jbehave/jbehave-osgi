package org.jbehave.osgi.examples.trader.pomfirst.paxexam.embedder;

import org.jbehave.osgi.core.annotations.UsingStoryRunnerServiceFilter;
import org.jbehave.osgi.core.services.StoryRunnerService;
import org.jbehave.osgi.examples.trader.pomfirst.itests.embedders.TraderAnnotatedPathRunnerOsgiWithStepFactoryServiceFilter;
import org.jbehave.osgi.examples.trader.pomfirst.paxexam.AbstractExampleTestConfiguration;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

/**
 * A launcher that calls story runner services registered in the OSGi container.
 * <p>
 * This example uses the PAX-Exam test framework to instantiate an OSGi
 * container, to install bundles containing the stories and steps and then
 * execute the specified stories. <br>
 * 
 * This option uses the {@link @UsingStoryRunnerServiceFilter} <i>classes</i>
 * attribute to specify which {@link StoryRunnerService } must be bound to the it
 * and executed.
 * 
 * @author Cristiano Gavião
 *
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
@UsingStoryRunnerServiceFilter(classes = { TraderAnnotatedPathRunnerOsgiWithStepFactoryServiceFilter.class })
public class PaxExamLauncherForJBehaveOption2 extends
		AbstractExampleTestConfiguration {

	public PaxExamLauncherForJBehaveOption2() {
	}

}