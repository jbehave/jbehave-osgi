package org.jbehave.osgi.examples.trader.service;

import org.jbehave.osgi.examples.trader.model.Stock;
import org.jbehave.osgi.examples.trader.model.Trader;

public class TradingService {

    public Stock newStock(String symbol, double threshold) {
        return new Stock(symbol, threshold);
    }

    public Trader newTrader(String name, String rank) {
        return new Trader(name, rank);
    }

}
