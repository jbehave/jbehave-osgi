package org.jbehave.osgi.core.commands;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Semaphore;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.jbehave.osgi.core.components.AbstractComponent;
import org.jbehave.osgi.core.services.InjectableStepsFactoryService;
import org.jbehave.osgi.core.services.StoryRunnerService;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.log.LogService;

/**
 * 
 * @author Cristiano Gavião
 */
@Component(enabled = true, service = DefaultCommandsComponent.class, property = {
        "component.description=Command Provider for JBehave Story Runner Service",
        "osgi.command.scope=jbehave", "osgi.command.function=storyRunner",
        "osgi.command.function=stepsFactory", "osgi.command.function=str",
        "osgi.command.function=stf" })
public class DefaultCommandsComponent extends AbstractComponent {

    private final Set<StoryRunnerService> storyRunnerServices = new ConcurrentSkipListSet<StoryRunnerService>();
    private final Set<InjectableStepsFactoryService> stepsFactoryServices = new ConcurrentSkipListSet<InjectableStepsFactoryService>();
    private Semaphore semaphore;

    private static final String STEPS_FACTORY_ACTION_SHOW_DETAIL = "detail";
    private static final String STEPS_FACTORY_ACTION_LIST_ALL = "list";

    private static final String STORY_RUNNER_ACTION_LIST_ALL = "list";
    private static final String STORY_RUNNER_ACTION_SET_REPORT_OUTPUT = "output";
    private static final String STORY_RUNNER_ACTION_SHOW_DETAILS = "detail";
    private static final String STORY_RUNNER_ACTION_RUN = "run";
    private static final String STORY_RUNNER_ACTION_STORIES = "stories";

    @Activate
    @Override
    protected void activate(ComponentContext context) {
        super.activate(context);
        semaphore = new Semaphore(1);

    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MULTIPLE)
    protected void bindInjectableStepsFactoryService(
            InjectableStepsFactoryService injectableStepsFactoryService,
            Map<?, ?> properties) {
        getInjectableStepsFactoryServices().add(injectableStepsFactoryService);
    }

    @Override
    @Reference
    protected void bindLogService(LogService logService) {
        super.bindLogService(logService);
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MULTIPLE)
    protected void bindStoryRunnerService(
            StoryRunnerService storyRunnerService, Map<?, ?> properties) {
        getStoryRunnerServices().add(storyRunnerService);
    }

    public Set<InjectableStepsFactoryService> getInjectableStepsFactoryServices() {
        return stepsFactoryServices;
    }

    protected Set<StoryRunnerService> getStoryRunnerServices() {
        return storyRunnerServices;
    }

    private String printDictionary(Dictionary<String, ?> dic) {
        int count = dic.size();
        String[] keys = new String[count];
        Enumeration<?> keysEnum = dic.keys();
        int i = 0;
        while (keysEnum.hasMoreElements()) {
            keys[i++] = (String) keysEnum.nextElement();
        }
        Util.sortByString(keys);

        StringBuilder builder = new StringBuilder();
        for (i = 0; i < count; i++) {
            builder.append("\t\t" + keys[i] + " = ");
            Object value = dic.get(keys[i]);
            if (value.getClass().isArray()) {
                builder.append(Arrays.deepToString((Object[]) value));
            } else {
                builder.append(value.toString());
            }
            builder.append("\r\n");
        }
        builder.append("\r\n");
        return builder.toString();
    }

    public void stf() {
        stf("list", new InjectableStepsFactoryService[0]);
    }

    @Descriptor("Manage registered JBehave's StepFactory services.")
    public void stf(
            @Descriptor("StepsFactory services actions are:(list - all registered services| details)") String action,
            @Descriptor("One or more registered StepFactory services") InjectableStepsFactoryService... stepsFactoryServices) {
        stepsFactory(action, stepsFactoryServices);
    }

    public void str() {
        str("", "list", new StoryRunnerService[0]);
    }

    @Descriptor("Manage registered JBehave's StoryRunner services.")
    public void str(
            @Descriptor("sets the reporting output directory only for one run.") @Parameter(names = {
                    "-o", "--output" }, absentValue = "") String reportOutputDir,
            @Descriptor("StoryRunner services actions are:(list - all registered services| details | output | run | stories)") String action,
            @Descriptor("One or more registered StoryRunner services.") StoryRunnerService... storyRunnerServices) {
        storyRunner(reportOutputDir, action, storyRunnerServices);
    }

