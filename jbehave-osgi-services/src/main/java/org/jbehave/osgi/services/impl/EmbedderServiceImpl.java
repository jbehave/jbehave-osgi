package org.jbehave.osgi.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.osgi.services.EmbedderService;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Default implementation of the OSGi Embedder Service
 * {@link EmbedderService}
 * </p>
 * 
 * @author Cristiano Gavião
 */
public class EmbedderServiceImpl implements EmbedderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmbedderServiceImpl.class);

    private BundleContext bundleContext;
    private Embedder embedder;
    private Boolean status = false;
    private List<String> excludeNames = new ArrayList<String>();
    private List<String> includeNames = new ArrayList<String>();
    private List<String> classNames = null;

    @Override
    public List<String> findClassNames() {
        if (classNames == null) {
            classNames = new ArrayList<String>();
            LOGGER.debug("Searching for Embedder class names, including " + includeNames + " and excluding "
                    + excludeNames + ".");
            // TODO List<String> classNames =
            // newStoryFinder().findClassNames(searchDirectory(),
            // getIncludeList(),
            // getExcludeList());
            classNames.addAll(includeNames);
            LOGGER.info("Found Embedder classes: " + classNames);
        }
        System.out.println("Found class names : '"+classNames+"'" + " size "+classNames.size());
        return classNames;
    }

    public Embedder getEmbedder() {
        return embedder;
    }

    @Override
    public boolean isStarted() {
        return status;
    }

    @Override
    public void runStoriesWithAnnotatedEmbedderRunner() {
        runStoriesWithAnnotatedEmbedderRunner(findClassNames());
    }

    @Override
    public void runStoriesWithAnnotatedEmbedderRunner(List<String> classNames) {
        LOGGER.info("Running stories with annotated embedder runner using classes: '" + classNames +"'");
        embedder.runStoriesWithAnnotatedEmbedderRunner(classNames);
    }

    public void setEmbedder(Embedder embedder) {
        this.embedder = embedder;
        LOGGER.debug("Injected Embedder " + embedder);
    }

    public void setExcludes(String excludesCSV) {
        excludeNames.addAll(fromCSV(excludesCSV));
        LOGGER.info("Updated exclude names " + excludeNames);
    }

    public void setIncludes(String includesCSV) {
        includeNames.addAll(fromCSV(includesCSV));
        LOGGER.info("Updated include names " + includeNames);
    }

    private List<String> fromCSV(String csv) {
        List<String> list = new ArrayList<String>();
        if ( csv != null ){
            for (String string : csv.split(",")) {
                if ( StringUtils.isNotEmpty(string) ){
                    list.add(string);
                }                
            }
        }
        return list;
    }

    @Override
    public void showStatus() {
        StringBuilder message = new StringBuilder("OSGi Embedder Service is" + (isStarted() ? " " : " not ")
                + "started");
        String eol = System.getProperty("line.separator");
        message.append(eol + "Include names (" + includeNames.size() + "):" + includeNames);
        message.append(eol + "Exclude names (" + excludeNames.size() + "):" + excludeNames);
        System.out.println(message.toString());
        LOGGER.info(message.toString());
    }

    public void start() {
        LOGGER.info("Starting OSGi Embedder Service");
        status = true;
    }

    public void stop() {
        LOGGER.info("Stopping OSGi Embedder Service");
        status = false;
        embedder = null;
    }

    public BundleContext getBundleContext() {
        return bundleContext;
    }

    public void setBundleContext(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
