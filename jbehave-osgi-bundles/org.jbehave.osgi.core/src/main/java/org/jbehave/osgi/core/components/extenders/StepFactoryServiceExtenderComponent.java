package org.jbehave.osgi.core.components.extenders;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.jbehave.osgi.core.Constants;
import org.jbehave.osgi.core.components.AbstractExtenderComponent;
import org.jbehave.osgi.core.configuration.dto.StepFactoryDTO;
import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.log.LogService;

/**
 * 
 * @author Cristiano Gavião
 */
@Component(enabled = true, immediate = true)
public class StepFactoryServiceExtenderComponent extends
		AbstractExtenderComponent {

	public StepFactoryServiceExtenderComponent() {
		setExtenderManifestHeader(Constants.STEP_FACTORY_EXTENDER_MANIFEST_HEADER);
		setExtenderPropertyGroup(Constants.STEP_FACTORY_EXTENDER_PROPERTY_GROUP);
		setExtenderPropertyItem(Constants.STEP_FACTORY_EXTENDER_PROPERTY_ITEM);
		setExtenderTargetFactoryPid(Constants.STEP_FACTORY_FPID);
	}

    @Activate
    @Override
    protected void activate(ComponentContext context) {
        super.activate(context);
    }
    
	@Reference(cardinality = ReferenceCardinality.MANDATORY)
	@Override
	protected void bindConfigurationAdmin(ConfigurationAdmin configurationAdmin) {
		super.bindConfigurationAdmin(configurationAdmin);
	}

	@Reference
	@Override
	protected void bindLogService(LogService logService) {
		super.bindLogService(logService);
	}

	/**
	 * Determines if the given type is a {@link Class} containing at least one
	 * method annotated with annotations from package
	 * "org.jbehave.core.annotations".
	 * 
	 * @param type
	 *            the Type of the steps instance
	 * @return A boolean, <code>true</code> if at least one annotated method is
	 *         found.
	 */
	protected boolean hasAnnotatedMethods(Type type) {
		if (type != null && type instanceof Class<?>) {
		    Method[] methods = ((Class<?>) type).getMethods();
		    
			for (Method method : methods) {
				for (Annotation annotation : method.getAnnotations()) {
					if (annotation.annotationType().getName()
							.startsWith("org.jbehave.core.annotations")) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * It took a header string in the format of :
	 * 
	 * <pre>
	 * FACTORY_NAME_1:STEP_CLASS_1_1;STEP_CLASS_1_1,
	 * FACTORY_NAME_2:STEP_CLASS_2_1;STEP_CLASS_2_1
	 * </pre>
	 */
	@Override
	public void onExtensionAddition(Bundle extendeeBundle, String[] headerValues) {

		Map<String, StepFactoryDTO> factoryDTOs = new HashMap<>();

		for (int i = 0; i < headerValues.length; i++) {
			String stepFactoryManifestHeaderItem = headerValues[i];
			if (stepFactoryManifestHeaderItem != null
					&& !stepFactoryManifestHeaderItem.isEmpty()) {
				// extract the name of the factory.
				StepFactoryDTO stepFactoryDto = extractStepFactoryFromHeader(
						extendeeBundle, stepFactoryManifestHeaderItem);
				if (factoryDTOs.containsKey(stepFactoryDto.getName())) {
					// merge classes
					StepFactoryDTO existentStepFactoryDto = factoryDTOs
							.get(stepFactoryDto.getName());
					existentStepFactoryDto.getStepClasses().addAll(
							stepFactoryDto.getStepClasses());
				} else {
					factoryDTOs.put(stepFactoryDto.getName(), stepFactoryDto);
				}
			}
		}

		for (StepFactoryDTO stepFactoryDTO : factoryDTOs.values()) {

			registerStepFactoryServiceForBundle(extendeeBundle, stepFactoryDTO);
		}
	}

	/**
	 * It took a header string in the format of :
	 * 
	 * <pre>
	 * FACTORY_NAME_1:STEP_CLASS_1_1;STEP_CLASS_1_1
	 * </pre>
	 * 
	 * @param factories
	 *            an comma separated string containing the step factory name and
	 *            its step classes separated by semicolons.
	 * @return
	 */
	private StepFactoryDTO extractStepFactoryFromHeader(Bundle extendeeBundle,
			String stepFactoryManifestHeaderItem) {
		// search the name. if its not found, use "default" as the name.
		String name = "default";
		StepFactoryDTO factoryDTO = null;
		int colonIdx = stepFactoryManifestHeaderItem
				.indexOf(Constants.CHAR_COLLON);
		if (colonIdx > 0) {
			name = stepFactoryManifestHeaderItem.substring(0, colonIdx);
		}

		String stepClassPatternStr = stepFactoryManifestHeaderItem
				.substring(colonIdx + 1);
		if (stepClassPatternStr != null && !stepClassPatternStr.isEmpty()) {
			factoryDTO = new StepFactoryDTO(name);
			String[] stepClassPatterns = stepClassPatternStr
					.split(Constants.CHAR_SEMICOLLON);
			for (String stepClassPattern : stepClassPatterns) {
				if (JAVA_CLASS_QUALIFIED_NAME_PATTERN.matcher(stepClassPattern)
						.matches()) {
					processStepFactoryManifestHeaderClass(extendeeBundle,
							factoryDTO, stepClassPattern);
				}

				else if (JAVA_QUALIFIED_NAME_PATTERN_WILDCARD.matcher(
						stepClassPattern).matches()) {
					stepClassPatternStr = stepClassPatternStr.replace(".", "/");
					String path = "/".concat(stepClassPatternStr.substring(0,
							stepClassPatternStr.lastIndexOf("/") + 1));
					String pattern = stepClassPatternStr.substring(
							stepClassPatternStr.lastIndexOf("/") + 1).concat(
							".class");
					logDebug("Searching for: " + path + pattern);

					BundleWiring bundleWiring = extendeeBundle
							.adapt(BundleWiring.class);
					Collection<String> classes = bundleWiring.listResources(
							path, pattern, BundleWiring.LISTRESOURCES_LOCAL);
					for (Iterator<String> iterator = classes.iterator(); iterator
							.hasNext();) {
						// it is a Class and transform dots
						String className = iterator.next();
						if (className.contains("$")) {
							continue;
						}
						String classFixedName = className.substring(0,
								className.lastIndexOf(".class")).replace("/",
								".");
						processStepFactoryManifestHeaderClass(extendeeBundle,
								factoryDTO, classFixedName);
					}
				}
			}
		}

		return factoryDTO;
	}

	@Override
	public void onExtensionRemoval(Bundle extendeeBundle, Object object) {
		deleteFactoryConfigurationsForExtendeeBundle(
				extendeeBundle.getSymbolicName(), extendeeBundle.getVersion()
						.toString());
		logDebug("Deleted Story Runner Service configurations for bundle '"
				+ extendeeBundle.getSymbolicName() + "'.");
	}

	/**
	 * Process the step class and add it to the bundle's step factory instance.
	 * 
	 * @param stepClassesToProvide
	 *            a list of names of classes that will be added to the
	 *            StepFactory.
	 * 
	 * @param extendeeBundle
	 * @param className
	 */
	private void processStepFactoryManifestHeaderClass(Bundle extendeeBundle,
			StepFactoryDTO stepFactoryDto, String className) {
		Class<?> clazz = null;
		try {
			clazz = extendeeBundle.loadClass(className);

			if (hasAnnotatedMethods(clazz)) {
				// has annotations then I will add it to the service properties
				// so it can be loaded again later.
				stepFactoryDto.getStepClasses().add(className);
			} else {
				logDebug("Ignoring step class " + className + " for "
						+ extendeeBundle.getSymbolicName());
			}
		} catch (ClassNotFoundException e) {
			logError("Could not instantiate " + className, e);
		}
	}

	private void registerStepFactoryServiceForBundle(Bundle extendeeBundle,
			StepFactoryDTO stepFactoryDTO) {
		Dictionary<String, Object> properties = new Hashtable<>();

		// this will trigger DS to register a service using the passed
		// properties.
		createFactoryConfigurationForExtendeeWithMultipleItems(
				extendeeBundle.getSymbolicName(), extendeeBundle.getVersion()
						.toString(), stepFactoryDTO.getName(),
				stepFactoryDTO.getStepClasses(), properties);
	}

}
