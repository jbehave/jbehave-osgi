package org.jbehave.osgi.core.itests;

import static org.jbehave.osgi.core.Constants.STEP_FACTORY_EXTENDER_MANIFEST_HEADER;
import static org.jbehave.osgi.core.Constants.STORY_RUNNER_EXTENDER_MANIFEST_HEADER;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.streamBundle;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;
import static org.osgi.framework.Constants.BUNDLE_SYMBOLICNAME;
import static org.osgi.framework.Constants.BUNDLE_VERSION;
import static org.osgi.framework.Constants.EXPORT_PACKAGE;
import static org.osgi.framework.Constants.IMPORT_PACKAGE;
import static org.osgi.framework.Constants.REQUIRE_BUNDLE;

import org.jbehave.osgi.core.itests.lib.embedders.AnnotatedPathRunnerOsgiExample;
import org.jbehave.osgi.core.itests.lib.steps.NamedParametersSteps;
import org.jbehave.osgi.core.itests.lib.steps.SearchSteps;
import org.jbehave.osgi.paxexam.ProbeOptions;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.options.UrlProvisionOption;
import org.ops4j.pax.tinybundles.core.TinyBundles;

public class PaxExamForkedContainerConfiguration {

    public PaxExamForkedContainerConfiguration() {
    }

    private UrlProvisionOption buildEmbedderExtendeeBundle() {
        return streamBundle(TinyBundles
                .bundle()
                .add(AnnotatedPathRunnerOsgiExample.class)
                .set(EXPORT_PACKAGE,
                        "org.jbehave.osgi.core.itests.lib.embedders")
                .set(BUNDLE_SYMBOLICNAME,
                        "org.jbehave.osgi.core.itests.embedders")
                .set(BUNDLE_VERSION, "1.0.0")
                .set(STORY_RUNNER_EXTENDER_MANIFEST_HEADER,
                        "org.jbehave.osgi.core.itests.lib.embedders.AnnotatedPathRunnerOsgiExample")
                .set(IMPORT_PACKAGE,
                        "com.thoughtworks.paranamer,org.osgi.framework")
                .set(REQUIRE_BUNDLE,
                        "org.jbehave.osgi.core,org.hamcrest.integration;bundle-version=\"1.3.0\"")
                .build(TinyBundles.withClassicBuilder()));
    }

    private UrlProvisionOption buildInjectableStepsFactoryExtendeeBundle() {
        return streamBundle(TinyBundles
                .bundle()
                .add(SearchSteps.class)
                .add(NamedParametersSteps.class)
                .set(IMPORT_PACKAGE,
                        "com.thoughtworks.paranamer,org.osgi.framework")
                .set(EXPORT_PACKAGE, "org.jbehave.osgi.core.itests.lib.steps")
                .set(BUNDLE_SYMBOLICNAME, "org.jbehave.osgi.core.itests.steps")
                .set(BUNDLE_VERSION, "1.0.0")
                .set(STEP_FACTORY_EXTENDER_MANIFEST_HEADER,
                        "stepFactory1:org.jbehave.osgi.core.itests.lib.steps.*")
                .set(REQUIRE_BUNDLE,
                        "org.jbehave.osgi.core,org.hamcrest.integration;bundle-version=\"1.3.0\"")
                .build(TinyBundles.withClassicBuilder()));
    }

    @org.ops4j.pax.exam.Configuration
    public Option[] config() {

        return options(systemProperty("osgi.debug").value("true"),
                systemProperty("eclipse.consoleLog").value("true"),
                systemProperty("eclipse.log.level").value("ALL"),
                systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level")
                        .value("ALL"),
                ProbeOptions.consoleOSGiLogging().startLevel(1),
                ProbeOptions.jbehaveCoreAndDependencies(),
                buildEmbedderExtendeeBundle().startLevel(4),
                buildInjectableStepsFactoryExtendeeBundle().startLevel(4));
    }
}
