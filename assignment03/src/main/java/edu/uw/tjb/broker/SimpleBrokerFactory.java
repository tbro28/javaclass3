package edu.uw.tjb.broker;

import edu.uw.ext.framework.account.AccountManager;
import edu.uw.ext.framework.broker.Broker;
import edu.uw.ext.framework.broker.BrokerFactory;
import edu.uw.ext.framework.exchange.StockExchange;

/**
 * BrokerFactory implementation that returns a SimpleBroker.
 */
public class SimpleBrokerFactory  implements BrokerFactory {

    /**
     * Instantiates a new SimpleBroker.
     *
     * @param name
     * @param accountManager
     * @param stockExchange
     * @return
     */
    @Override
    public Broker newBroker(String name, AccountManager accountManager, StockExchange stockExchange) {
        return new SimpleBroker(name, accountManager, stockExchange);
    }

}
