package org.jbehave.osgi.examples.trader.pomfirst.paxexam.embedder;

import javax.inject.Inject;

import org.jbehave.osgi.core.services.StoryRunnerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.ops4j.pax.exam.util.Filter;

/**
 * This example uses the PAX-Exam test framework to instantiate an OSGi
 * container, install bundles containing the stories and steps and execute the
 * required stories. <br>
 * 
 * This option uses of the {@link PaxExam}'s default Junit runner.
 * <p>
 * Note that we are using the PaxExam service injection feature to bind only one
 * StoryRunner service that will be called in the run() test method.
 * 
 * 
 * @author cvgaviao
 *
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class PaxExamLauncherForJBehaveOption2 {

    @Inject
    @Filter("(&(objectClass=org.jbehave.osgi.core.services.StoryRunnerService)(storyRunnerClass=org.jbehave.osgi.examples.trader.pomfirst.jbehave.embedders.TraderAnnotatedPathRunnerOsgiWithStepFactoryServiceFilter))")
    StoryRunnerService storyRunnerService;

    public PaxExamLauncherForJBehaveOption2() {
    }

    @Test
    public void run() throws Throwable {
        storyRunnerService.run();
    }
}