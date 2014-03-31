package org.jbehave.osgi.examples.trader.pomfirst.paxexam;

import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;

import org.jbehave.osgi.paxexam.ProbeOptions;
import org.jbehave.osgi.paxexam.junit.AbstractPaxExamForStoryRunner;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;

public class AbstractExampleTestConfiguration extends
        AbstractPaxExamForStoryRunner {

    @Configuration
    public Option[] config() {

        return options(
                // vmOption("-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"),
                // systemTimeout(0),
                ProbeOptions.consoleOSGiLogging().startLevel(1),
                ProbeOptions.jbehaveCoreAndDependencies(),
                systemProperty("eclipse.consoleLog").value("true"),
                systemProperty("eclipse.log.level").value("DEBUG"),
                systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level")
                        .value("ALL"),
                mavenBundle("org.jbehave.osgi.examples",
                        "org.jbehave.osgi.examples.trader.pomfirst.application")
                        .versionAsInProject().start(true),
                mavenBundle("org.jbehave.osgi.examples",
                        "org.jbehave.osgi.examples.trader.pomfirst.itests")
                        .versionAsInProject().start(true));
        // url("reference:file:"
        // + PathUtils.getBaseDir()
        // +
        // "/../org.jbehave.osgi.examples.trader.pomfirst.application/target/classes"),
        // url("reference:file:"
        // + PathUtils.getBaseDir()
        // +
        // "/../org.jbehave.osgi.examples.trader.pomfirst.itests/target/classes"));
    }
}
