package org.jbehave.osgi.examples.trader.pomfirst.paxexam.converters;

import java.lang.reflect.Type;

import org.jbehave.core.steps.ParameterConverters.ParameterConverter;
import org.jbehave.core.steps.ParameterConverters.ParameterConvertionFailed;
import org.jbehave.osgi.examples.trader.pomfirst.bundle.model.Trader;
import org.jbehave.osgi.examples.trader.pomfirst.bundle.persistence.TraderPersister;

public class TraderConverter implements ParameterConverter {
	private TraderPersister persister;

	public TraderConverter(TraderPersister persister) {
		this.persister = persister;
	}

	public boolean accept(Type type) {
		if (type instanceof Class<?>) {
			return Trader.class.isAssignableFrom((Class<?>) type);
		}
		return false;
	}

	public Object convertValue(String value, Type type) {
		Trader trader = persister.retrieveTrader(value);
		if (trader == null) {
			throw new ParameterConvertionFailed("Trader not found for name "
					+ value, null);
		}
		return trader;
	}

}
