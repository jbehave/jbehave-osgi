package org.jbehave.osgi.examples.trader.pomfirst.paxexam.embedder;

import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;

import javax.inject.Inject;

import org.jbehave.osgi.core.services.StoryRunnerService;
import org.jbehave.osgi.paxexam.ProbeOptions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.ops4j.pax.exam.util.Filter;

/**
 * This example uses the PAX-Exam test framework to instantiate an OSGi
 * container, install bundles containing the stories and steps and execute the
 * required stories. <br>
 * 
 * This option uses of the Pax-Exam {@link @Filter} annotation to specify which
 * {@link StoryRunnerService} must be bound and executed.
 * 
 * @author Cristiano Gavião
 *
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class PaxExamLauncherForJBehaveOption1 {

    @Inject
    // without a filter this should bind the first service found...
    @Filter("(&(objectClass=org.jbehave.osgi.core.services.StoryRunnerService)(storyRunnerClass=org.jbehave.osgi.examples.trader.pomfirst.itests.embedders.TraderAnnotatedPathRunnerOsgiWithStepFactoryServiceFilter))")
    StoryRunnerService storyRunnerService;

    public PaxExamLauncherForJBehaveOption1() {
    }

    @Configuration
    public Option[] config() {

        return options(
                systemProperty("eclipse.consoleLog").value("true"),
                systemProperty("eclipse.log.level").value("DEBUG"),
                systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level")
                        .value("ALL"),
                ProbeOptions.jbehaveCoreAndDependencies(),
                mavenBundle("org.jbehave.osgi.examples",
                        "org.jbehave.osgi.examples.trader.pomfirst.application")
                        .versionAsInProject().start(),
                mavenBundle("org.jbehave.osgi.examples",
                        "org.jbehave.osgi.examples.trader.pomfirst.itests")
                        .versionAsInProject().start()
                        );
    }

    @Test
    public void run() throws Throwable {

        Assert.assertNotNull(storyRunnerService);
        storyRunnerService.run();
    }
}