package org.jbehave.osgi.core.components;

import java.util.Dictionary;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.osgi.framework.Bundle;
import org.osgi.service.component.ComponentConstants;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;

/**
 * 
 * @author Cristiano Gavião
 *
 */
public abstract class AbstractComponent {

    private volatile ComponentContext componentContext = null;

    private AtomicReference<LogService> logService = new AtomicReference<LogService>();

    private String componentName;

    public AbstractComponent() {
    }

    protected void activate(ComponentContext context) {
        // save bundleContext reference...
        setComponentContext(context);

        componentName = (String) getProperties().get(
                ComponentConstants.COMPONENT_NAME);

        logInfo("Starting " + componentName + ".");

    }

    protected void bindLogService(LogService logService) {
        this.logService.set(logService);
        logDebug("Binded LogService for " + getClass().getSimpleName() + ".");
    }

    protected void deactivate(ComponentContext context) {
        setComponentContext(null);
    }

    protected ComponentContext getComponentContext() {
        return componentContext;
    }

    protected Bundle getBundle() {
        return getComponentContext().getBundleContext().getBundle();
    }

    protected String getComponentName() {
        return componentName;
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

    private void setComponentContext(ComponentContext componentContext) {
        this.componentContext = componentContext;
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
