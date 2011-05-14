package org.jbehave.osgi.commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.karaf.shell.console.Completer;
import org.apache.karaf.shell.console.completer.StringsCompleter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassNamesCompleter implements Completer {

    private List<String> includeNames = new ArrayList<String>();
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassNamesCompleter.class);

	
	@Override
	public int complete(String buffer, int cursor, List<String> candidates) {		
		StringsCompleter delegate = new StringsCompleter();
		delegate.getStrings().addAll(includeNames);
		return delegate.complete(buffer, cursor, candidates);
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
}
