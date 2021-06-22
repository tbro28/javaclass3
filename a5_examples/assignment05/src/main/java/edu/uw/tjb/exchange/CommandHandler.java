package edu.uw.tjb.exchange;

import java.net.Socket;
import edu.uw.ext.framework.exchange.StockExchange;


/**
 * An instance of this class is dedicated to executing commands received from clients.
 */
public class CommandHandler implements Runnable {

    private Socket sock;
    private StockExchange realExchange;


    public CommandHandler(Socket sock, StockExchange realExchange) {
        this.sock = sock;
        this.realExchange = realExchange;
    }

    /**
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
}
