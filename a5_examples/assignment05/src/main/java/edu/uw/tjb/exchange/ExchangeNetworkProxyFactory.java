package edu.uw.tjb.exchange;

import edu.uw.ext.framework.exchange.NetworkExchangeProxyFactory;
import edu.uw.ext.framework.exchange.StockExchange;

public class ExchangeNetworkProxyFactory implements NetworkExchangeProxyFactory {


    /**
     * Instantiates a enabled ExchangeNetworkProxy.
     *
     * @param multicastIP
     * @param multicastPort
     * @param commandIP
     * @param commandPort
     * @return
     */
    @Override
    public StockExchange newProxy(String multicastIP,
                                  int multicastPort,
                                  String commandIP,
                                  int commandPort) {
        return new ExchangeNetworkProxy(multicastIP, multicastPort, commandIP, commandPort);
    }
}
