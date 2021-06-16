package edu.uw.tjb.broker;

import edu.uw.ext.framework.broker.*;
import edu.uw.ext.framework.order.StopBuyOrder;
import edu.uw.ext.framework.order.StopSellOrder;
import edu.uw.ext.framework.broker.OrderQueue;

import java.util.function.Consumer;

/**
 * Maintains queues to different types of orders and
 * requests the execution of orders when price conditions allow their execution.
 */
public class SimpleOrderManager implements OrderManager {

    private String stockTickerSymbol;
    private int price;


    /**
     * Queue for stop buy orders
     */
    protected OrderQueue<Integer, StopBuyOrder>	stopBuyOrderQueue;


    /**
     * Queue for stop sell orders
     */
    protected OrderQueue<Integer, StopSellOrder> stopSellOrderQueue;


    /**
     * Constructor.
     *
     * @param stockTickerSymbol
     */
    protected SimpleOrderManager (String stockTickerSymbol) {
        this.stockTickerSymbol = stockTickerSymbol;
    }


    /**
     * Constructor.
     *
     * @param stockTickerSymbol
     * @param price
     */
    public SimpleOrderManager (String stockTickerSymbol, int price) {
        this.stockTickerSymbol = stockTickerSymbol;
        this.price = price;
    }


    /**
     * Gets the stock ticker symbol for the stock managed by this stock manager.
     *
     * @return
     */
    @Override
    public String getSymbol() {
        return stockTickerSymbol;
    }

    /**
     * Respond to a stock price adjustment by setting threshold on dispatch filters.
     *
     * @param price
     */
    @Override
    public void adjustPrice(int price) {
        this.price = price;
    }

    /**
     * Queue a stop buy order.
     *
     * @param stopBuyOrder
     */
    @Override
    public void queueOrder(StopBuyOrder stopBuyOrder) {
        stopBuyOrderQueue.enqueue(stopBuyOrder);
    }

    /**
     * Queue a stop sell order.
     *
     * @param stopSellOrder
     */
    @Override
    public void queueOrder(StopSellOrder stopSellOrder) {
        stopSellOrderQueue.enqueue(stopSellOrder);
        //stopSellOrder.getPrice()
    }

    /**
     * Registers the processor to be used during buy order processing.
     * ?
     * @param consumer
     */
    @Override
    public void setBuyOrderProcessor(Consumer<StopBuyOrder> consumer) {
        stopBuyOrderQueue.setConsumer(consumer);
    }

    /**
     * Registers the processor to be used during sell order processing.
     * ?
     * @param consumer
     */
    @Override
    public void setSellOrderProcessor(Consumer<StopSellOrder> consumer) {
        stopSellOrderQueue.setConsumer(consumer);
    }
}
