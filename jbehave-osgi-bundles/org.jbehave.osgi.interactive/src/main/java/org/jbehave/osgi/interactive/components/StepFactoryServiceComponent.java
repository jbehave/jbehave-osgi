package org.jbehave.osgi.interactive.components;

import java.util.List;

import org.jbehave.core.steps.CandidateSteps;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.osgi.service.component.ComponentContext;

public class StepFactoryServiceComponent extends AbstractComponent implements
		InjectableStepsFactory {

	@Override
	protected void activate(ComponentContext context) {
		super.activate(context);
		System.out.println("Activate StepFactory Component by :" + context.getUsingBundle());
		// embedderOsgi = new EmbedderOsgi(context.getProperties());
	}

	@Override
	protected void deactivate(ComponentContext context) {
		super.deactivate(context);
		// embedderOsgi = null;
	}

	private void refreshCompositeStepsFactory() {
		// useStepsFactory(new CompositeStepsFactory(
		// injectableStepsFactories
		// .toArray(new InjectableStepsFactory[injectableStepsFactories
		// .size()])));
	}

	@Override
	public List<CandidateSteps> createCandidateSteps() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object createInstanceOfType(Class<?> type) {
		// TODO Auto-generated method stub
		return null;
	}
}
