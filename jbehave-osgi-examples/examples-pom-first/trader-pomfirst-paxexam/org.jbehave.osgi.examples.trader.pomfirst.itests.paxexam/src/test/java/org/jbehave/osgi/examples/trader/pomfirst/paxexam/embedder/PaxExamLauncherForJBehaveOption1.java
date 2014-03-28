package org.jbehave.osgi.examples.trader.pomfirst.paxexam.embedder;

import javax.inject.Inject;

import org.jbehave.osgi.core.services.StoryRunnerService;
import org.jbehave.osgi.examples.trader.pomfirst.paxexam.PaxExamForkedContainerConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class PaxExamLauncherForJBehaveOption1 extends
        PaxExamForkedContainerConfiguration {

    @Inject
    StoryRunnerService storyRunnerService;

    public PaxExamLauncherForJBehaveOption1() {
    }

    @Test
    public void run() throws Throwable {

        // without a filter this will bind the first service found...
        storyRunnerService.run();
    }
}