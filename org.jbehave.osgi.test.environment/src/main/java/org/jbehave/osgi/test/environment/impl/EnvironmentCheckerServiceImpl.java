package org.jbehave.osgi.test.environment.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

import org.jbehave.osgi.test.environment.EnvironmentCheckerService;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.wiring.BundleWiring;

/**
 * <p>
 * Default implementation of the OSGi Environment Checker Service
 * {@link EnvironmentCheckerService}
 * </p>
 * 
 * @author Cristiano Gavião
 */
public class EnvironmentCheckerServiceImpl implements EnvironmentCheckerService {

	private BundleContext bundleContext;
	private Bundle bundle;
	private Boolean status = false;
	private String resource1 = "behavior1.story";
	private String resource2 = "behavior2.story";
	private String resource3 = "behavior3.story";
	private String javaPackage = "org.jbehave.osgi.test.environment.stories";
	private String dirPackage = "/org/jbehave/osgi/test/environment/stories/";
	private String resourceDir = "stories/";
	private String resourceModDir1 = "stories/module1";
	private PrintStream intp = System.out;

	public void start() {
		intp.println("Starting OSGi Environment Checker Service...\n");
		status = true;

		// check for Osgi Persistent Storage Services Support...
		checkSupportForOsgiPersistentStorage();

		checkSupportForExternalURLAccess();

		checkSupportForGetEntryPaths();

		checkSupportForFindEntries();

		checkSupportForGetEntry();

		checkSupportForGetEntryPaths();

		checkSupportForGetResourceAsStream();

		checkSupportForSearchClasspathWithWiringApi();
	}

	private void checkSupportForSearchClasspathWithWiringApi() {
		intp.println("\n----- Check support for Search Classpath With Wiring Api:  \n");
		BundleWiring wiring = bundle.adapt(BundleWiring.class);

		intp.println("* Search for *.story on all bundle classpath");

		// use classloader and don't look on fragments
		Collection<String> foundBundleStories = wiring.listResources("/",
				"*.story", BundleWiring.LISTRESOURCES_RECURSE);

		for (Iterator<String> iterator = foundBundleStories.iterator(); iterator
				.hasNext();) {
			String story = (String) iterator.next();
			intp.println("Story path:" + story);
		}

		intp.println("* Search for *.class on all bundle classpath");
		Collection<String> foundBundleClasses = wiring.listResources("/",
				"*.class", BundleWiring.LISTRESOURCES_RECURSE);

		for (Iterator<String> iterator = foundBundleClasses.iterator(); iterator
				.hasNext();) {
			String classname = (String) iterator.next();
			intp.println("Class path:" + classname);
		}

		intp.println("* Search for *.story on all bundle and fragments classpath");
		// don't use classloader and look on fragments
		Collection<URL> foundFragmentStories = wiring.findEntries("/",
				"*.story", BundleWiring.FINDENTRIES_RECURSE);
		for (Iterator<URL> iterator = foundFragmentStories.iterator(); iterator
				.hasNext();) {
			URL urlStory = (URL) iterator.next();
			intp.println("Story url:" + urlStory);
		}

		intp.println("* Search for *.class on all bundle and fragments classpath");
		// don't use classloader and look on fragments
		Collection<URL> foundFragmentClasses = wiring.findEntries("/",
				"*.class", BundleWiring.FINDENTRIES_RECURSE);
		for (Iterator<URL> iterator = foundFragmentClasses.iterator(); iterator
				.hasNext();) {
			URL urlClasses = (URL) iterator.next();
			intp.println("Class url:" + urlClasses);
		}
	}

	/**
     * 
     */
	private void checkSupportForGetResourceAsStream() {
		intp.println("\n----- Check support for GetResourceAsStream:  \n");

		intp.println("* Check entry on resource dir: " + "'" + resourceModDir1
				+ resource1 + "'");
		InputStream resolvedEntry = this.getClass().getClassLoader()
				.getResourceAsStream(resourceModDir1 + resource1);
		if (resolvedEntry != null) {
			System.out.println("resolved ->" + resourceModDir1 + resource1);
		} else {
			intp.println("no entries was found in '" + resourceModDir1
					+ resource1 + "'.");
		}
		intp.println("* Check entry on dir package: " + "'" + dirPackage
				+ resource3 + "'");
		InputStream resolvedEntry2 = this.getClass().getClassLoader()
				.getResourceAsStream(dirPackage + resource3);
		if (resolvedEntry2 != null) {
			System.out.println("resolved ->" + resolvedEntry2);
		} else {
			intp.println("no entries was found in '" + dirPackage + resource3
					+ "'.");
		}

	}

