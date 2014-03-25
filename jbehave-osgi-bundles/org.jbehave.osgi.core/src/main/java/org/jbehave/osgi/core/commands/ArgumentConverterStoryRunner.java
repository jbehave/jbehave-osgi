package org.jbehave.osgi.core.commands;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.felix.service.command.Converter;
import org.jbehave.osgi.core.components.AbstractComponent;
import org.jbehave.osgi.core.services.StoryRunnerService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.log.LogService;

/**
 * 
 * @author Cristiano Gavião
 */
@Component(enabled = true, immediate = true)
public class ArgumentConverterStoryRunner extends AbstractComponent implements
        Converter {
    private final ConcurrentSkipListSet<StoryRunnerService> storyRunnerServices = new ConcurrentSkipListSet<StoryRunnerService>();

    @Reference
    @Override
    protected void bindLogService(LogService logService) {
        super.bindLogService(logService);
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MULTIPLE)
    protected void bindStoryRunnerService(
            StoryRunnerService storyRunnerService, Map<?, ?> properties) {
        getStoryRunnerServices().add(storyRunnerService);
    }

    @Override
    public Object convert(Class<?> desiredType, Object inArg) throws Exception {

        if (desiredType == StoryRunnerService.class) {

            if (inArg instanceof String) {
                for (StoryRunnerService runnerService : getStoryRunnerServices()) {
                    if (runnerService.getStoryClassName().equalsIgnoreCase(
                            (String) inArg)) {
                        return runnerService;
                    }
                }
            }
            if (inArg instanceof Long) {
                StoryRunnerService[] allrunners = getStoryRunnerServices()
                        .toArray(
                                new StoryRunnerService[getStoryRunnerServices()
                                        .size()]);
                int id = ((Long) inArg).intValue();
                if (id < 1 || id > allrunners.length + 1) {
                    throw new IllegalArgumentException("StoryRunner with id '"
                            + id + "' doesn't exists");
                }
                return allrunners[id - 1];
            }
        }
        if (desiredType == Boolean.TYPE) {
            if (inArg == Boolean.TYPE) {
                return inArg;
            } else if (inArg instanceof String) {
                return Boolean.parseBoolean((String) inArg);
            }
        }

        return null;
    }

    protected void unbindStoryRunnerService(
            StoryRunnerService storyRunnerService, Map<?, ?> properties) {
        getStoryRunnerServices().remove(storyRunnerService);
    }

    public CharSequence format(Object target, int level, Converter escape)
            throws Exception {
        if (target instanceof Dictionary<?, ?>) {
            Dictionary<?, ?> dic = (Dictionary<?, ?>) target;
            return printDictionary(dic);
        }

        if (target instanceof List<?>) {
            List<?> list = (List<?>) target;
            if (checkDictionaryElements(list)) {
                StringBuilder builder = new StringBuilder();
                for (Object dic : list) {
                    builder.append("StoryRunner:\r\n");
                    builder.append(printDictionary((Dictionary<?, ?>) dic));
                    builder.append("\r\n");
                    builder.append("\r\n");
                }
                return builder.toString();
            }
        }

        return null;
    }

    protected ConcurrentSkipListSet<StoryRunnerService> getStoryRunnerServices() {
        return storyRunnerServices;
    }

    private boolean checkDictionaryElements(List<?> list) {
        for (Object element : list) {
            if (!(element instanceof Dictionary<?, ?>)) {
                return false;
            }
        }

        return true;
    }

    private String printDictionary(Dictionary<?, ?> dic) {
        int count = dic.size();
        String[] keys = new String[count];
        Enumeration<?> keysEnum = dic.keys();
        int i = 0;
        while (keysEnum.hasMoreElements()) {
            keys[i++] = (String) keysEnum.nextElement();
        }
        Util.sortByString(keys);

        StringBuilder builder = new StringBuilder();
        for (i = 0; i < count; i++) {
            builder.append(" " + keys[i] + " = " + dic.get(keys[i]));
            builder.append("\r\n");
        }
        builder.append("\r\n");
        return builder.toString();
    }

}
