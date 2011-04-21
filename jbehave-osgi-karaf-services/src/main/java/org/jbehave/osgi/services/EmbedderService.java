package org.jbehave.osgi.services;

import java.util.List;

public interface EmbedderService {

    void startUp();

    boolean isStarted();

    String getStatus();

    void runAsEmbeddables(List<String> classNames);

    void runStoriesWithAnnotatedEmbedderRunner(String runnerClass, List<String> classNames);

}