	private void checkSupportForGetEntry() {
		intp.println("\n----- Check support for GetEntry:  \n");

		intp.println("* Check entry on resource dir: " + "'" + resourceModDir1
				+ resource1 + "'");
		URL resolvedEntry = bundle.getEntry(resourceModDir1 + resource1);
		if (resolvedEntry != null) {
			System.out.println("resolved ->" + resolvedEntry);
		} else {
			intp.println("no entries was found in '" + resourceModDir1
					+ resource1 + "'.");
		}
		intp.println("* Check entry on dir package: " + "'" + dirPackage
				+ resource3 + "'");
		URL resolvedEntry2 = bundle.getEntry(dirPackage + resource3);
		if (resolvedEntry2 != null) {
			System.out.println("resolved ->" + resolvedEntry2);
		} else {
			intp.println("no entries was found in '" + dirPackage + resource3
					+ "'.");
		}
		intp.println("* Check entry on java package: " + "'" + javaPackage
				+ resource3 + "'");
		URL resolvedEntry3 = bundle.getEntry(javaPackage + resource3);
		if (resolvedEntry3 != null) {
			System.out.println("resolved ->" + resolvedEntry3);
		} else {
			intp.println("no entries was found in '" + javaPackage + resource3
					+ "'.");
		}
	}

	private void checkSupportForFindEntries() {
		intp.println("\n----- Check support for FindEntries:  \n");

		intp.println("* Search for *.story resources on java package: " + "'"
				+ javaPackage + "'");
		Enumeration<URL> resolvedJavaPackages = bundle.findEntries(javaPackage,
				"*.story", true);
		if (resolvedJavaPackages != null) {
			for (Enumeration<URL> e = resolvedJavaPackages; e.hasMoreElements();)
				System.out.println("->" + e.nextElement());
		} else {
			intp.println("no entries was found in package '" + javaPackage
					+ "'.");
		}

		intp.println("\n* Search for *.story resources on directory '"
				+ resourceDir + "'");
		Enumeration<URL> resolvedDirs = bundle.findEntries(resourceDir,
				"*.story", true);

		intp.println("resolved entries: ");
		if (resolvedDirs != null) {
			for (Enumeration<URL> e = resolvedDirs; e.hasMoreElements();)
				System.out.println("->" + e.nextElement());

		} else {
			intp.println("no entries was found in dir '" + resourceDir + "'.");
		}

		intp.println("\n* Search for *.story resources on dir package '"
				+ dirPackage + "'");
		Enumeration<URL> resolvedDirPackages = bundle.findEntries(dirPackage,
				"*.story", true);

		intp.println("resolved entries: ");
		if (resolvedDirPackages != null) {
			for (Enumeration<URL> e = resolvedDirPackages; e.hasMoreElements();)
				System.out.println("->" + e.nextElement());

		} else {
			intp.println("no entry paths was found in dir '" + dirPackage
					+ "'.");
		}

	}

	private void checkSupportForGetEntryPaths() {
		intp.println("\n----- Check support for GetEntryPaths: \n");

		intp.println("* Check resources on directory '" + resourceDir + "'");
		Enumeration<String> resolvedDirs = bundle.getEntryPaths(resourceDir);

		intp.println("resolved paths: ");
		if (resolvedDirs != null) {
			for (Enumeration<String> e = resolvedDirs; e.hasMoreElements();)
				System.out.println("->" + e.nextElement());

		} else {
			intp.println("no entry paths was found in dir '" + resourceDir
					+ "'.");
		}

		intp.println("\n* Check resources on package '" + javaPackage + "'");
		Enumeration<String> resolvedPackages = bundle
				.getEntryPaths(javaPackage);

		intp.println("resolved paths: ");
		if (resolvedPackages != null) {
			for (Enumeration<String> e = resolvedPackages; e.hasMoreElements();)
				System.out.println("->" + e.nextElement());

		} else {
			intp.println("no entry paths was found in dir '" + javaPackage
					+ "'.");
		}

		intp.println("\n* Check resources on dir package '" + dirPackage + "'");
		Enumeration<String> resolvedDirPackages = bundle
				.getEntryPaths(dirPackage);

		intp.println("resolved paths: ");
		if (resolvedDirPackages != null) {
			for (Enumeration<String> e = resolvedDirPackages; e
					.hasMoreElements();)
				System.out.println("->" + e.nextElement());

		} else {
			intp.println("no entry paths was found in dir '" + dirPackage
					+ "'.");
		}

	}

