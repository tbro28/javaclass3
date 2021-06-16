package edu.uw.tjb.broker;

import edu.uw.ext.framework.account.AccountManager;
import edu.uw.ext.framework.broker.Broker;
import edu.uw.ext.framework.broker.BrokerFactory;
import edu.uw.ext.framework.exchange.StockExchange;

/**
 * BrokerFactory implementation that returns a ExecutorBroker.
 *
 * @author Russ Moul
 */
public final class SimpleBrokerFactory implements BrokerFactory {
    /**
     * Instantiates a new ExecutorBroker.
     *
     * @param name the broker's name
     * @param acctMngr the account manager to be used by the broker
     * @param exch the exchange to be used by the broker
     *
     * @return a newly created ExecutorBroker instance
      */
    @Override
    public Broker newBroker(final String name, final AccountManager acctMngr,
                            final StockExchange exch) {
        return new SimpleBroker(name, acctMngr, exch);
    }
}

