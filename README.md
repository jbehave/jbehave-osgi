#JBehave OGSi

JBehave OGSi works on two OSGi environments: Apache Karaf and Eclipse Equinox.

##KARAF

To use with Karaf do the following:

1) Build the Karaf bundles: 

    mvn install -Pkaraf

2) Start Karaf shell (in clean mode):
    
    <Karaf bin dir>/karaf clean

Ensure the karaf installation dir has +wr priviledges enabled for all enclosed items.

3) Install the OSGi Services as Karaf Features:

    karaf> features:addurl mvn:org.jbehave.osgi/jbehave-osgi-karaf-features/1.0.0-SNAPSHOT/xml/karaf
    karaf> features:install jbehave-osgi  
        
4) After installing it you can test with the command:

    karaf> list -s

should show a list like this:

    START LEVEL 100 , List Threshold: 50
       ID   State         Blueprint      Level  Symbolic name
    [  42] [Active     ] [            ] [   60] org.apache.commons.collections (3.2.1)
    [  43] [Active     ] [            ] [   60] com.springsource.org.apache.tools.ant (1.7.1)
    [  44] [Active     ] [            ] [   60] org.apache.commons.io (1.4)
    [  45] [Active     ] [            ] [   60] org.apache.commons.lang (2.5)
    [  46] [Active     ] [            ] [   60] wrap_mvn_org.codehaus.plexus_plexus-utils_2.0.5 (0)
    [  47] [Active     ] [            ] [   60] wrap_mvn_com.thoughtworks.paranamer_paranamer_2.3 (0)
    [  48] [Active     ] [            ] [   60] wrap_mvn_org.freemarker_freemarker_2.3.16 (0)
    [  49] [Active     ] [            ] [   60] wrap_mvn_org.hamcrest_hamcrest-all_1.1 (0)
    [  50] [Active     ] [            ] [   60] wrap_mvn_junit_junit-dep_4.8.2 (0)
    [  51] [Active     ] [            ] [   60] wrap_mvn_xpp3_xpp3_min_1.1.4c (0)
    [  52] [Active     ] [            ] [   60] wrap_mvn_com.thoughtworks.xstream_xstream_1.3.1 (0)
    [  53] [Active     ] [            ] [   60] wrap_mvn_org.jbehave_jbehave-core_3.4-SNAPSHOT (0)
    [  54] [Active     ] [            ] [   60] wrap_mvn_org.jbehave_jbehave-ant_3.4-SNAPSHOT (0)
    [  55] [Active     ] [Created     ] [   60] org.jbehave.osgi.jbehave-osgi-services (1.0.0.SNAPSHOT)
    [  56] [Active     ] [Created     ] [   60] org.jbehave.osgi.jbehave-osgi-karaf-commands (1.0.0.SNAPSHOT)

5) Install the bundle fragment with Steps and AnnotatedEmbedder classes:

    karaf> osgi:install mvn:org.jbehave.osgi/jbehave-osgi-trader-fragment-mvn/1.0.0-SNAPSHOT

6) Refresh the org.jbehave.osgi.jbehave-osgi-services using its ID:

    karaf> Refresh 55 <-- this number is the ID that appears on ID column after the command List -->
    karaf> list -s

should show something like this:
    ...
    [  55] [Active     ] [Created     ] [   60] org.jbehave.osgi.jbehave-osgi-services (1.0.0.SNAPSHOT)
                                           Fragments: 57
    [  56] [Active     ] [Created     ] [   60] org.jbehave.osgi.jbehave-osgi-karaf-commands (1.0.0.SNAPSHOT)
    [  57] [Resolved   ] [            ] [   60] org.jbehave.osgi.jbehave-osgi-trader-fragment-mvn (1.0.0.SNAPSHOT)
                                           Hosts: 55

6) Verify the Status:

    karaf> jbehave:status

7) Run the stories via the Annotadded Embedder:

    karaf> jbehave:runannotaddedembedder org.jbehave.examples.trader.annotations.TraderAnnotatedEmbedder

* it is possible to setup the AnnotatedEmbedder classes to be run creating a properties file named org.jbehave.osgi.cfg that
should be located on {$karaf}/etc directory.

The configuration file should contain this properties:

    includes=org.jbehave.examples.trader.annotations.TraderAnnotatedEmbedder
    excludes= 
 
This way you could call the story runner without parameters:
    karaf> jbehave:runannotaddedembedder

 
##EQUINOX

To use with equinox do the following:

1) Build the module: 

    mvn install -Pequinox

2) Start Equinox shell


3) Install the OSGi Services as Equinox Features

        
4) After installing it you can test with the command:


5) Install bundle(s) with Steps and Embedable


6) Run the embeddables via the Embedder

