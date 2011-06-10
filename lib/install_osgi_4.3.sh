#!/bin/bash
mvn install:install-file -Dfile=./osgi.core.jar -DgroupId=org.osgi -DartifactId=org.osgi.core -Dversion=4.3.0 -Dpackaging=jar