	private void checkSupportForExternalURLAccess() {
		intp.println("----- Check support for external URL resources: ");

		BufferedReader in;
		try {
			URL jbsite = new URL("http://www.jbehave.org/");
			in = new BufferedReader(new InputStreamReader(jbsite.openStream()));
			String inputLine;
			int caracters = 0;

			while ((inputLine = in.readLine()) != null)
				caracters += inputLine.length();
			in.close();
			intp.println("* Was readed " + caracters + " caracters from "
					+ jbsite + ".");

		} catch (IOException e) {
			intp.println("URL couldn't be openned...");
		}

	}

	@SuppressWarnings("unused")
	private void checkSupportForOsgiPersistentStorage() {
		String persistentDirectory0 = "";
		String persistentDirectory1 = "/configuration/";
		String persistentDirectory2 = "configuration/";
		String persistentDirectory3 = "configuration/test/";

		intp.println("----- Check support for GetData to create and read file at OSGi Persistent Storage: ");
		File base0 = bundleContext.getDataFile(persistentDirectory0);
		intp.println("Base Directory0: " + "'" + persistentDirectory0 + "' -> "
				+ "'" + base0.getPath() + "'");
		intp.println("Base Directory0, exist?: " + "' -> " + base0.exists());
		intp.println("Base Directory0, can write?: " + "' -> "
				+ base0.canWrite());
		intp.println("Base Directory0, can read?: " + "' -> " + base0.canRead());
		intp.println("Base Directory0, can execute?: " + "' -> "
				+ base0.canExecute());

		File base1 = bundleContext.getDataFile(persistentDirectory0);
		intp.println("Base Directory1: " + "'" + persistentDirectory1 + "' -> "
				+ "'"
				+ bundleContext.getDataFile(persistentDirectory1).getPath()
				+ "'");
		intp.println("Base Directory1, exist?: " + "' -> " + base1.exists());
		intp.println("Base Directory1, can write?: " + "' -> "
				+ base1.canWrite());
		intp.println("Base Directory1, can read?: " + "' -> " + base1.canRead());
		intp.println("Base Directory1, can execute?: " + "' -> "
				+ base1.canExecute());

		intp.println("Base Directory2: " + "'" + persistentDirectory2 + "' -> "
				+ "'"
				+ bundleContext.getDataFile(persistentDirectory2).getPath()
				+ "'");
		intp.println("Base Directory3: " + "'" + persistentDirectory3 + "' -> "
				+ "'"
				+ bundleContext.getDataFile(persistentDirectory3).getPath()
				+ "'");

		intp.println("\n* Creating File in OSGi Persistent Storage: ");
		if (!base1.exists()) {
			try {
				base1.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			intp.println("  Created dir: " + base1.getPath());
		}

		File configFile = new File(base1, resource2);
		if (!configFile.exists()) {
			try {
				configFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			intp.println("  Created File: " + configFile.getPath());
		}
		FileOutputStream os;
		try {
			os = new FileOutputStream(configFile);
			os.write("This is just an example of config file created by getDataFile().\n"
					.getBytes());
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		intp.println("* Reading File from OSGi Persistent Storage: ");

		FileInputStream is;
		try {
			is = new FileInputStream(configFile);
			intp.println("  Readed File: " + configFile.getPath() + ":");
			byte[] buff = new byte[512];
			int read = 0;
			while ((read = is.read(buff, 0, buff.length)) > 0) {
				intp.println(new String(buff, 0, buff.length));
			}
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void stop() {
		intp.println("Stopping OSGi Environment Service");
		status = false;
	}

	public BundleContext getBundleContext() {
		return bundleContext;
	}

	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
		this.bundle = bundleContext.getBundle();
	}

	@Override
	public boolean isStarted() {
		return status;
	}
}
