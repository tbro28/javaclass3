package edu.uw.tjb.exchange;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.uw.ext.framework.exchange.StockExchange;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Accepts connections and passes them to a CommandHandler,
 * for the reading and processing of commands.
 */
public class CommandListener implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(CommandListener.class);


    private int commandPort;
    private StockExchange realExchange;
    private ExecutorService service = Executors.newCachedThreadPool();
    private ServerSocket serverSocket;


    public CommandListener(int commandPort, StockExchange realExchange) {
        this.commandPort = commandPort;
        this.realExchange = realExchange;
    }

    /**
     * Accept connections, and creates a CommandExecutor for servicing the connection.
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

        try {
            serverSocket = new ServerSocket(commandPort);

            log.info("Socket obtained: " + serverSocket.getLocalPort());

            while(!serverSocket.isClosed()){
                Socket sock = serverSocket.accept();
                service.execute(new CommandHandler(sock, realExchange));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        terminate();
    }


    /**
     * Terminates this thread gracefully.
     */
    public void terminate(){
        try {
            service.shutdown();
            serverSocket.close();
        } catch (IOException e) {
            log.error("Failed to close the socket", e);
            e.printStackTrace();
        }
    }

}
