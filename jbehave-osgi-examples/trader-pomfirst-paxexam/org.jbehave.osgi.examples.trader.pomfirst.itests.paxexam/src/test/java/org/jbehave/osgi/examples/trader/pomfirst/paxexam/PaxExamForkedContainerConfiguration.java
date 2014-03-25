package org.jbehave.osgi.examples.trader.pomfirst.paxexam;

import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.vmOption;
import static org.ops4j.pax.exam.CoreOptions.systemTimeout;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;
import static org.ops4j.pax.exam.CoreOptions.url;

import org.jbehave.osgi.paxexam.junit.ProbeOptions;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.util.PathUtils;

public class PaxExamForkedContainerConfiguration {

    @org.ops4j.pax.exam.Configuration
    public Option[] config() {

        return options(
                vmOption("-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"),
                systemTimeout(0),
                ProbeOptions.jbehaveCoreAndDependencies(),
                ProbeOptions.simpleOSGiLogging(),
                systemProperty("eclipse.consoleLog").value("true"),
                systemProperty("eclipse.log.level").value("DEBUG"),
                systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level")
                        .value("ALL"),
                url("reference:file:"
                        + PathUtils.getBaseDir()
                        + "/../org.jbehave.osgi.examples.trader.pomfirst.application/target/classes"),
                url("reference:file:"
                        + PathUtils.getBaseDir()
                        + "/../org.jbehave.osgi.examples.trader.pomfirst.itests/target/classes"));
    }

}
