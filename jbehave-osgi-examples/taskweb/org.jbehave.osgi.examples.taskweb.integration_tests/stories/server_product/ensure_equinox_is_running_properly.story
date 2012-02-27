Server check-up story

Narrative:
In order to ensure the server will handle user requisitions properly
As a technical administrator 
I want to do a check-up after server startup

Scenario: Needed bundles and services are ready after start server

Given equinox server is running
Then all needed bundles are installed: 
|bundleName|startLevel|autoStart|
|ch.qos.logback.classic*1.0.0.v20111214-2030|default|default|
|ch.qos.logback.core*1.0.0.v20111214-2030|default|default|
|ch.qos.logback.slf4j*1.0.0.v20120123-1500|default|false|
|com.c4biz.osgiutils.configuration.manager|2|true|
|com.c4biz.osgiutils.jdk.deps|default|false|
|-- com.c4biz.osgiutils.logging.reader|1|true --|
|com.c4biz.osgiutils.vaadin.equinox.shiro|4|true|
|com.vaadin|default|default|
|javax.el|default|default|
|javax.servlet*3.0.0.v201112011016|default|default|
|javax.servlet.jsp*2.2.0.v201112011158|default|default|
|org.apache.shiro.core|3|true|
|org.apache.shiro.quartz|3|true|
|org.apache.shiro.web|3|true|
|org.eclipse.core.contenttype|default|default|
|org.eclipse.core.jobs|default|default|
|org.eclipse.core.runtime|default|true|
|org.eclipse.equinox.app|default|default|
|org.eclipse.equinox.cm|1|true|
|org.eclipse.equinox.common|1|true|
|-- org.eclipse.equinox.device|default|default --|
|org.eclipse.equinox.ds|1|true|
|org.eclipse.equinox.event|default|default|
|org.eclipse.equinox.http.jetty*3.0.0.c4biz|default|default|
|org.eclipse.equinox.http.registry|default|default|
|org.eclipse.equinox.http.servlet|default|default|
|-- org.eclipse.equinox.launcher.cocoa.macosx.x86_64|default|false --|
|org.eclipse.equinox.launcher|default|default|
|org.eclipse.equinox.log|1|true|
|org.eclipse.equinox.preferences|default|default|
|org.eclipse.equinox.registry|default|default|
|-- org.eclipse.equinox.security.macosx|default|false --|
|-- org.eclipse.equinox.security|default|default --|
|org.eclipse.equinox.simpleconfigurator|1|true|
|org.eclipse.equinox.util|default|default|
|org.eclipse.jetty.continuation|default|default|
|org.eclipse.jetty.http|default|default|
|org.eclipse.jetty.io|default|default|
|org.eclipse.jetty.security|default|default|
|org.eclipse.jetty.server|default|default|
|org.eclipse.jetty.servlet|default|default|
|org.eclipse.jetty.util|default|default|
|org.eclipse.osgi.services|default|default|
|org.eclipse.osgi.util|default|default|
|org.eclipse.osgi|-1|true|
|org.quartz|default|default|
|org.slf4j.api*1.6.4.v20111214-2030|default|default|
|org.jbehave.osgi.examples.taskweb.configuration|default|false|
|org.jbehave.osgi.examples.taskweb.server|default|default|
|org.jbehave.osgi.examples.taskweb.shiroini|default|false|
|org.jbehave.osgi.examples.taskweb.theme|default|false|
|org.jbehave.osgi.examples.taskweb.vaadin|default|default|
 
And all needed services are registered: 
|serviceName|
|org.osgi.service.cm.ConfigurationAdmin|
|org.osgi.service.log.LogService|
|com.c4biz.osgiutils.configuration.manager.service.IConfigurationService|
