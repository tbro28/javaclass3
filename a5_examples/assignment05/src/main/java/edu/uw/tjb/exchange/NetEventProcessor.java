package edu.uw.tjb.exchange;

import edu.uw.ext.framework.exchange.ExchangeListener;

/**
 * Listens for (by joining the multicast group) and processes events received from the exchange.
 * Processing the events consists of propagating them to registered listeners.
 */
public class NetEventProcessor implements Runnable {



    /**
     * Continuously accepts and processes market and price change events.
     *
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

    }


    /**
     * Adds a market listener.
     *
     * @param l
     */
    public void addExchangeListener(ExchangeListener l) {


    }


    /**
     * Removes a market listener.
     *
     * @param l
     */
    public void removeExchangeListener(ExchangeListener l) {


    }





}
