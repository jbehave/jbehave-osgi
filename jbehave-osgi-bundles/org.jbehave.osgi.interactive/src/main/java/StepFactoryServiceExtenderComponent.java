

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import org.jbehave.osgi.interactive.components.AbstractExtenderComponent;
import org.jbehave.osgi.interactive.services.StoryRunnerService;
import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;

/**
 * <p>
 * Default implementation of the JBehave Execution OSGi Service
 * {@link StoryRunnerService}
 * </p>
 * 
 * @author Cristiano Gavião
 */
public class StepFactoryServiceExtenderComponent extends
		AbstractExtenderComponent {

	@Override
	public void onExtensionAddition(Bundle bundle, String[] headerValues) {
		for (int i = 0; i < headerValues.length; i++) {
			String stepFactoryHeaderItem = headerValues[i];
			if (stepFactoryHeaderItem != null
					&& !stepFactoryHeaderItem.isEmpty()) {

				if (JAVA_CLASS_QUALIFIED_NAME_PATTERN.matcher(
						stepFactoryHeaderItem).matches()) {
					registerStepFactoryService(bundle, stepFactoryHeaderItem);
				}

				else if (JAVA_QUALIFIED_NAME_PATTERN_WILDCARD.matcher(
						stepFactoryHeaderItem).matches()) {
					stepFactoryHeaderItem = stepFactoryHeaderItem.replace(".",
							"/");
					String path = "/".concat(stepFactoryHeaderItem.substring(0,
							stepFactoryHeaderItem.lastIndexOf("/") + 1));
					String pattern = stepFactoryHeaderItem.substring(
							stepFactoryHeaderItem.lastIndexOf("/") + 1).concat(
							".class");
					logDebug("Searching for :" + path + pattern);

					BundleWiring bundleWiring = bundle
							.adapt(BundleWiring.class);
					Collection<String> classes = bundleWiring.listResources(
							path, pattern, BundleWiring.LISTRESOURCES_LOCAL);
					for (Iterator<String> iterator = classes.iterator(); iterator
							.hasNext();) {
						// it is a Class and transform dots
						String className = iterator.next();
						if (className.contains("$"))
							continue;
						String classFixedName = className.substring(0,
								className.lastIndexOf(".class")).replace("/",
								".");
						registerStepFactoryService(bundle, classFixedName);
					}
				}
			}
		}
	}

	@Override
	public void onExtensionRemoval(Bundle bundle, Object object) {
		deleteFactoryConfigurationForExtendedBundle(bundle.getSymbolicName());
		logDebug("Deleted Step Factory Service configurations for bundle '"
				+ bundle.getSymbolicName() + "'.");
	}

	private void registerStepFactoryService(Bundle bundle, String clazzName) {
		Hashtable<String, Object> config = new Hashtable<String, Object>();
		createFactoryConfigurationForExtendedItem(bundle.getSymbolicName(),
				clazzName, "", config);
	}

}
