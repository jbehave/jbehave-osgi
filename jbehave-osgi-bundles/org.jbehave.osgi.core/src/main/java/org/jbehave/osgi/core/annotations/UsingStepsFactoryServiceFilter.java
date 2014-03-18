package org.jbehave.osgi.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Inherited
@Documented
/**
 * Determines which StepFactoryServices must be bound by a StoryRunnerService registered by an Embedder class annotated with it.
 * @author cvgaviao
 *
 */
public @interface UsingStepsFactoryServiceFilter {

	String custom() default "";

	/**
	 * An array of stepFactoryIds that will be used to create an filter using
	 * AND operator for every value.
	 * 
	 * @return
	 */
	String[] value() default {};
}
