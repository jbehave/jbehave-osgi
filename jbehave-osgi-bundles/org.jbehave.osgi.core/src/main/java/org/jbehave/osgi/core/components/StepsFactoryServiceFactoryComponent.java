package org.jbehave.osgi.core.components;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jbehave.core.annotations.AsParameterConverter;
import org.jbehave.core.steps.CandidateSteps;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.ParameterConverters.MethodReturningConverter;
import org.jbehave.core.steps.ParameterConverters.ParameterConverter;
import org.jbehave.core.steps.Steps;
import org.jbehave.osgi.core.Constants;
import org.jbehave.osgi.core.services.InjectableStepsFactoryService;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.log.LogService;

@Component(servicefactory = true, configurationPid = Constants.STEP_FACTORY_FPID, service = { InjectableStepsFactoryService.class }, configurationPolicy = ConfigurationPolicy.REQUIRE)
public class StepsFactoryServiceFactoryComponent extends AbstractServiceComponent
        implements InjectableStepsFactoryService {

    @SuppressWarnings("serial")
    public static class StepsInstanceNotFound extends RuntimeException {

        public StepsInstanceNotFound(Class<?> type,
                InjectableStepsFactory stepsFactory) {
            super("Steps instance not found for type " + type + " in factory "
                    + stepsFactory);
        }

    }

    private final Map<Class<?>, Object> stepsInstances = new LinkedHashMap<Class<?>, Object>();

    private String[] stepClassesNames;

    public StepsFactoryServiceFactoryComponent() {
    }

    @Activate
    @Override
    protected void activate(ComponentContext context) {
        super.activate(context);

        Object stepFactoryIdObj = getProperties().get(
                Constants.STEP_FACTORY_EXTENDER_PROPERTY_GROUP);
        setExtendeeID(stepFactoryIdObj != null ? (String) stepFactoryIdObj
                : "no defined");

        Object stepClassesNamesObj = getProperties().get(
                Constants.STEP_FACTORY_EXTENDER_PROPERTY_ITEM);
        stepClassesNames = stepClassesNamesObj != null ? ((String[]) stepClassesNamesObj)
                : null;

        try {
            loadStepClassesInstances();
        } catch (Exception e) {
            logError(
                    "Error loading step classes. Component is being disabled.",
                    e);
        }
        logDebug("An InjectableStepsFactoryService was activated for: "
                + getStepFactoryId() + " from bundle: "
                + getStepFactoryBundleName());
    }

    @Reference
    @Override
    protected void bindLogService(LogService logService) {
        super.bindLogService(logService);
    }

    @Override
    public List<CandidateSteps> createCandidateSteps() {
        List<Class<?>> types = stepsTypes();
        List<CandidateSteps> steps = new ArrayList<CandidateSteps>();
        for (Class<?> type : types) {
            configuration().parameterConverters().addConverters(
                    methodReturningConverters(type));
            steps.add(new Steps(configuration(), type, this));
        }
        return steps;
    }

    @Override
    public Object createInstanceOfType(Class<?> type) {
        Object instance = stepsInstances.get(type);
        if (instance == null) {
            throw new StepsInstanceNotFound(type, this);
        }
        return instance;
    }

    @Deactivate
    @Override
    protected void deactivate(ComponentContext context) {
        super.deactivate(context);
    }

    @Override
    public String[] getStepClassesNames() {
        return stepClassesNames;
    }

    public String getStepFactoryBundleName() {
        return getExtendeeBundleName();
    }

    public String getStepFactoryBundleVersion() {
        return getExtendeeBundleVersion();
    }

    @Override
    public String getStepFactoryId() {
        return getExtendeeID();
    }

    private void loadStepClassesInstances() throws InstantiationException,
            IllegalAccessException {

        String[] classes = (String[]) getComponentContext().getProperties()
                .get(Constants.STEP_FACTORY_EXTENDER_PROPERTY_ITEM);
        for (int i = 0; i < classes.length; i++) {
            String stepClassName = classes[i];
            Class<Object> clazz;
            try {
                clazz = loadClass(stepClassName, Object.class);
                if (!Modifier.isAbstract(clazz.getModifiers())) {
                    Object obj = instanceOf(Object.class, clazz);
                    stepsInstances.put(clazz, obj);
                }
            } catch (ClassNotFoundException e) {
                logError("Fail to load StepClass.", e);
            }
        }
    }

    /**
     * Create parameter converters from methods annotated with @AsParameterConverter
     */
    private List<ParameterConverter> methodReturningConverters(Class<?> type) {
        List<ParameterConverter> converters = new ArrayList<ParameterConverter>();

        for (Method method : type.getMethods()) {
            if (method.isAnnotationPresent(AsParameterConverter.class)) {
                converters
                        .add(new MethodReturningConverter(method, type, this));
            }
        }

        return converters;
    }

    protected List<Class<?>> stepsTypes() {
        return new ArrayList<Class<?>>(stepsInstances.keySet());
    }

}
