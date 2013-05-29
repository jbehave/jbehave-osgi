package org.jbehave.osgi.examples.trader.pomfirst.bundle.persistence;

import org.jbehave.osgi.examples.trader.pomfirst.bundle.model.Trader;

public class TraderPersister {

    private Trader[] traders;

    public TraderPersister(Trader... traders) {
        this.traders = traders;
    }

    public Trader retrieveTrader(String name) {
        for (Trader trader : traders) {
            if (trader.getName().equals(name)) {
                return trader;
            }
        }
        return null;
    }

}
