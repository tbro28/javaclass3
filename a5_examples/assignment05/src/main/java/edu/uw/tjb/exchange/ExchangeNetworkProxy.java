package edu.uw.tjb.exchange;

import edu.uw.ext.framework.exchange.ExchangeListener;
import edu.uw.ext.framework.exchange.StockExchange;
import edu.uw.ext.framework.exchange.StockQuote;
import edu.uw.ext.framework.order.Order;

import java.util.Optional;

public class ExchangeNetworkProxy implements StockExchange {


    private String cmdIpAddress;
    private int cmdPort;
    private NetEventProcessor netEventProcessor;


    /**
     * eventIpAddress - the multicast IP address to connect to
     * eventPort - the multicast port to connect to
     * cmdIpAddress - the address the exchange accepts request on
     * cmdPort - the address the exchange accepts request on
     *
     * @param eventIpAddress
     * @param eventPort
     * @param cmdIpAddress
     * @param cmdPort
     */
    public ExchangeNetworkProxy(String eventIpAddress,
                                int eventPort,
                                String cmdIpAddress,
                                int cmdPort) {

        this.cmdIpAddress = cmdIpAddress;
        this.cmdPort = cmdPort;
        netEventProcessor = new NetEventProcessor(eventIpAddress, eventPort);


    }



    /**
     * The state of the exchange.
     *
     * @return
     */
    @Override
    public boolean isOpen() {
        return false;
    }



    /**
     * Gets the ticker symbols for all of the stocks in the traded on the exchange.
     *
     * @return
     */
    @Override
    public String[] getTickers() {
        return new String[0];
    }



    /**
     * Gets a stocks current price.
     *
     * @param ticker
     * @return
     */
    @Override
    public Optional<StockQuote> getQuote(String ticker) {
        return Optional.empty();
    }



    /**
     * Adds a market listener.
     *
     * @param exchangeListener
     */
    @Override
    public void addExchangeListener(ExchangeListener exchangeListener) {

    }



    /**
     * Removes a market listener.
     *
     * @param exchangeListener
     */
    @Override
    public void removeExchangeListener(ExchangeListener exchangeListener) {

    }



    /**
     * Creates a command to execute a trade and sends it to the exchange.
     *
     * @param order
     * @return
     */
    @Override
    public int executeTrade(Order order) {
        return 0;
    }
}
