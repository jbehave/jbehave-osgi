#JBehave OGSi

JBehave OGSi was created to be able to execute JBehave stories inside an OSGi environment.

For now it can be used together with Tycho to test bundles deployed in an Eclipse Equinox container, RCP products using SWTBot and Web Applications (as Vaadin) deployed as a bundle.

It is composed by 3 basic components: 

- org.jbehave.osgi.services, that wraps the JBehave Core API and exposes its functionalities a service to the OSGi Container;
- org.jbehave.osgi.web, that wraps the wraps the JBehave Selenium API and let it be used with the core service;
- org.jbehave.osgi.equinox, which supply Equinox specifics features, a console commands and a P2 repository.
 
##JBehave for Equinox

### Building JBehave-OSGi for Equinox

* because we are using Wiring API that is part of OSGI R4.3, this module will be supported only by Indigo (3.7).

The JBehave-OSGi components uses different approach to being built.
 
Equinox components are built using Eclipse Tycho, which uses a _MANIFEST-first_ approach. This mean that all information needed in the build is taken from the bundle manifest.

While JBehave-OSGi Service ans Web are built using Maven Bundle Plugin, which uses _POM-first_ approach. This mean that all information needed in the build is taken from the bundle manifest.
Its not possible to run both methods in same maven reactor (the same maven running job), so you must to split the build into two phases.


First build the _POM-first_ projects:
 
1) Go to the root folder where you have cloned the project:

	cd /myprojects/jbehave-osgi

2) Start the maven profile named **services**

	mvn clean install -P services
	
This will install the JBehave-OSGi Service and Web components in the local maven repository.
	
	
Then build the _MANIFEST-first_ projects:

1) Go to root folder where you have cloned the project

	cd /myprojects/jbehave-osgi

2) Start the maven profile *equinox*: 

	mvn clean verify -P equinox

This will create a P2 repository in the following folder: 

     /myprojects/jbehave-osgi/org.jbehave.osgi.equinox.repository/target/repository 
     
This P2 repository will contain all components and all bundles dependencies needed for JBehave-OSGi to be executed on Equinox.


    
### Running the examples    
   
#### RCP Product Example

1) Go to root folder where you have cloned the project:

	cd /myprojects/jbehave-osgi
	
2) Start the maven build:

    mvn clean verify -P example-rcpmail
 
Now the build will start and the JBehave stories will be executed in the building.    
If everything goes right, you could see the generated RCP product in this folder:

	/myprojects/jbehave-osgi/jbehave-osgi-examples/rcpmail/org.jbehave.osgi.examples.rcpmail.product/target/products    


#### Trader Equinox Server Example

This example reuses code from Trader example, so you must to ensure that this project was properly built before.

1) Go to root folder where you have cloned the project:

	cd /myprojects/jbehave-osgi

2) You must to install the _POM-First_ projects:

    mvn clean install -P examples-pom-first

2) Then you need build the _MANIFEST-First_ projects:

    mvn clean verify -P example-manifest-first

    
### Using JBehave-OSGi for Equinox

When developing OSGi or RCP projects in Eclipse PDE, it's recommended that you create one workspace for each set of related projects, setting up one Target Platform for it, selecting only the necessary set of bundles that it needs to run the application and its tests properly.

You can run the BDD tests in a Equinox environment using three different ways: 

1) using Tycho-Surefire plugin as part of a Maven/Tycho building;
2) using a PDE's JUnit Plug-in Test launcher;
3) using JBehave OSGi commands at the console of some running Equinox container;

And to be able to use it you must add org.jbehave.osgi.services bundle to the Target Platform of your project or install it in some running container.


#### Setup JBehave-OSGi in a Target Platform


#### Installing JBehave-OSGi in a running Equinox container

 
 

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
     