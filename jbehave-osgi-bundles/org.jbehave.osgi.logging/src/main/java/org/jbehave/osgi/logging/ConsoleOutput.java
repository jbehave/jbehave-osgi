package org.jbehave.osgi.logging;

import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;

public class ConsoleOutput implements LogListener {

    @Override
    public void logged(LogEntry logEntry) {
        if (logEntry.getMessage() != null)
            System.out.println("[" + logEntry.getBundle().getSymbolicName()
                    + "] " + logEntry.getMessage());
    }

}
