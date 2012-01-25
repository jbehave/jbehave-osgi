#JBehave OGSi

JBehave OGSi was created to be able to execute JBehave stories inside an OSGi environment.

For now it can be used with a Eclipse Equinox container, including RCP applications.

It is composed by 2 basic components: org.jbehave.osgi.services, that wraps JBehave API and org.jbehave.osgi.equinox which contain Equinox commands.
 
##EQUINOX

### Building JBehave-OSGi for Equinox
* because we are using Wiring API that is part of OSGI R4.3, this module will be supported only by Indigo (3.7).

Equinox module is being built using Eclipse Tycho, that uses _MANIFEST-first_ approach. 
It is required that the JBehave OSGi Service project first that is built using _POM-first_ approach.
The mix of the two methods in same maven reactor is not allowed by Tycho, so you need to separate the build into to phases (steps 1 and 2):

1) Build the service project

	mvn install -Pservice

2) Build the equinox module: 

	mvn install -Pequinox

This will create a P2 repository (org.jbehave.osgi.equinox.repository/target/repository) which will contain the Core feature and all bundles dependencies needed for JBehave-OSGi to be executed on Equinox.


### Using JBehave-OSGi for Equinox

When developing OSGi or RCP projects in Eclipse PDE, it's recommended that you create one workspace for each set of related projects, setting up one Target Platform for it, selecting only the necessary set of bundles that it needs to run the application and its tests properly.

You can run the BDD tests in a Equinox environment using three different ways: 

1) using Tycho-Surefire plugin as part of a Maven/Tycho building;
2) using a PDE's JUnit Plug-in Test launcher;
3) using JBehave OSGi commands at the console of some running Equinox container;

And to be able to use it you must add org.jbehave.osgi.services bundle to the Target Platform of your project or install it in some running container.


#### Installing JBehave-OSGi in a Target Platform


#### Installing JBehave-OSGi in a running Equinox container


#### Creating a OSGi Behavior Test


 
  
We provide with JBehave-OSGi sources one example of how you could do this kind of setup using a Target Platform Definition File.  
In your Eclipse IDE, go to the org.jbehave.osgi.equinox.target project and open (double-click) the eclipse-jbehave-osgi-indigo.target file.    
Repair that this definition file is pointing to ${project_loc:/org.jbehave.osgi.equinox.repository}/target/repository/ exactly where our repository was created before.  
In your case, you could probably point it to some HTTP server where you have deployed the JBehave P2 repository.  
After open the file you just need to click on "Set as Target Platform" link and your workspace will be configured properly.


#### Running Stories using an Equinox Launcher:

1) Setup OSGi Framework Launcher
Once your workspace/target platform is set up you can setup the Equinox launcher, choosing the bundles and tests fragments that must be installed when the Equinox is launched.  
We provided one launcher sample named run_jbehave-osgi_equinox_runtime where we selected org.jbehave.osgi.sample.fragment.trader.bnd test fragment.

- Go to menu "Run", then "Debug Configurations". 
- Select run_jbehave-osgi_equinox_runtime launcher.
- Check if only the necessary projects is selected on Workspace. 
- Ensure that all dependencies needed is selected on Target Platform clicking on "Validate Bundles". 
- If no problem were detected click on "Debug" button. That will start your Equinox instance with debug support (check the Console View).

2) After launching Equinox you can test JBehave OSGi Services with this commands:

    osgi> help
    
You should see the JBehave help (with others too):

	--- JBehave Equinox Commands ---
	jbStatus - JBehave OSGi EmbedderService status.
	jbRunStoriesWithAnnotatedEmbedder - Run Stories via Annotated Embedder. 
	
Then you could try the service status:

    osgi> jbStatus
    
You should see:

     OSGi Embedder Service is started.      

3) Run the stories via the Embedder

    osgi> jbRunStoriesWithAnnotatedEmbedder org.jbehave.osgi.examples.trader.annotations.TraderAnnotatedEmbedder
    