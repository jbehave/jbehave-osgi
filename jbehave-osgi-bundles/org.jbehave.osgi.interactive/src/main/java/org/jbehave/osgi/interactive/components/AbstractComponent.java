package org.jbehave.osgi.interactive.components;

import java.util.Dictionary;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;

/**
 * 
 * @author Cristiano Gavião
 *
 */
public abstract class AbstractComponent {

	private ComponentContext componentContext;
	private AtomicReference<LogService> logService = new AtomicReference<LogService>();
	private boolean logToService;
	private boolean logToConsole;

	public AbstractComponent() {
	}

	protected boolean isLogToService() {
		return logToService;
	}

	protected void setLogToService(boolean logServiceOn) {
		this.logToService = logServiceOn;
	}

	protected boolean isLogToConsole() {
		return logToConsole;
	}

	protected void setLogToConsole(boolean logToConsole) {
		this.logToConsole = logToConsole;
	}

	protected void activate(ComponentContext context) {
		// save bundleContext reference...
		setComponentContext(context);
		logToService = (Boolean) getProperties().get("service.logToService");
		logToConsole = (Boolean) getProperties().get("service.logToConsole");
	}

	protected void modified(ComponentContext context) {
		// save bundleContext reference...
		setComponentContext(context);
		logToService = (Boolean) getProperties().get("service.logToService");
		logToConsole = (Boolean) getProperties().get("service.logToConsole");
	}

	protected void deactivate(ComponentContext context) {
		setComponentContext(null);
	}

	protected ComponentContext getComponentContext() {
		return componentContext;
	}

	private void setComponentContext(ComponentContext componentContext) {
		this.componentContext = componentContext;
	}

	protected final LogService getLogService() {
		return logService.get();
	}

	protected final Dictionary<?, ?> getProperties() {
		return getComponentContext().getProperties();
	}

	public final void logDebug(String msg) {
		if (getLogService() != null)
			getLogService().log(LogService.LOG_DEBUG, msg);
		if (logToConsole)
			System.out.println(msg);
	}

	public final void logError(String msg, Throwable e) {
		if (getLogService() != null)
			getLogService().log(LogService.LOG_ERROR, msg, e);
		if (logToConsole)
			System.out.println(msg);
	}

	public final void logInfo(String msg) {
		if (getLogService() != null)
			getLogService().log(LogService.LOG_INFO, msg);
		if (logToConsole)
			System.out.println(msg);
	}

	public final void logWarn(String msg) {
		if (getLogService() != null)
			getLogService().log(LogService.LOG_WARNING, msg);
		if (logToConsole)
			System.out.println(msg);
	}

	protected final void bindLogService(LogService logService) {
		this.logService.set(logService);
		logDebug("Binded LogService for the JBehave OSGi Component.");
	}

	protected final void unbindLogService(LogService logService) {
		this.logService.compareAndSet(logService, null);
		logDebug("Unbinded LogService for the JBehave OSGi Component.");
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
