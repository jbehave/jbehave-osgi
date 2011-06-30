package org.jbehave.osgi.commands;

import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.jbehave.osgi.services.EmbedderService;

public abstract class EmbedderCommand extends OsgiCommandSupport {

    private EmbedderService embedderService;

    public EmbedderService getEmbedderService() {
        return embedderService;
    }

    public void setEmbedderService(EmbedderService embedderService) {
        this.embedderService = embedderService;
    }

}
