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

@Component(servicefactory = true, configurationPid = Constants.STEP_FACTORY_FPID, service = { InjectableStepsFactoryService.class }, configurationPolicy = ConfigurationPolicy.REQUIRE)
public class StepsFactoryServiceFactoryComponent extends AbstractComponent
		implements InjectableStepsFactoryService,
		Comparable<StepsFactoryServiceFactoryComponent> {

	@SuppressWarnings("serial")
	public static class StepsInstanceNotFound extends RuntimeException {

		public StepsInstanceNotFound(Class<?> type,
				InjectableStepsFactory stepsFactory) {
			super("Steps instance not found for type " + type + " in factory "
					+ stepsFactory);
		}

	}

	private final Map<Class<?>, Object> stepsInstances = new LinkedHashMap<Class<?>, Object>();

	private String stepFactoryBundleName = "";

	private String stepFactoryBundleVersion = "";

	private String[] stepClassesNames;

	private String stepFactoryId = "";

	public StepsFactoryServiceFactoryComponent() {
	}

	@Activate
	@Override
	protected void activate(ComponentContext context) {
		super.activate(context);

		Object stepFactoryBundleNameObj = getProperties().get(
				Constants.EXTENDEE_BUNDLE);
		stepFactoryBundleName = stepFactoryBundleNameObj != null ? (String) stepFactoryBundleNameObj
				: "no defined";

		Object stepFactoryBundleVersionObj = getProperties().get(
				Constants.EXTENDEE_BUNDLE_VERSION);
		stepFactoryBundleVersion = stepFactoryBundleVersionObj != null ? (String) stepFactoryBundleVersionObj
				: "no defined";

		Object stepFactoryIdObj = getProperties().get(
				Constants.STEP_FACTORY_EXTENDER_PROPERTY_GROUP);
		stepFactoryId = stepFactoryIdObj != null ? (String) stepFactoryIdObj
				: "no defined";

		Object stepClassesNamesObj = getProperties().get(
				Constants.STEP_FACTORY_EXTENDER_PROPERTY_ITEM);
		stepClassesNames = stepClassesNamesObj != null ? ((String[]) stepClassesNamesObj)
				: null;
		setOwnerBundle(searchOwnerBundle(context.getBundleContext(), stepFactoryBundleName, stepFactoryBundleVersion));

		try {
			loadStepClassesInstances();
		} catch (Exception e) {
			logError(
					"Error loading step classes. Component is being disabled.",
					e);
		}
		logDebug("An InjectableStepsFactoryService was activated for: "
				+ stepFactoryId + " from bundle: " + stepFactoryBundleName);
	}

	@Override
	public int compareTo(StepsFactoryServiceFactoryComponent o) {

		return this.getStepFactoryId().compareTo(o.getStepFactoryId());
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
		return stepFactoryBundleName;
	}

	public String getStepFactoryBundleVersion() {
		return stepFactoryBundleVersion;
	}

	@Override
	public String getStepFactoryId() {
		return stepFactoryId;
	}

	private void loadStepClassesInstances() throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {

		String[] classes = (String[]) getComponentContext().getProperties()
				.get(Constants.STEP_FACTORY_EXTENDER_PROPERTY_ITEM);
		for (int i = 0; i < classes.length; i++) {
			String stepClassName = classes[i];
			Class<Object> clazz = loadClass(stepClassName, Object.class);
			if (!Modifier.isAbstract(clazz.getModifiers())) {
				Object obj = instanceOf(Object.class, clazz);
				stepsInstances.put(clazz, obj);
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