    @Descriptor("Manage registered JBehave's StepFactory services.")
    public void stepsFactory(
            @Descriptor("StepsFactory services actions are:(list - all registered services| details)") String action,
            @Descriptor("One or more registered StepFactory services") InjectableStepsFactoryService... stepsFactoryServices) {
        if (action.equalsIgnoreCase(STEPS_FACTORY_ACTION_LIST_ALL)
                || action.isEmpty()) {
            InjectableStepsFactoryService[] allStepsFactories = getInjectableStepsFactoryServices()
                    .toArray(
                            new InjectableStepsFactoryService[getInjectableStepsFactoryServices()
                                    .size()]);
            System.out.println("StepsFactories: ");
            for (int i = 0; i < allStepsFactories.length; i++) {
                InjectableStepsFactoryService stepsFactoryService = allStepsFactories[i];
                System.out.println("\t" + (i + 1) + ") "
                        + stepsFactoryService.getStepFactoryId() + " <"
                        + stepsFactoryService.getStepFactoryBundleName() + ":"
                        + stepsFactoryService.getStepFactoryBundleVersion()
                        + ">");
            }
            return;
        }

        // after here all actions needs at least one service specified.
        if (stepsFactoryServices == null || stepsFactoryServices.length == 0) {
            System.err.println("You must indicate"
                    + " at least one registered StepsFactory service.");
            return;
        }

        if (action.equalsIgnoreCase(STEPS_FACTORY_ACTION_SHOW_DETAIL)) {
            for (int i = 0; i < stepsFactoryServices.length; i++) {
                InjectableStepsFactoryService stepsFactoryService = stepsFactoryServices[i];
                System.out.println("\t" + (i + 1) + ") StepsFactory: "
                        + stepsFactoryService.getStepFactoryId());
                System.out.println("\t    Bundle: "
                        + stepsFactoryService.getStepFactoryBundleName() + ":"
                        + stepsFactoryService.getStepFactoryBundleVersion());
                System.out.println("\t    Steps Classes:");
                for (String stepClass : stepsFactoryService
                        .getStepClassesNames()) {
                    System.out.println("\t\t" + stepClass);
                }
            }
            return;
        }
    }

    @Descriptor("Manage registered JBehave's StoryRunner services.")
    public void storyRunner(
            @Descriptor("sets the reporting output directory only for one run.") @Parameter(names = {
                    "-o", "--output" }, absentValue = "") String reportOutputDir,
            @Descriptor("StoryRunner services actions are:(list - all registered services| details | output | run | stories)") String action,
            @Descriptor("One or more registered StoryRunner services.") StoryRunnerService... storyRunnerServices) {

        if (action.equalsIgnoreCase(STORY_RUNNER_ACTION_LIST_ALL)
                || action.isEmpty()) {
            StoryRunnerService[] allrunners = getStoryRunnerServices().toArray(
                    new StoryRunnerService[getStoryRunnerServices().size()]);
            System.out.println("StoryRunners: ");
            for (int i = 0; i < allrunners.length; i++) {
                StoryRunnerService storyRunnerService = allrunners[i];
                System.out.println("\t" + (i + 1) + ") "
                        + storyRunnerService.getStoryClassName() + " <"
                        + storyRunnerService.getStoryBundleName() + ">");
            }
            return;

        }
        if (storyRunnerServices == null || storyRunnerServices.length == 0) {
            System.err.println("You must indicate"
                    + " at least one registered StoryRunner service.");
            return;
        }

        if (action.equalsIgnoreCase(STORY_RUNNER_ACTION_SET_REPORT_OUTPUT)) {
            if (!reportOutputDir.isEmpty()) {
                System.out.println("Setting report output directory to: '"
                        + reportOutputDir + "'.");

                for (int i = 0; i < storyRunnerServices.length; i++) {
                    StoryRunnerService storyRunnerService = storyRunnerServices[i];
                    storyRunnerService.useReportOutputLocation(reportOutputDir);
                }
                return;
            }
        }

        if (action.equalsIgnoreCase(STORY_RUNNER_ACTION_RUN)) {
            for (int i = 0; i < storyRunnerServices.length; i++) {
                StoryRunnerService storyRunnerService = storyRunnerServices[i];
                try {
                    semaphore.acquire();
                    if (reportOutputDir != null && !reportOutputDir.isEmpty()) {
                        System.out.println("Using this report location: "
                                + reportOutputDir);
                        storyRunnerService.run(reportOutputDir);
                    } else {
                        System.out.println("Using this report location: "
                                + storyRunnerService.reportOutputLocation());
                        storyRunnerService.run();
                    }
                } catch (Throwable e) {
                    logError("Error in core command component.", e);
                } finally {
                    semaphore.release();
                }
            }
            return;
        }

        if (action.equalsIgnoreCase(STORY_RUNNER_ACTION_SHOW_DETAILS)) {
            System.out.println("StoryRunner Details:");
            for (int i = 0; i < storyRunnerServices.length; i++) {
                StoryRunnerService storyRunnerService = storyRunnerServices[i];
                System.out.println("\tStoryRunner: '"
                        + storyRunnerService.getStoryClassName() + "'");
                System.out.println("\t    Bundle: "
                        + storyRunnerService.getStoryBundleName() + ":"
                        + storyRunnerService.getStoryBundleVersion());

                System.out.println("\t    Default report location: '"
                        + storyRunnerService.reportOutputLocation() + "'");
                System.out.println("\t    Properties: ");
                Dictionary<String, ?> properties = storyRunnerService
                        .getConfigurationProperties();
                System.out.println(printDictionary(properties));
            }
            return;
        }

        if (action.equalsIgnoreCase(STORY_RUNNER_ACTION_STORIES)) {

        }
    }

    protected void unbindInjectableStepsFactoryService(
            InjectableStepsFactoryService injectableStepsFactoryService,
            Map<?, ?> properties) {
        getInjectableStepsFactoryServices().remove(
                injectableStepsFactoryService);
    }

    protected void unbindStoryRunnerService(
            StoryRunnerService storyRunnerService, Map<?, ?> properties) {
        getStoryRunnerServices().remove(storyRunnerService);
    }
}
