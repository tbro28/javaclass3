package edu.uw.tjb.broker;

import edu.uw.ext.framework.account.Account;
import edu.uw.ext.framework.account.AccountException;
import edu.uw.ext.framework.account.AccountManager;
import edu.uw.ext.framework.broker.*;
import edu.uw.ext.framework.exchange.ExchangeEvent;
import edu.uw.ext.framework.exchange.ExchangeListener;
import edu.uw.ext.framework.exchange.StockExchange;
import edu.uw.ext.framework.exchange.StockQuote;
import edu.uw.ext.framework.order.MarketBuyOrder;
import edu.uw.ext.framework.order.MarketSellOrder;
import edu.uw.ext.framework.order.StopBuyOrder;
import edu.uw.ext.framework.order.StopSellOrder;
import edu.uw.ext.framework.order.Order;
import edu.uw.ext.framework.broker.OrderManager;
import edu.uw.tjb.account.SimpleAccount;

import java.util.Optional;

/**
 * An implementation of the Broker interface, provides a full implementation less
 * the creation of the order manager and market queue.
 */
public class SimpleBroker implements Broker, ExchangeListener {

    /*
    name
    account
    Optional<StockQuote>
    */

    private String name;
    AccountManager accountManager;
    StockExchange stockExchange;



    /**
     * The market order queue.
     */
    protected OrderQueue<Boolean, Order> marketOrders;

    /**
     * Constructor.
     *
     * @param brokerName
     * @param acctMgr
     * @param exchg
     */
    public SimpleBroker (String brokerName, AccountManager acctMgr, StockExchange exchg) {
        this(brokerName, exchg, acctMgr);
    }

    /**
     * Constructor for subclasses.
     * ?
     * @param brokerName
     * @param exchg
     * @param acctMgr
     */
    protected SimpleBroker (String brokerName, StockExchange exchg, AccountManager acctMgr) {
        name = brokerName;
        stockExchange = exchg;
        accountManager = acctMgr;
    }


    /**
     * Get the name of the broker.
     *
     * @return
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Create an account with the broker.
     *
     * @param username
     * @param password
     * @param balance
     * @return
     * @throws BrokerException
     */
    @Override
    public Account createAccount(String username, String password, int balance) throws BrokerException {

        try {
            return new SimpleAccount(username, password.getBytes(), balance);
        } catch (AccountException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Create an appropriate order manager for this broker.
     *
     * @param ticker
     * @param initialPrice
     * @return
     */
    protected OrderManager createOrderManager(String ticker, int initialPrice) {
        return new SimpleOrderManager(ticker, initialPrice);
    }


    /**
     * Delete an account with the broker.
     *
     * @param username
     * @throws BrokerException
     */
    @Override
    public void deleteAccount(String username) throws BrokerException {

        /*
        * Delete the account.
        * */
        try {
            accountManager.deleteAccount(username);
        } catch (AccountException e) {
            e.printStackTrace();
        }

    }

////////////////////////////////////////////////////////////////////////////////

    /**
     * Locate an account with the broker.
     *
     * @param username
     * @param password
     * @return
     * @throws BrokerException
     */
    @Override
    public Account getAccount(String username, String password) throws BrokerException {

        try {
            accountManager.validateLogin(username, password);
        } catch (AccountException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Get a price quote for a stock.
     *
     * @param symbol
     * @return
     */
    @Override
    public Optional<StockQuote> requestQuote(String symbol) {

        return Optional.empty();
    }

    /**
     * Place an order with the broker.
     *
     * @param marketBuyOrder
     * @throws BrokerException
     */
    @Override
    public void placeOrder(MarketBuyOrder marketBuyOrder) throws BrokerException {

    }

    /**
     * Place an order with the broker.
     *
     * @param marketSellOrder
     * @throws BrokerException
     */
    @Override
    public void placeOrder(MarketSellOrder marketSellOrder) throws BrokerException {

    }

    /**
     * Place an order with the broker.
     *
     * @param stopBuyOrder
     * @throws BrokerException
     */
    @Override
    public void placeOrder(StopBuyOrder stopBuyOrder) throws BrokerException {

    }

    /**
     * Place an order with the broker.
     *
     * @param stopSellOrder
     * @throws BrokerException
     */
    @Override
    public void placeOrder(StopSellOrder stopSellOrder) throws BrokerException {

    }

    /**
     * Release broker resources.
     *
     * @throws BrokerException
     */
    @Override
    public void close() throws BrokerException {

    }


    /**
     * ExchangeListener interface methods.
     *
     * @param exchangeEvent
     */


    /**
     * Upon the exchange opening sets the market dispatch filter threshold and
     * processes any available orders.
     *
     * @param exchangeEvent
     */
    @Override
    public void exchangeOpened(ExchangeEvent exchangeEvent) {

    }

    /**
     * Upon the exchange opening sets the market dispatch filter threshold.
     *
     * @param exchangeEvent
     */
    @Override
    public void exchangeClosed(ExchangeEvent exchangeEvent) {

    }

    /**
     *
     * Upon the exchange opening sets the market dispatch filter threshold and
     * processes any available orders.
     *
     * @param exchangeEvent
     */
    @Override
    public void priceChanged(ExchangeEvent exchangeEvent) {

    }


    /**
     * Execute an order with the exchange, satisfies the Consumer<Order> functional interface.
     *
     * @param order
     */
    protected void executeOrder (Order order) {

    }


    /**
     * Fetch the stock list from the exchange and initialize an order manager for each stock.
     */
    protected void initializeOrderManagers() {

    }



}
