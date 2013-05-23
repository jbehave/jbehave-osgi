package org.jbehave.osgi.interactive.services;

import java.util.List;

/**
 * 
 * @author Cristiano Gavião
 *
 */
public interface IStoryExecutionPlan {

	List<IStoryExecutionSchedule> getStoryExecutionSchedules();
}
