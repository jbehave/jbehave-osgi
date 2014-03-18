package org.jbehave.osgi.core.configuration.dto;

import java.util.ArrayList;
import java.util.List;

public class StepFactoryDTO {

	private final String name;
	private List<String> stepClassList = new ArrayList<>();
	
	public StepFactoryDTO (String name){
		this.name = name;
	}
	public StepFactoryDTO (String name, String...classesName){
		this.name = name;
		for (int i = 0; i < classesName.length; i++) {
			stepClassList.add(classesName[i]);
		}
	}
	public String getName() {
		return name;
	}
	public List<String> getStepClasses() {
		return stepClassList;
	}
}
