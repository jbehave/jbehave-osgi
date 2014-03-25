package org.jbehave.osgi.examples.trader.pomfirst.paxexam.embedder;

import org.jbehave.osgi.core.annotations.UsingStoryRunnerServiceFilter;
import org.jbehave.osgi.core.services.StoryRunnerService;
import org.jbehave.osgi.paxexam.junit.AbstractPaxExamForStoryRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
//import org.jbehave.osgi.examples.trader.pomfirst.bundle.service.TradingService;
//import org.jbehave.osgi.examples.trader.pomfirst.jbehave.steps1.AndSteps;
//import org.jbehave.osgi.examples.trader.pomfirst.jbehave.steps1.BeforeAfterSteps;
//import org.jbehave.osgi.examples.trader.pomfirst.jbehave.steps1.TraderSteps;

/**
 * This example uses the PAX-Exam test framework to instantiate an OSGi
 * environment, install the test bundles and execute the required stories.
 * <p>
 * 
 * This option uses of the {@link PaxExamForJBehave} Junit runner.
 * 
 * 
 * 
 * @author cvgaviao
 *
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
@UsingStoryRunnerServiceFilter("(&(objectClass=StoryRunnerService)(name=*))")
public class PaxExamLauncherForJBehaveOption3 extends
        AbstractPaxExamForStoryRunner {

    public PaxExamLauncherForJBehaveOption3() {
    }

    /**
     * By default the parent run() method tracks and run all registered
     * {@link StoryRunnerService} instance.<br>
     * Overriding the run() method gives the developer a way to choose which
     * services must be run using a LDAP filter.
     * 
     * @throws Throwable
     */
    @Test
    public void run() throws Throwable {

        if (getStoryRunnerServices() != null) {
        }

    }
}