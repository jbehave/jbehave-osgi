#JBehave OGSi

JBehave OGSi works on two OSGi environments: Apache Karaf and Eclipse Equinox.

##KARAF

### Building

The Karaf module is built using 'one step' maven profile run:

1) Build the Karaf bundles: 

    $ mvn install -Pkaraf

That will install on your local maven repo the Core and Dependencies features needed for JBehave-OSGi to be executed on Karaf. 

### Installation
This procedure was tested in Karaf 2.2.1.

1) Start Karaf shell (in clean mode):
    
    $ <karaf bin dir>/karaf clean

Ensure the karaf installation dir has +wr priviledges enabled for all enclosed items.

2) Install the OSGi Services as Karaf Features:

    karaf> features:addurl mvn:org.jbehave.osgi/org.jbehave.osgi.karaf.features.core/1.0.0-SNAPSHOT/xml/karaf
    karaf> features:install jbehave-osgi-features-core  
        
3) After installing it you can test with the command:

    karaf> list -s

should show a list like this:

    START LEVEL 100 , List Threshold: 50
        ID   State         Blueprint      Level  Symbolic name
    [  42] [Active     ] [            ] [   60] com.springsource.org.apache.commons.collections (3.2.1)
    [  43] [Active     ] [            ] [   60] com.springsource.org.apache.commons.io (1.4.0)
    [  44] [Active     ] [            ] [   60] com.springsource.org.apache.commons.lang (2.5.0)
    [  45] [Installed  ] [            ] [   60] com.springsource.org.hamcrest (1.1.0)
    [  46] [Active     ] [            ] [   60] com.springsource.org.hamcrest.core (1.1.0)
    [  47] [Active     ] [            ] [   60] com.springsource.javax.inject (1.0.0)
    [  48] [Active     ] [Created     ] [   60] org.jbehave.osgi.jbehave-osgi-services (1.0.0.SNAPSHOT)
    [  49] [Active     ] [Created     ] [   60] org.jbehave.osgi.jbehave-osgi-karaf-commands (1.0.0.SNAPSHOT)

4) Verify the Status:

    karaf> jbehave:status   
    
You should see:
    
    OSGi Embedder Service is started.


### Running Stories

To use Jbehave-OSGi you need to create a bundle fragment containing Steps and AnnotatedEmbedder classes for your own project, associating it to org.jbehave.osgi.services (as Fragment-Host).  
We provided one project that you could use to test. It was created using maven-bundle-plugin to facilitate embed Trader jars.
 
1) Build the provided sample:
  
    $ mvn install -Psample-bnd 
  
2) Install the bundle fragment with Steps and AnnotatedEmbedder classes:
  
    karaf> osgi:install mvn:org.jbehave.osgi/org.jbehave.osgi.sample.fragment.trader.bnd/1.0.0-SNAPSHOT

3) Refresh the org.jbehave.osgi.jbehave-osgi-services using its ID:

    karaf> refresh 48 <-- ID that appears on ID column after the command List -->
    karaf> list -s

should show something like this:
    
    [  48] [Active     ] [Created     ] [   60] org.jbehave.osgi.jbehave-osgi-services (1.0.0.SNAPSHOT)
                                           Fragments: 50
    [  49] [Active     ] [Created     ] [   60] org.jbehave.osgi.jbehave-osgi-karaf-commands (1.0.0.SNAPSHOT)
    [  50] [Resolved   ] [            ] [   60] org.jbehave.osgi.jbehave-osgi-sample-fragment-trader-bnd (1.0.0.SNAPSHOT)
                                           Hosts: 48

4) Run the stories via Annotadded Embedder:

    karaf> jbehave:run-stories-with-annotated-embedder org.jbehave.examples.trader.annotations.TraderAnnotatedEmbedder

It is possible to setup the AnnotatedEmbedder classes to be run creating a properties file named org.jbehave.osgi.cfg that should be located on {$karaf}/etc directory.  
The configuration file should contain this properties:

    includes=org.jbehave.examples.trader.annotations.TraderAnnotatedEmbedder  
 
This way you could call the story runner without parameters:

    karaf> jbehave:run-stories-with-annotated-embedder
 
 
##EQUINOX

### Building JBehave-OSGi for Equinox
* because we are using Wiring API that is part of OSGI R4.3, this module will be supported only by Indigo (3.7).

Equinox module will be built using Eclipse Tycho, that uses _MANIFEST-first_ approach. But it requires the JBehave OSGi Service project that is built using _POM-first_ approach.
The mix of the two methods in same maven reactor is not allowed by Tycho, so you need to separate the build into to phases (steps 1 and 2):

1) Build the service project

	mvn install -Pservice

2) Build the equinox module: 

	mvn install -Pequinox

This will create a P2 repository (org.jbehave.osgi.equinox.repository/target/repository) which will contain the Core feature and all bundles dependencies needed for JBehave-OSGi to be executed on Equinox.


### Using JBehave-OSGi for Equinox
To be able to use JBehave-OSGi on Equinox projects you need to add it's bundles to the Target Platform of your own project.   
It is possible to execute Equinox executing OSGi Framework Launcher inside you eclipse IDE or through an external Equinox distribution. 

#### Installation with Eclipse Launcher:

When developing OSGi projects in Eclipse IDE, it's recommended that you create one workspace for each set of related project (we can say: with same parent POM).  
This way you could setup one Target Platform for it, selecting only the necessary set of bundles that it needs to run properly.
  
We provide with JBehave-OSGi sources one example of how you could do this kind of setup using a Target Platform Definition File.  
In your Eclipse IDE, go to the org.jbehave.osgi.equinox.target project and open (double-click) the eclipse-jbehave-osgi-indigo.target file.    
Repair that this definition file is pointing to ${project_loc:/org.jbehave.osgi.equinox.repository}/target/repository/ exactly where our repository was created before.  
In your case, you could probably point it to some HTTP server where you have deployed the JBehave P2 repository.  
After open the file you just need to click on "Set as Target Platform" link and your workspace will be configured properly.


#### Running Stories with Eclipse Launcher:

1) Setup OSGi Framework Launcher
Once your workspace is set up you can setup the Equinox launcher, choosing the bundles and tests fragments that must be installed when the Equinox is launched.  
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

    osgi> jbRunStoriesWithAnnotatedEmbedder org.jbehave.examples.trader.annotations.TraderAnnotatedEmbedder
    