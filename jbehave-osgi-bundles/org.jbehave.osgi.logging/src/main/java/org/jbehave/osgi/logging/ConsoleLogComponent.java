package org.jbehave.osgi.logging;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.log.LogReaderService;

@Component(immediate = true, service = ConsoleLogComponent.class)
public class ConsoleLogComponent {

    List<LogReaderService> logReaderServices = new CopyOnWriteArrayList<>();

    ConsoleOutput consoleOutput = new ConsoleOutput();

    @Reference
    protected void bindLogReaderService(LogReaderService logReaderService) {
        logReaderServices.add(logReaderService);
        logReaderService.addLogListener(consoleOutput);
    }

    protected void unbindLogReaderService(LogReaderService logReaderService) {
        logReaderService.removeLogListener(consoleOutput);
        logReaderServices.remove(logReaderService);
    }

}
