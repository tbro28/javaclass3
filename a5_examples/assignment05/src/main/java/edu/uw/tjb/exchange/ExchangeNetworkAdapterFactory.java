package edu.uw.tjb.exchange;

import edu.uw.ext.framework.exchange.ExchangeAdapter;
import edu.uw.ext.framework.exchange.NetworkExchangeAdapterFactory;
import edu.uw.ext.framework.exchange.StockExchange;

public class ExchangeNetworkAdapterFactory implements NetworkExchangeAdapterFactory {


    /**
     *
     * edu.uw.ext.framework.exchange.StockExchange exchange,
     * String multicastIP,
     * int multicastPort,
     * int commandPort
     *
     * @param exchange
     * @param multicastIP
     * @param multicastPort
     * @param commandPort
     * @return
     */
    @Override
    public ExchangeAdapter newAdapter(StockExchange exchange,
                                      String multicastIP,
                                      int multicastPort,
                                      int commandPort) {
        return new ExchangeNetworkAdapter(exchange, multicastIP, multicastPort, commandPort);
    }
}
