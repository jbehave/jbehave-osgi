package org.jbehave.osgi.paxexam.junit;

import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;

import java.util.Collection;

import javax.inject.Inject;

import org.jbehave.osgi.core.annotations.UsingStoryRunnerServiceFilter;
import org.jbehave.osgi.core.services.StoryRunnerService;
import org.jbehave.osgi.paxexam.ProbeOptions;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

public abstract class AbstractPaxExamForStoryRunner {

    @Inject
    private BundleContext bc;

    protected volatile Collection<ServiceReference<StoryRunnerService>> services2Run = null;

    @Before
    public void beforeSetup() throws InvalidSyntaxException,
            InterruptedException {

        Filter filter = buildFilter();
        services2Run = bc.getServiceReferences(StoryRunnerService.class,
                filter.toString());

        Assert.assertNotNull(services2Run);
        Assert.assertEquals(services2Run.isEmpty(), false);
    }

    /**
     * A filter will be created using the information set using the annotation
     * {@link UsingStoryRunnerServiceFilter}.
     * 
     * @return the created filter object.
     * @throws InvalidSyntaxException
     *             the string used is not valid.
     */
    private Filter buildFilter() throws InvalidSyntaxException {
        StringBuilder filterBuilder = new StringBuilder();
        if (getClass().isAnnotationPresent(UsingStoryRunnerServiceFilter.class)) {
            String filter = getClass().getAnnotation(
                    UsingStoryRunnerServiceFilter.class).filter();
            if (!filter.isEmpty()) {
                filterBuilder.append(filter);
            } else {
                Class<?>[] classes = getClass().getAnnotation(
                        UsingStoryRunnerServiceFilter.class).classes();
                if (classes.length > 0) {
                    filterBuilder
                            .append("(&(objectClass=org.jbehave.osgi.core.services.StoryRunnerService)");
                    filterBuilder.append("(|");
                    for (int i = 0; i < classes.length; i++) {
                        filterBuilder.append("(storyRunnerClass=");
                        filterBuilder.append(classes[i].getName());
                        filterBuilder.append(")");
                    }
                    filterBuilder.append("))");
                } else {
                    filterBuilder
                            .append("(objectClass=org.jbehave.osgi.core.services.StoryRunnerService)");
                }
            }
        } else {
            filterBuilder
                    .append("(objectClass=org.jbehave.osgi.core.services.StoryRunnerService)");
        }

        return bc.createFilter(filterBuilder.toString());
    }

    @Configuration
    public Option[] config() {

        return options(
                systemProperty("eclipse.consoleLog").value("true"),
                systemProperty("eclipse.log.level").value("DEBUG"),
                systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level")
                        .value("ALL"),
                ProbeOptions.jbehaveCoreAndDependencies());
    }

    @After
    public void release() {
        services2Run = null;
    }

    /**
     * The method that starts JBehave story execution.
     * <p>
     * This method is executed by PaxExam inside an OSGi environment.<br>
     * 
     * @throws Throwable
     */
    @Test
    public synchronized void runAllServices() throws Throwable {

        Assert.assertNotNull(
                "No StoryRunnerService specified was found to be executed.",
                services2Run);
        for (ServiceReference<StoryRunnerService> serviceReference : services2Run) {
            StoryRunnerService storyRunnerService = bc
                    .getService(serviceReference);
            if (storyRunnerService != null) {
                storyRunnerService.run();
            }
        }

    }
}
