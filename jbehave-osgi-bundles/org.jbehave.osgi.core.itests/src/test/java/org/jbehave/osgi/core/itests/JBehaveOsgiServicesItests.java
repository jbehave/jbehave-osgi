package org.jbehave.osgi.core.itests;

import static org.knowhowlab.osgi.testing.assertions.ServiceAssert.assertServiceAvailable;
import static org.knowhowlab.osgi.testing.utils.FilterUtils.and;
import static org.knowhowlab.osgi.testing.utils.FilterUtils.create;
import static org.knowhowlab.osgi.testing.utils.FilterUtils.eq;

import org.jbehave.osgi.core.services.InjectableStepsFactoryService;
import org.jbehave.osgi.core.services.StoryRunnerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class JBehaveOsgiServicesItests extends AbstractCommonTest {


    @Test
    public void ensureStepsFactoryServiceAreBeingCreated() throws Throwable {
        assertServiceAvailable(and(create(InjectableStepsFactoryService.class),
                eq("stepFactoryId", "stepFactory1")));

    }

    @Test
    public void ensureStoryRunnerServiceAreBeingCreated() throws Throwable {
        // asserts that test service with custom properties is available
        assertServiceAvailable(and(
                create(StoryRunnerService.class),
                eq("storyRunnerClass",
                        "org.jbehave.osgi.core.itests.lib.embedders.AnnotatedPathRunnerOsgiExample")));

    }

}