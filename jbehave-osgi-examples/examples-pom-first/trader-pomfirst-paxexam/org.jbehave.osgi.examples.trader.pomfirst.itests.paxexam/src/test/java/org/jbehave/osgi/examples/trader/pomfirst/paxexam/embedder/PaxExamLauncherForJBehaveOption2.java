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
 * This example uses the PAX-Exam test framework to instantiate an OSGi
 * container, install bundles containing the stories and steps and execute the
 * required stories. <br>
 * 
 * This option uses of the {@link @UsingStoryRunnerServiceFilter} classes
 * attribute to specify which {@link StoryRunnerService} must be bound and
 * executed.
 * 
 * @author cvgaviao
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