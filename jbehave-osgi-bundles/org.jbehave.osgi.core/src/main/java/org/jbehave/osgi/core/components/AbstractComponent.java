package org.jbehave.osgi.core.components;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jbehave.core.configuration.AnnotationBuilder.InstantiationFailed;
import org.jbehave.core.configuration.Configuration;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;

/**
 * 
 * @author Cristiano Gavião
 *
 */
public abstract class AbstractComponent {

	private volatile Configuration configuration = null;

	private volatile Bundle ownerBundle = null;

	private volatile ComponentContext componentContext = null;

	private AtomicReference<LogService> logService = new AtomicReference<LogService>();

	public AbstractComponent() {
	}

	protected void activate(ComponentContext context) {
		// save bundleContext reference...
		setComponentContext(context);
	}

	protected void bindLogService(LogService logService) {
		this.logService.set(logService);
		logDebug("Binded LogService for " + getClass().getSimpleName() + ".");
	}

	public Configuration configuration() {
		return configuration;
	}

	protected void deactivate(ComponentContext context) {
		setComponentContext(null);
	}

	protected ComponentContext getComponentContext() {
		return componentContext;
	}

	protected final LogService getLogService() {
		return logService.get();
	}

	public Bundle getOwnerBundle() {
		return ownerBundle;
	}

	protected final Dictionary<?, ?> getProperties() {
		return getComponentContext().getProperties();
	}

	@SuppressWarnings("unchecked")
	protected <T> T getPropertyValue(Class<T> memberType, String memberName) {

		Object value = getProperties().get(memberName);
		return (T) value;
	}

	@SuppressWarnings("unchecked")
	protected <T> List<T> getPropertyValues(Class<T> type, String memberName) {
		ArrayList<T> result = new ArrayList<T>();
		List<T> csvString = (List<T>) getProperties().get(memberName);
		if (csvString == null || csvString.isEmpty()) {
			logWarn("Could not find property with name '" + memberName + "'");
			return result;
		}
		return csvString;
	}

	protected <T, V extends T> T instanceOf(Class<T> type, Class<V> ofClass) {

		try {

			try {
				Constructor<V> constructor = ofClass
						.getConstructor(new Class<?>[] { Bundle.class,
								Configuration.class });
				return constructor.newInstance(getOwnerBundle(),
						configuration());
			} catch (NoSuchMethodException ns) {
			}
			try {
				Constructor<V> constructor = ofClass
						.getConstructor(new Class<?>[] { Bundle.class });
				return constructor.newInstance(getOwnerBundle());
			} catch (NoSuchMethodException ns) {
			}
			try {
				Constructor<V> constructor = ofClass
						.getConstructor(new Class<?>[] { Configuration.class });
				return constructor.newInstance(configuration());
			} catch (NoSuchMethodException ns) {
			}
			try {
				Constructor<V> constructor = ofClass
						.getConstructor(new Class<?>[] { ClassLoader.class });
				ClassLoader classloader = getOwnerBundle().adapt(
						BundleWiring.class).getClassLoader();
				return constructor.newInstance(classloader);
			} catch (NoSuchMethodException ns) {
			}
			return ofClass.newInstance();
		} catch (Exception e) {
			// annotationMonitor.elementCreationFailed(ofClass, e);
			throw new InstantiationFailed(ofClass, type, e);
		}
	}

	@SuppressWarnings("unchecked")
	protected <T> Class<T> loadClass(String className, Class<T> memberType) {
		try {
			return (Class<T>) getOwnerBundle().loadClass(className);
		} catch (Exception e) {
			logError("Error on loading class " + className, e);
		}
		throw new RuntimeException();
		// throw new AnnotationRequired(annotatedClass, annotationClass);
	}

	public final void logDebug(String msg) {
		if (getLogService() != null)
			getLogService().log(LogService.LOG_DEBUG, msg);
	}

	public final void logError(String msg, Throwable e) {
		if (getLogService() != null)
			getLogService().log(LogService.LOG_ERROR, msg, e);
	}

	public final void logInfo(String msg) {
		if (getLogService() != null)
			getLogService().log(LogService.LOG_INFO, msg);
	}

	public final void logWarn(String msg) {
		if (getLogService() != null)
			getLogService().log(LogService.LOG_WARNING, msg);
	}

	protected Bundle searchOwnerBundle(BundleContext bundleContext,
			String storyBundleName, String storyBundleVersion) {
		Bundle[] bundles = bundleContext.getBundles();
		for (Bundle bundle : bundles) {
			if (bundle.getSymbolicName().equals(storyBundleName)
					&& bundle.getVersion().toString()
							.equals(storyBundleVersion)) {
				return bundle;
			}
		}
		return null;
	}

	private void setComponentContext(ComponentContext componentContext) {
		this.componentContext = componentContext;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public void setOwnerBundle(Bundle ownerBundle) {
		this.ownerBundle = ownerBundle;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

	protected final void unbindLogService(LogService logService) {
		this.logService.compareAndSet(logService, null);
		logDebug("Unbinded LogService for " + getClass().getSimpleName() + ".");
	}

}
