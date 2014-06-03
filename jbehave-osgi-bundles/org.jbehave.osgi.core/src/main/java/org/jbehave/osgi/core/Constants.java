package org.jbehave.osgi.core;

public interface Constants {
	
	public static final String STORY_RUNNER_FACTORY_FPID = "org.jbehave.osgi.core.services.StoryRunnerServiceFactory";
	public static final String STEP_FACTORY_FPID = "org.jbehave.osgi.core.services.StepsFactory";

	public static final String COMPONENT_DESCRIPTION = "component.description";

    public static final String SKIP_EXTENDERS = "jbehave.osgi.skip.extenders"; 

	//public static final String EXTENDER_PROPERTY_GROUP = "extender.property.group";
	//public static final String EXTENDER_PROPERTY_ITEM = "extender.property.item";
	//public static final String EXTENDER_PROPERTY_ITEM_CLASSIFIER = "extender.property.item.classifier";
	public static final String EXTENDER_TARGET_SERVICE_FACTORY_PID = "extender.targetFactoryPid";
	
	public static final String EXTENDEE_BUNDLE = "extendee.bundle";
	public static final String EXTENDEE_BUNDLE_VERSION = "extendee.bundle.version";

	public static final String LOG_TO_CONSOLE = "service.logToConsole";
	public static final String LOG_TO_SERVICE = "service.logToService";
	
	public static final String DTO_PREFIX_EMBEDDER = "embedder";
	public static final String DTO_PREFIX_CONFIGURATION = "configuration";
	public static final String DTO_PREFIX_STORY_SEARCH = "story.search";
	
	
	public static final String STORY_RUNNER_EXTENDER_MANIFEST_HEADER = "JBehave-StoryRunner";
	//public static final String STORY_RUNNER_EXTENDER_PROPERTY_GROUP = "storyRunnerId";
	public static final String STORY_RUNNER_EXTENDER_PROPERTY_ITEM = "storyRunnerClass";
	public static final String STORY_RUNNER_EXTENDER_PROPERTY_ITEM_CLASSIFIER = "storyType";
	public static final String STORY_RUNNER_SERVICE_STEP_FACTORY_SERVICE_TARGET = "InjectableStepsFactory.target";

	public static final String STEP_FACTORY_EXTENDER_MANIFEST_HEADER = "JBehave-StepFactory";
	public static final String STEP_FACTORY_EXTENDER_PROPERTY_GROUP = "stepFactoryId";
	public static final String STEP_FACTORY_EXTENDER_PROPERTY_ITEM = "stepClass";
	
	public static String CHAR_COMMA = ",";
	public static String CHAR_COLLON = ":";
	public static String CHAR_SEMICOLLON = ";";
	public final static String JBEHAVE_OSGI_REPORT_OUTPUT_DIR_PROPERTY = "jbehave.osgi.outputdir";

}
