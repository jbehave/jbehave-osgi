Server check-up story

Narrative:
In order to ensure the server will handle user requisitions properly
As a technical administrator 
I want to do a check-up after server startup

Scenario: Needed bundles and services are ready after start server

Given equinox server is running

Then all needed bundles are active: 
|bundleName|
|org.eclipse.osgi|
|org.eclipse.equinox.cm|
|org.eclipse.equinox.log|
|org.eclipse.equinox.ds|
|org.eclipse.equinox.common|
|org.eclipse.equinox.simpleconfigurator|
|com.c4biz.osgiutils.vaadin6.shiro|
|com.c4biz.osgiutils.logging.reader|
|org.apache.shiro.core|
|org.apache.shiro.web|
|org.apache.shiro.quartz|
|com.c4biz.osgiutils.configuration.manager|
 
And all needed services are registered: 
|serviceName|
|org.osgi.service.cm.ConfigurationAdmin|
|org.osgi.service.log.LogService|
|com.c4biz.osgiutils.configuration.manager.service.IConfigurationService|
